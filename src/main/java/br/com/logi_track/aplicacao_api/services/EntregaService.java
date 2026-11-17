package br.com.logi_track.aplicacao_api.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.exception.RecursoNaoEncontradoException;
import br.com.logi_track.aplicacao_api.models.Entrega;
import br.com.logi_track.aplicacao_api.models.Motorista;
import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.models.Volume;
import br.com.logi_track.aplicacao_api.repositories.CentroDistribuicaoRepository;
import br.com.logi_track.aplicacao_api.repositories.EntregaRepository;
import br.com.logi_track.aplicacao_api.repositories.MotoristaRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;
import br.com.logi_track.aplicacao_api.repositories.TransportadoraRepository;
import br.com.logi_track.aplicacao_api.repositories.VeiculoRepository;

@Service
public class EntregaService {

	private static final String SITUACAO_EM_TRANSITO = "em_transito";
	private static final String SITUACAO_ENTREGUE = "entregue";

	private final EntregaRepository entregaRepository;
	private final PedidoRepository pedidoRepository;
	private final MotoristaRepository motoristaRepository;
	private final VeiculoRepository veiculoRepository;
	private final TransportadoraRepository transportadoraRepository;
	private final CentroDistribuicaoRepository centroDistribuicaoRepository;
	private final ProdutoRepository produtoRepository;

	public EntregaService(
			EntregaRepository entregaRepository,
			PedidoRepository pedidoRepository,
			MotoristaRepository motoristaRepository,
			VeiculoRepository veiculoRepository,
			TransportadoraRepository transportadoraRepository,
			CentroDistribuicaoRepository centroDistribuicaoRepository,
			ProdutoRepository produtoRepository) {
		this.entregaRepository = entregaRepository;
		this.pedidoRepository = pedidoRepository;
		this.motoristaRepository = motoristaRepository;
		this.veiculoRepository = veiculoRepository;
		this.transportadoraRepository = transportadoraRepository;
		this.centroDistribuicaoRepository = centroDistribuicaoRepository;
		this.produtoRepository = produtoRepository;
	}

	public Entrega criar(Entrega entrega) {
		validarReferencias(entrega);
		LocalDateTime agora = LocalDateTime.now();
		entrega.setId(null);
		if (entrega.getSituacao() == null || entrega.getSituacao().isBlank()) {
			entrega.setSituacao("no_cd");
		}
		if (entrega.getAtrasado() == null) {
			entrega.setAtrasado(Boolean.FALSE);
		}

		if (SITUACAO_EM_TRANSITO.equals(entrega.getSituacao())) {
			baixarEstoqueDoPedido(entrega.getIdPedido());
		}

		entrega.setCriadoEm(agora);
		entrega.setAtualizadoEm(agora);
		return entregaRepository.save(entrega);
	}

	public List<Entrega> listarTodos() {
		return entregaRepository.findAll();
	}

	public List<Entrega> listarAtrasadas() {
		return entregaRepository.findByAtrasadoTrue();
	}

	public List<Entrega> listarPorMotorista(String idMotorista) {
		return entregaRepository.findByIdMotorista(new ObjectId(idMotorista));
	}

	public Entrega buscarPorId(String id) {
		return entregaRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Entrega nao encontrada: " + id));
	}

	public Map<String, Object> buscarComRelacionados(String id) {
		Entrega entrega = buscarPorId(id);
		Pedido pedido = pedidoRepository.findById(entrega.getIdPedido())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Pedido referenciado pela entrega nao existe: " + entrega.getIdPedido()));
		Motorista motorista = motoristaRepository.findById(entrega.getIdMotorista())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Motorista referenciado pela entrega nao existe: " + entrega.getIdMotorista()));

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("entrega", entrega);
		resultado.put("pedido", pedido);
		resultado.put("motorista", motorista);
		return resultado;
	}

	public Entrega atualizar(String id, Entrega dados) {
		Entrega existente = buscarPorId(id);
		validarReferencias(dados);

		String situacaoAnterior = existente.getSituacao();
		String situacaoNova = dados.getSituacao();
		boolean passouParaEmTransito = !SITUACAO_EM_TRANSITO.equals(situacaoAnterior)
				&& SITUACAO_EM_TRANSITO.equals(situacaoNova);

		if (passouParaEmTransito) {
			baixarEstoqueDoPedido(existente.getIdPedido());
		}

		existente.setPedido(dados.getPedido());
		existente.setIdTransportadora(dados.getIdTransportadora());
		existente.setMotorista(dados.getMotorista());
		existente.setIdVeiculo(dados.getIdVeiculo());
		existente.setIdCentroDistribuicao(dados.getIdCentroDistribuicao());
		existente.setSituacao(dados.getSituacao());
		existente.setAtrasado(dados.getAtrasado());
		existente.setPrometidoPara(dados.getPrometidoPara());
		existente.setDestino(dados.getDestino());
		existente.setRota(dados.getRota());
		existente.setHorariosEtapas(dados.getHorariosEtapas());
		existente.setRastreioAtual(dados.getRastreioAtual());
		existente.setAtualizadoEm(LocalDateTime.now());
		return entregaRepository.save(existente);
	}

	public void excluir(String id) {
		Entrega existente = buscarPorId(id);
		String situacao = existente.getSituacao();
		if (SITUACAO_EM_TRANSITO.equals(situacao) || SITUACAO_ENTREGUE.equals(situacao)) {
			throw new NegocioException(
					"Nao e permitido excluir entrega que ja saiu do CD.",
					HttpStatus.BAD_REQUEST);
		}
		entregaRepository.deleteById(id);
	}

	private void baixarEstoqueDoPedido(String idPedido) {
		Pedido pedido = pedidoRepository.findById(idPedido)
				.orElseThrow(() -> new NegocioException(
						"Pedido inexistente para baixa de estoque: " + idPedido,
						HttpStatus.BAD_REQUEST));
		if (pedido.getVolumes() == null || pedido.getVolumes().isEmpty()) {
			throw new NegocioException("Pedido sem volumes para baixa de estoque.", HttpStatus.BAD_REQUEST);
		}

		Map<String, Integer> porProduto = somarPorProduto(pedido.getVolumes());
		for (Map.Entry<String, Integer> entry : porProduto.entrySet()) {
			Produto produto = produtoRepository.findById(entry.getKey())
					.orElseThrow(() -> new NegocioException(
							"Produto inexistente no volume: " + entry.getKey(),
							HttpStatus.BAD_REQUEST));
			int estoque = produto.getQuantidadeEstoque() == null ? 0 : produto.getQuantidadeEstoque();
			int reservada = produto.getQuantidadeReservada() == null ? 0 : produto.getQuantidadeReservada();
			int quantidade = entry.getValue();
			if (estoque < quantidade || reservada < quantidade) {
				throw new NegocioException(
						"Estoque insuficiente para o produto: " + produto.getNome(),
						HttpStatus.BAD_REQUEST);
			}
			produto.setQuantidadeEstoque(estoque - quantidade);
			produto.setQuantidadeReservada(reservada - quantidade);
			produtoRepository.save(produto);
		}

		pedido.setSituacao(SITUACAO_EM_TRANSITO);
		pedido.setAtualizadoEm(LocalDateTime.now());
		pedidoRepository.save(pedido);
	}

	private Map<String, Integer> somarPorProduto(List<Volume> volumes) {
		Map<String, Integer> porProduto = new LinkedHashMap<>();
		for (Volume volume : volumes) {
			int qtd = volume.getQuantidade() == null ? 0 : volume.getQuantidade();
			porProduto.merge(volume.getIdProduto(), qtd, Integer::sum);
		}
		return porProduto;
	}

	private void validarReferencias(Entrega entrega) {
		if (entrega.getIdPedido() == null || entrega.getIdPedido().isBlank()) {
			throw new NegocioException("idPedido e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (!pedidoRepository.existsById(entrega.getIdPedido())) {
			throw new NegocioException("Pedido inexistente: " + entrega.getIdPedido(), HttpStatus.BAD_REQUEST);
		}
		if (entrega.getIdMotorista() == null || entrega.getIdMotorista().isBlank()) {
			throw new NegocioException("idMotorista e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (!motoristaRepository.existsById(entrega.getIdMotorista())) {
			throw new NegocioException("Motorista inexistente: " + entrega.getIdMotorista(), HttpStatus.BAD_REQUEST);
		}
		if (entrega.getIdTransportadora() == null || entrega.getIdTransportadora().isBlank()
				|| !transportadoraRepository.existsById(entrega.getIdTransportadora())) {
			throw new NegocioException("Transportadora inexistente ou obrigatoria.", HttpStatus.BAD_REQUEST);
		}
		if (entrega.getIdVeiculo() == null || entrega.getIdVeiculo().isBlank()
				|| !veiculoRepository.existsById(entrega.getIdVeiculo())) {
			throw new NegocioException("Veiculo inexistente ou obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (entrega.getIdCentroDistribuicao() == null || entrega.getIdCentroDistribuicao().isBlank()
				|| !centroDistribuicaoRepository.existsById(entrega.getIdCentroDistribuicao())) {
			throw new NegocioException("Centro de distribuicao inexistente ou obrigatorio.", HttpStatus.BAD_REQUEST);
		}
	}
}

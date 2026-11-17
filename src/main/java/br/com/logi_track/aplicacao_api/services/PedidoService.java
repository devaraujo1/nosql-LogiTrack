package br.com.logi_track.aplicacao_api.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.exception.RecursoNaoEncontradoException;
import br.com.logi_track.aplicacao_api.models.Cliente;
import br.com.logi_track.aplicacao_api.models.Entrega;
import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.models.Volume;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;
import br.com.logi_track.aplicacao_api.repositories.EntregaRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;

@Service
public class PedidoService {

	private static final String CANCELADO = "cancelado";
	private static final String EM_TRANSITO = "em_transito";
	private static final String ENTREGUE = "entregue";

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ProdutoRepository produtoRepository;
	private final EntregaRepository entregaRepository;

	public PedidoService(
			PedidoRepository pedidoRepository,
			ClienteRepository clienteRepository,
			ProdutoRepository produtoRepository,
			EntregaRepository entregaRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.produtoRepository = produtoRepository;
		this.entregaRepository = entregaRepository;
	}

	public Pedido criar(Pedido pedido) {
		validarCampos(pedido);
		if (CANCELADO.equals(pedido.getSituacao())) {
			throw new NegocioException("Nao e permitido criar pedido ja cancelado.", HttpStatus.BAD_REQUEST);
		}
		reservarVolumes(pedido.getVolumes());
		LocalDateTime agora = LocalDateTime.now();
		pedido.setId(null);
		if (pedido.getSituacao() == null || pedido.getSituacao().isBlank()) {
			pedido.setSituacao("criado");
		}
		pedido.setCriadoEm(agora);
		pedido.setAtualizadoEm(agora);
		return pedidoRepository.save(pedido);
	}

	public List<Pedido> listarTodos() {
		return pedidoRepository.findAll();
	}

	public List<Pedido> listarPorCliente(String idCliente) {
		return pedidoRepository.findByIdCliente(new ObjectId(idCliente));
	}

	public Pedido buscarPorId(String id) {
		return pedidoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: " + id));
	}

	public Map<String, Object> buscarComCliente(String id) {
		Pedido pedido = buscarPorId(id);
		Cliente cliente = pedido.getCliente();
		if (cliente == null || cliente.getId() == null) {
			throw new RecursoNaoEncontradoException("Pedido sem cliente referenciado: " + id);
		}
		Cliente completo = clienteRepository.findById(cliente.getId())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Cliente referenciado pelo pedido nao existe: " + cliente.getId()));

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("pedido", pedido);
		resultado.put("cliente", completo);
		return resultado;
	}

	public Pedido atualizar(String id, Pedido dados) {
		Pedido existente = buscarPorId(id);
		validarCampos(dados);

		boolean jaSaiuDoCd = pedidoJaSaiuDoCd(existente);
		boolean eraCancelado = CANCELADO.equals(existente.getSituacao());
		boolean vaiCancelar = CANCELADO.equals(dados.getSituacao());

		boolean reservaAtiva = !jaSaiuDoCd && !eraCancelado;
		boolean novaReserva = !jaSaiuDoCd && !vaiCancelar;

		Map<String, Integer> antigo = reservaAtiva
				? somarPorProduto(existente.getVolumes())
				: Collections.emptyMap();
		Map<String, Integer> novo = novaReserva
				? somarPorProduto(dados.getVolumes())
				: Collections.emptyMap();

		ajustarReservaPorDelta(antigo, novo);

		existente.setCliente(dados.getCliente());
		existente.setSituacao(dados.getSituacao());
		existente.setOrigem(dados.getOrigem());
		existente.setDestino(dados.getDestino());
		existente.setVolumes(dados.getVolumes());
		existente.setPagamento(dados.getPagamento());
		existente.setAtualizadoEm(LocalDateTime.now());
		return pedidoRepository.save(existente);
	}

	public void excluir(String id) {
		Pedido existente = buscarPorId(id);
		boolean jaSaiuDoCd = pedidoJaSaiuDoCd(existente);
		boolean eraCancelado = CANCELADO.equals(existente.getSituacao());
		if (!jaSaiuDoCd && !eraCancelado) {
			liberarVolumes(existente.getVolumes());
		}
		pedidoRepository.deleteById(id);
	}

	private void validarCampos(Pedido pedido) {
		if (pedido.getIdCliente() == null || pedido.getIdCliente().isBlank()) {
			throw new NegocioException("idCliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (!clienteRepository.existsById(pedido.getIdCliente())) {
			throw new NegocioException("Cliente inexistente: " + pedido.getIdCliente(), HttpStatus.BAD_REQUEST);
		}
		if (pedido.getVolumes() == null || pedido.getVolumes().isEmpty()) {
			throw new NegocioException("Nao e permitido cadastrar pedido sem volumes.", HttpStatus.BAD_REQUEST);
		}
		if (pedido.getOrigem() == null || pedido.getDestino() == null) {
			throw new NegocioException("Origem e destino sao obrigatorios.", HttpStatus.BAD_REQUEST);
		}
		if (pedido.getPagamento() == null) {
			throw new NegocioException("Pagamento e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		for (Volume volume : pedido.getVolumes()) {
			if (volume.getIdProduto() == null || volume.getIdProduto().isBlank()) {
				throw new NegocioException("idProduto e obrigatorio em cada volume.", HttpStatus.BAD_REQUEST);
			}
			if (volume.getQuantidade() == null || volume.getQuantidade() <= 0) {
				throw new NegocioException("Quantidade do volume deve ser maior que zero.", HttpStatus.BAD_REQUEST);
			}
			if (!produtoRepository.existsById(volume.getIdProduto())) {
				throw new NegocioException(
						"Produto inexistente no volume: " + volume.getIdProduto(),
						HttpStatus.BAD_REQUEST);
			}
		}
	}

	private void ajustarReservaPorDelta(Map<String, Integer> antigo, Map<String, Integer> novo) {
		Set<String> ids = new HashSet<>();
		ids.addAll(antigo.keySet());
		ids.addAll(novo.keySet());

		for (String idProduto : ids) {
			int qtdAntiga = antigo.getOrDefault(idProduto, 0);
			int qtdNova = novo.getOrDefault(idProduto, 0);
			int delta = qtdNova - qtdAntiga;
			if (delta == 0) {
				continue;
			}

			Produto produto = produtoRepository.findById(idProduto)
					.orElseThrow(() -> new NegocioException(
							"Produto inexistente no volume: " + idProduto,
							HttpStatus.BAD_REQUEST));
			int reservada = produto.getQuantidadeReservada() == null ? 0 : produto.getQuantidadeReservada();

			if (delta > 0) {
				int disponivel = produto.getQuantidadeDisponivel();
				if (disponivel < delta) {
					throw new NegocioException(
							"Estoque insuficiente para o produto: " + produto.getNome(),
							HttpStatus.BAD_REQUEST);
				}
				produto.setQuantidadeReservada(reservada + delta);
			} else {
				produto.setQuantidadeReservada(Math.max(reservada + delta, 0));
			}
			produtoRepository.save(produto);
		}
	}

	private void reservarVolumes(List<Volume> volumes) {
		Map<String, Integer> porProduto = somarPorProduto(volumes);
		for (Map.Entry<String, Integer> entry : porProduto.entrySet()) {
			Produto produto = produtoRepository.findById(entry.getKey())
					.orElseThrow(() -> new NegocioException(
							"Produto inexistente no volume: " + entry.getKey(),
							HttpStatus.BAD_REQUEST));
			int disponivel = produto.getQuantidadeDisponivel();
			int quantidade = entry.getValue();
			if (disponivel < quantidade) {
				throw new NegocioException(
						"Estoque insuficiente para o produto: " + produto.getNome(),
						HttpStatus.BAD_REQUEST);
			}
			int reservada = produto.getQuantidadeReservada() == null ? 0 : produto.getQuantidadeReservada();
			produto.setQuantidadeReservada(reservada + quantidade);
			produtoRepository.save(produto);
		}
	}

	private void liberarVolumes(List<Volume> volumes) {
		if (volumes == null || volumes.isEmpty()) {
			return;
		}
		Map<String, Integer> porProduto = somarPorProduto(volumes);
		for (Map.Entry<String, Integer> entry : porProduto.entrySet()) {
			Produto produto = produtoRepository.findById(entry.getKey())
					.orElseThrow(() -> new NegocioException(
							"Produto inexistente no volume: " + entry.getKey(),
							HttpStatus.BAD_REQUEST));
			int reservada = produto.getQuantidadeReservada() == null ? 0 : produto.getQuantidadeReservada();
			int nova = reservada - entry.getValue();
			produto.setQuantidadeReservada(Math.max(nova, 0));
			produtoRepository.save(produto);
		}
	}

	private Map<String, Integer> somarPorProduto(List<Volume> volumes) {
		Map<String, Integer> porProduto = new LinkedHashMap<>();
		if (volumes == null) {
			return porProduto;
		}
		for (Volume volume : volumes) {
			int qtd = volume.getQuantidade() == null ? 0 : volume.getQuantidade();
			porProduto.merge(volume.getIdProduto(), qtd, Integer::sum);
		}
		return porProduto;
	}

	private boolean pedidoJaSaiuDoCd(Pedido pedido) {
		String situacaoPedido = pedido.getSituacao();
		if (EM_TRANSITO.equals(situacaoPedido) || ENTREGUE.equals(situacaoPedido)) {
			return true;
		}
		if (pedido.getId() == null) {
			return false;
		}
		return entregaRepository.findByIdPedido(new ObjectId(pedido.getId()))
				.map(Entrega::getSituacao)
				.map(sit -> EM_TRANSITO.equals(sit) || ENTREGUE.equals(sit))
				.orElse(false);
	}
}

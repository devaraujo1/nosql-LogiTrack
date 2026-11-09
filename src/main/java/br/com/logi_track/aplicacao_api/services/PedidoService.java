package br.com.logi_track.aplicacao_api.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.exception.RecursoNaoEncontradoException;
import br.com.logi_track.aplicacao_api.models.Cliente;
import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ProdutoRepository produtoRepository;

	public PedidoService(
			PedidoRepository pedidoRepository,
			ClienteRepository clienteRepository,
			ProdutoRepository produtoRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.produtoRepository = produtoRepository;
	}

	public Pedido criar(Pedido pedido) {
		validar(pedido);
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
		return pedidoRepository.findByIdCliente(idCliente);
	}

	public Pedido buscarPorId(String id) {
		return pedidoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: " + id));
	}

	public Map<String, Object> buscarComCliente(String id) {
		Pedido pedido = buscarPorId(id);
		Cliente cliente = clienteRepository.findById(pedido.getIdCliente())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Cliente referenciado pelo pedido nao existe: " + pedido.getIdCliente()));

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("pedido", pedido);
		resultado.put("cliente", cliente);
		return resultado;
	}

	public Pedido atualizar(String id, Pedido dados) {
		Pedido existente = buscarPorId(id);
		validar(dados);
		existente.setIdCliente(dados.getIdCliente());
		existente.setSituacao(dados.getSituacao());
		existente.setOrigem(dados.getOrigem());
		existente.setDestino(dados.getDestino());
		existente.setVolumes(dados.getVolumes());
		existente.setPagamento(dados.getPagamento());
		existente.setAtualizadoEm(LocalDateTime.now());
		return pedidoRepository.save(existente);
	}

	public void excluir(String id) {
		if (!pedidoRepository.existsById(id)) {
			throw new RecursoNaoEncontradoException("Pedido nao encontrado: " + id);
		}
		pedidoRepository.deleteById(id);
	}

	private void validar(Pedido pedido) {
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
		pedido.getVolumes().forEach(volume -> {
			if (volume.getIdProduto() != null && !volume.getIdProduto().isBlank()
					&& !produtoRepository.existsById(volume.getIdProduto())) {
				throw new NegocioException(
						"Produto inexistente no volume: " + volume.getIdProduto(),
						HttpStatus.BAD_REQUEST);
			}
		});
	}
}

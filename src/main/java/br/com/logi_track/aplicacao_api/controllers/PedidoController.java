package br.com.logi_track.aplicacao_api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.services.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
		Pedido criado = pedidoService.criar(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@GetMapping
	public ResponseEntity<List<Pedido>> listar(@RequestParam(required = false) String idCliente) {
		if (idCliente != null && !idCliente.isBlank()) {
			return ResponseEntity.ok(pedidoService.listarPorCliente(idCliente));
		}
		return ResponseEntity.ok(pedidoService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscarPorId(@PathVariable String id) {
		return ResponseEntity.ok(pedidoService.buscarPorId(id));
	}

	@GetMapping("/{id}/com-cliente")
	public ResponseEntity<Map<String, Object>> buscarComCliente(@PathVariable String id) {
		return ResponseEntity.ok(pedidoService.buscarComCliente(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pedido> atualizar(@PathVariable String id, @RequestBody Pedido pedido) {
		return ResponseEntity.ok(pedidoService.atualizar(id, pedido));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable String id) {
		pedidoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}

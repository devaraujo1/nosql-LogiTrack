package br.com.logi_track.aplicacao_api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.services.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@GetMapping
	public ResponseEntity<List<Produto>> listar() {
		return ResponseEntity.ok(produtoService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPorId(@PathVariable String id) {
		return ResponseEntity.ok(produtoService.buscarPorId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> reporEstoque(
			@PathVariable String id,
			@RequestBody Map<String, Integer> body) {
		Integer quantidadeEstoque = body == null ? null : body.get("quantidadeEstoque");
		return ResponseEntity.ok(produtoService.reporEstoque(id, quantidadeEstoque));
	}
}

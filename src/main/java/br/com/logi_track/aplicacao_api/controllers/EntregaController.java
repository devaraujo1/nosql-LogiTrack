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

import br.com.logi_track.aplicacao_api.models.Entrega;
import br.com.logi_track.aplicacao_api.services.EntregaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

	private final EntregaService entregaService;

	public EntregaController(EntregaService entregaService) {
		this.entregaService = entregaService;
	}

	@PostMapping
	public ResponseEntity<Entrega> criar(@Valid @RequestBody Entrega entrega) {
		Entrega criada = entregaService.criar(entrega);
		return ResponseEntity.status(HttpStatus.CREATED).body(criada);
	}

	@GetMapping
	public ResponseEntity<List<Entrega>> listar(
			@RequestParam(required = false) String idMotorista,
			@RequestParam(required = false) Boolean atrasadas) {
		if (Boolean.TRUE.equals(atrasadas)) {
			return ResponseEntity.ok(entregaService.listarAtrasadas());
		}
		if (idMotorista != null && !idMotorista.isBlank()) {
			return ResponseEntity.ok(entregaService.listarPorMotorista(idMotorista));
		}
		return ResponseEntity.ok(entregaService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Entrega> buscarPorId(@PathVariable String id) {
		return ResponseEntity.ok(entregaService.buscarPorId(id));
	}

	@GetMapping("/{id}/relacionados")
	public ResponseEntity<Map<String, Object>> buscarComRelacionados(@PathVariable String id) {
		return ResponseEntity.ok(entregaService.buscarComRelacionados(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Entrega> atualizar(@PathVariable String id, @Valid @RequestBody Entrega entrega) {
		return ResponseEntity.ok(entregaService.atualizar(id, entrega));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable String id) {
		entregaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}

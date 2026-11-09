package br.com.logi_track.aplicacao_api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.exception.RecursoNaoEncontradoException;
import br.com.logi_track.aplicacao_api.models.Cliente;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public Cliente criar(Cliente cliente) {
		validarCamposObrigatorios(cliente);
		if (clienteRepository.existsByEmail(cliente.getEmail())) {
			throw new NegocioException("Ja existe um cliente com este e-mail.", HttpStatus.CONFLICT);
		}
		LocalDateTime agora = LocalDateTime.now();
		cliente.setId(null);
		cliente.setCriadoEm(agora);
		cliente.setAtualizadoEm(agora);
		return clienteRepository.save(cliente);
	}

	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(String id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + id));
	}

	public Cliente atualizar(String id, Cliente dados) {
		Cliente existente = buscarPorId(id);
		validarCamposObrigatorios(dados);

		clienteRepository.findByEmail(dados.getEmail()).ifPresent(outro -> {
			if (!outro.getId().equals(id)) {
				throw new NegocioException("Ja existe um cliente com este e-mail.", HttpStatus.CONFLICT);
			}
		});

		existente.setNome(dados.getNome());
		existente.setTipo(dados.getTipo());
		existente.setDocumento(dados.getDocumento());
		existente.setEmail(dados.getEmail());
		existente.setTelefone(dados.getTelefone());
		existente.setEndereco(dados.getEndereco());
		existente.setAtualizadoEm(LocalDateTime.now());
		return clienteRepository.save(existente);
	}

	public void excluir(String id) {
		if (!clienteRepository.existsById(id)) {
			throw new RecursoNaoEncontradoException("Cliente nao encontrado: " + id);
		}
		clienteRepository.deleteById(id);
	}

	private void validarCamposObrigatorios(Cliente cliente) {
		if (cliente.getNome() == null || cliente.getNome().isBlank()) {
			throw new NegocioException("Nome do cliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
			throw new NegocioException("E-mail do cliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (cliente.getTipo() == null || cliente.getTipo().isBlank()) {
			throw new NegocioException("Tipo do cliente e obrigatorio (PF ou PJ).", HttpStatus.BAD_REQUEST);
		}
		if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
			throw new NegocioException("Documento do cliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
			throw new NegocioException("Telefone do cliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
		if (cliente.getEndereco() == null) {
			throw new NegocioException("Endereco do cliente e obrigatorio.", HttpStatus.BAD_REQUEST);
		}
	}
}

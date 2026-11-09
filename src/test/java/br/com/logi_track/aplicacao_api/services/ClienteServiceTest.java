package br.com.logi_track.aplicacao_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.models.Cliente;
import br.com.logi_track.aplicacao_api.models.Endereco;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

	@Mock
	private ClienteRepository clienteRepository;

	@InjectMocks
	private ClienteService clienteService;

	@Test
	void deveRecusarEmailDuplicado() {
		Cliente cliente = clienteValido();
		when(clienteRepository.existsByEmail("maria@email.com")).thenReturn(true);

		NegocioException ex = assertThrows(NegocioException.class, () -> clienteService.criar(cliente));

		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
		verify(clienteRepository, never()).save(any());
	}

	@Test
	void deveCriarClienteComEmailNovo() {
		Cliente cliente = clienteValido();
		when(clienteRepository.existsByEmail("maria@email.com")).thenReturn(false);
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

		Cliente salvo = clienteService.criar(cliente);

		assertEquals("Maria Silva", salvo.getNome());
		verify(clienteRepository).save(any(Cliente.class));
	}

	private Cliente clienteValido() {
		Cliente cliente = new Cliente();
		cliente.setNome("Maria Silva");
		cliente.setTipo("PF");
		cliente.setDocumento("123.456.789-00");
		cliente.setEmail("maria@email.com");
		cliente.setTelefone("75999990000");
		cliente.setEndereco(new Endereco());
		return cliente;
	}
}

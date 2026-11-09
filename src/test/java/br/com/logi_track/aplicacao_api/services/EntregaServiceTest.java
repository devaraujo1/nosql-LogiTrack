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
import br.com.logi_track.aplicacao_api.models.Entrega;
import br.com.logi_track.aplicacao_api.repositories.CentroDistribuicaoRepository;
import br.com.logi_track.aplicacao_api.repositories.EntregaRepository;
import br.com.logi_track.aplicacao_api.repositories.MotoristaRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.TransportadoraRepository;
import br.com.logi_track.aplicacao_api.repositories.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
class EntregaServiceTest {

	@Mock
	private EntregaRepository entregaRepository;

	@Mock
	private PedidoRepository pedidoRepository;

	@Mock
	private MotoristaRepository motoristaRepository;

	@Mock
	private VeiculoRepository veiculoRepository;

	@Mock
	private TransportadoraRepository transportadoraRepository;

	@Mock
	private CentroDistribuicaoRepository centroDistribuicaoRepository;

	@InjectMocks
	private EntregaService entregaService;

	@Test
	void deveRecusarEntregaComPedidoInexistente() {
		Entrega entrega = entregaBase();
		when(pedidoRepository.existsById("pedido1")).thenReturn(false);

		NegocioException ex = assertThrows(NegocioException.class, () -> entregaService.criar(entrega));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(entregaRepository, never()).save(any());
	}

	@Test
	void deveRecusarEntregaComMotoristaInexistente() {
		Entrega entrega = entregaBase();
		when(pedidoRepository.existsById("pedido1")).thenReturn(true);
		when(motoristaRepository.existsById("motorista1")).thenReturn(false);

		NegocioException ex = assertThrows(NegocioException.class, () -> entregaService.criar(entrega));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(entregaRepository, never()).save(any());
	}

	private Entrega entregaBase() {
		Entrega entrega = new Entrega();
		entrega.setIdPedido("pedido1");
		entrega.setIdMotorista("motorista1");
		entrega.setIdTransportadora("transp1");
		entrega.setIdVeiculo("veiculo1");
		entrega.setIdCentroDistribuicao("cd1");
		return entrega;
	}
}

package br.com.logi_track.aplicacao_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.models.Endereco;
import br.com.logi_track.aplicacao_api.models.Pagamento;
import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.models.Volume;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

	@Mock
	private PedidoRepository pedidoRepository;

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private ProdutoRepository produtoRepository;

	@InjectMocks
	private PedidoService pedidoService;

	@Test
	void deveRecusarPedidoSemVolumes() {
		Pedido pedido = new Pedido();
		pedido.setIdCliente("cliente1");
		pedido.setVolumes(Collections.emptyList());
		pedido.setOrigem(new Endereco());
		pedido.setDestino(new Endereco());
		pedido.setPagamento(pagamento());

		when(clienteRepository.existsById("cliente1")).thenReturn(true);

		NegocioException ex = assertThrows(NegocioException.class, () -> pedidoService.criar(pedido));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(pedidoRepository, never()).save(any());
	}

	@Test
	void deveCriarPedidoComVolumes() {
		Pedido pedido = new Pedido();
		pedido.setIdCliente("cliente1");
		pedido.setVolumes(List.of(volume()));
		pedido.setOrigem(new Endereco());
		pedido.setDestino(new Endereco());
		pedido.setPagamento(pagamento());

		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);
		when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

		Pedido salvo = pedidoService.criar(pedido);

		assertEquals("criado", salvo.getSituacao());
		verify(pedidoRepository).save(any(Pedido.class));
	}

	private Volume volume() {
		Volume volume = new Volume();
		volume.setCodigo("VOL-1");
		volume.setIdProduto("produto1");
		volume.setNomeProduto("Notebook");
		volume.setPesoKg(2.0);
		volume.setQuantidade(1);
		return volume;
	}

	private Pagamento pagamento() {
		Pagamento pagamento = new Pagamento();
		pagamento.setForma("pix");
		pagamento.setValor(new BigDecimal("59.90"));
		pagamento.setSituacao("pendente");
		return pagamento;
	}
}

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
import java.util.Optional;

import org.bson.types.ObjectId;
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
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.models.Volume;
import br.com.logi_track.aplicacao_api.repositories.ClienteRepository;
import br.com.logi_track.aplicacao_api.repositories.EntregaRepository;
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

	@Mock
	private EntregaRepository entregaRepository;

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
	void deveRecusarQuandoDisponivelInsuficiente() {
		Pedido pedido = pedidoComVolume(5);
		Produto produto = produto(10, 8);

		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

		NegocioException ex = assertThrows(NegocioException.class, () -> pedidoService.criar(pedido));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(pedidoRepository, never()).save(any());
	}

	@Test
	void deveRecusarCriarPedidoJaCancelado() {
		Pedido pedido = pedidoComVolume(2);
		pedido.setSituacao("cancelado");

		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);

		NegocioException ex = assertThrows(NegocioException.class, () -> pedidoService.criar(pedido));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(produtoRepository, never()).save(any());
		verify(pedidoRepository, never()).save(any());
	}

	@Test
	void deveReservarAoCriarPedido() {
		Pedido pedido = pedidoComVolume(2);
		Produto produto = produto(10, 0);

		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
		when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

		Pedido salvo = pedidoService.criar(pedido);

		assertEquals("criado", salvo.getSituacao());
		assertEquals(Integer.valueOf(2), produto.getQuantidadeReservada());
		assertEquals(Integer.valueOf(10), produto.getQuantidadeEstoque());
		verify(produtoRepository).save(produto);
	}

	@Test
	void deveLiberarReservaAoCancelar() {
		Pedido existente = pedidoComVolume(2);
		existente.setId("64f700000000000000000099");
		existente.setSituacao("criado");

		Pedido dados = pedidoComVolume(2);
		dados.setSituacao("cancelado");

		Produto produto = produto(10, 2);

		when(pedidoRepository.findById("64f700000000000000000099")).thenReturn(Optional.of(existente));
		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);
		when(entregaRepository.findByIdPedido(any(ObjectId.class))).thenReturn(Optional.empty());
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
		when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

		pedidoService.atualizar("64f700000000000000000099", dados);

		assertEquals(Integer.valueOf(0), produto.getQuantidadeReservada());
	}

	@Test
	void naoDeveZerarReservaSeAtualizacaoEstouraDisponivel() {
		Pedido existente = pedidoComVolume(2);
		existente.setId("64f700000000000000000099");
		existente.setSituacao("criado");

		Pedido dados = pedidoComVolume(12);
		dados.setSituacao("criado");

		Produto produto = produto(10, 2);

		when(pedidoRepository.findById("64f700000000000000000099")).thenReturn(Optional.of(existente));
		when(clienteRepository.existsById("cliente1")).thenReturn(true);
		when(produtoRepository.existsById("produto1")).thenReturn(true);
		when(entregaRepository.findByIdPedido(any(ObjectId.class))).thenReturn(Optional.empty());
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

		NegocioException ex = assertThrows(
				NegocioException.class,
				() -> pedidoService.atualizar("64f700000000000000000099", dados));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		assertEquals(Integer.valueOf(2), produto.getQuantidadeReservada());
		verify(pedidoRepository, never()).save(any());
	}

	@Test
	void naoDeveLiberarReservaSePedidoJaSaiuDoCd() {
		Pedido existente = pedidoComVolume(2);
		existente.setId("64f700000000000000000099");
		existente.setSituacao("em_transito");

		Produto produto = produto(8, 0);

		when(pedidoRepository.findById("64f700000000000000000099")).thenReturn(Optional.of(existente));

		pedidoService.excluir("64f700000000000000000099");

		assertEquals(Integer.valueOf(0), produto.getQuantidadeReservada());
		verify(produtoRepository, never()).save(any());
		verify(pedidoRepository).deleteById("64f700000000000000000099");
	}

	private Pedido pedidoComVolume(int quantidade) {
		Pedido pedido = new Pedido();
		pedido.setIdCliente("cliente1");
		pedido.setVolumes(List.of(volume(quantidade)));
		pedido.setOrigem(new Endereco());
		pedido.setDestino(new Endereco());
		pedido.setPagamento(pagamento());
		return pedido;
	}

	private Produto produto(int estoque, int reservada) {
		Produto produto = new Produto();
		produto.setId("produto1");
		produto.setNome("Notebook");
		produto.setQuantidadeEstoque(estoque);
		produto.setQuantidadeReservada(reservada);
		return produto;
	}

	private Volume volume(int quantidade) {
		Volume volume = new Volume();
		volume.setCodigo("VOL-1");
		volume.setIdProduto("produto1");
		volume.setNomeProduto("Notebook");
		volume.setPesoKg(2.0);
		volume.setQuantidade(quantidade);
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

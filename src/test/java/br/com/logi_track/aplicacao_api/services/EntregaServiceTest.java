package br.com.logi_track.aplicacao_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.models.Endereco;
import br.com.logi_track.aplicacao_api.models.Entrega;
import br.com.logi_track.aplicacao_api.models.Pedido;
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.models.Rota;
import br.com.logi_track.aplicacao_api.models.Volume;
import br.com.logi_track.aplicacao_api.repositories.CentroDistribuicaoRepository;
import br.com.logi_track.aplicacao_api.repositories.EntregaRepository;
import br.com.logi_track.aplicacao_api.repositories.MotoristaRepository;
import br.com.logi_track.aplicacao_api.repositories.PedidoRepository;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;
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

	@Mock
	private ProdutoRepository produtoRepository;

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
	void deveConverterReservaEmBaixaNoEmTransito() {
		Entrega existente = entregaBase();
		existente.setId("entrega1");
		existente.setSituacao("no_cd");

		Entrega dados = entregaBase();
		dados.setSituacao("em_transito");

		Pedido pedido = pedidoComVolume(2);
		Produto produto = produto(10, 2);

		when(entregaRepository.findById("entrega1")).thenReturn(Optional.of(existente));
		mockRefsOk();
		when(pedidoRepository.findById("pedido1")).thenReturn(Optional.of(pedido));
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
		when(entregaRepository.save(any(Entrega.class))).thenAnswer(inv -> inv.getArgument(0));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
		when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

		Entrega atualizada = entregaService.atualizar("entrega1", dados);

		assertEquals("em_transito", atualizada.getSituacao());
		assertEquals("em_transito", pedido.getSituacao());
		assertEquals(Integer.valueOf(8), produto.getQuantidadeEstoque());
		assertEquals(Integer.valueOf(0), produto.getQuantidadeReservada());
		verify(produtoRepository).save(produto);
		verify(pedidoRepository).save(pedido);
	}

	@Test
	void deveBaixarAoCriarJaEmTransito() {
		Entrega entrega = entregaBase();
		entrega.setSituacao("em_transito");

		Pedido pedido = pedidoComVolume(2);
		Produto produto = produto(10, 2);

		mockRefsOk();
		when(pedidoRepository.findById("pedido1")).thenReturn(Optional.of(pedido));
		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
		when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
		when(entregaRepository.save(any(Entrega.class))).thenAnswer(inv -> inv.getArgument(0));

		entregaService.criar(entrega);

		assertEquals(Integer.valueOf(8), produto.getQuantidadeEstoque());
		assertEquals(Integer.valueOf(0), produto.getQuantidadeReservada());
		assertEquals("em_transito", pedido.getSituacao());
	}

	@Test
	void naoDeveBaixarSeJaEstavaEmTransito() {
		Entrega existente = entregaBase();
		existente.setId("entrega1");
		existente.setSituacao("em_transito");

		Entrega dados = entregaBase();
		dados.setSituacao("em_transito");

		when(entregaRepository.findById("entrega1")).thenReturn(Optional.of(existente));
		mockRefsOk();
		when(entregaRepository.save(any(Entrega.class))).thenAnswer(inv -> inv.getArgument(0));

		entregaService.atualizar("entrega1", dados);

		verify(produtoRepository, never()).save(any());
		verify(pedidoRepository, never()).save(any());
	}

	@Test
	void deveRecusarExcluirEntregaJaSaiuDoCd() {
		Entrega existente = entregaBase();
		existente.setId("entrega1");
		existente.setSituacao("em_transito");

		when(entregaRepository.findById("entrega1")).thenReturn(Optional.of(existente));

		NegocioException ex = assertThrows(
				NegocioException.class,
				() -> entregaService.excluir("entrega1"));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		verify(entregaRepository, never()).deleteById(any());
	}

	private void mockRefsOk() {
		when(pedidoRepository.existsById("pedido1")).thenReturn(true);
		when(motoristaRepository.existsById("motorista1")).thenReturn(true);
		when(transportadoraRepository.existsById("transp1")).thenReturn(true);
		when(veiculoRepository.existsById("veiculo1")).thenReturn(true);
		when(centroDistribuicaoRepository.existsById("cd1")).thenReturn(true);
	}

	private Pedido pedidoComVolume(int quantidade) {
		Pedido pedido = new Pedido();
		pedido.setId("pedido1");
		pedido.setSituacao("criado");
		Volume volume = new Volume();
		volume.setIdProduto("produto1");
		volume.setQuantidade(quantidade);
		pedido.setVolumes(List.of(volume));
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

	private Entrega entregaBase() {
		Entrega entrega = new Entrega();
		entrega.setIdPedido("pedido1");
		entrega.setIdMotorista("motorista1");
		entrega.setIdTransportadora("transp1");
		entrega.setIdVeiculo("veiculo1");
		entrega.setIdCentroDistribuicao("cd1");
		entrega.setDestino(new Endereco());
		Rota rota = new Rota();
		rota.setDistanciaKm(10.0);
		rota.setDuracaoPrevistaMin(30);
		entrega.setRota(rota);
		return entrega;
	}
}

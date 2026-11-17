package br.com.logi_track.aplicacao_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

	@Mock
	private ProdutoRepository produtoRepository;

	@InjectMocks
	private ProdutoService produtoService;

	@Test
	void deveReporSomenteEstoqueFisico() {
		Produto produto = new Produto();
		produto.setId("produto1");
		produto.setNome("Notebook");
		produto.setQuantidadeEstoque(8);
		produto.setQuantidadeReservada(0);

		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto atualizado = produtoService.reporEstoque("produto1", 20);

		assertEquals(Integer.valueOf(20), atualizado.getQuantidadeEstoque());
		assertEquals(Integer.valueOf(0), atualizado.getQuantidadeReservada());
		verify(produtoRepository).save(produto);
	}

	@Test
	void deveRecusarEstoqueMenorQueReservada() {
		Produto produto = new Produto();
		produto.setId("produto1");
		produto.setQuantidadeEstoque(10);
		produto.setQuantidadeReservada(4);

		when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

		NegocioException ex = assertThrows(
				NegocioException.class,
				() -> produtoService.reporEstoque("produto1", 2));

		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}
}

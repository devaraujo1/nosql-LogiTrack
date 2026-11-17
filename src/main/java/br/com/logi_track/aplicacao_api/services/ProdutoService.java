package br.com.logi_track.aplicacao_api.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.logi_track.aplicacao_api.exception.NegocioException;
import br.com.logi_track.aplicacao_api.exception.RecursoNaoEncontradoException;
import br.com.logi_track.aplicacao_api.models.Produto;
import br.com.logi_track.aplicacao_api.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public List<Produto> listarTodos() {
		return produtoRepository.findAll();
	}

	public Produto buscarPorId(String id) {
		return produtoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado: " + id));
	}

	public Produto reporEstoque(String id, Integer quantidadeEstoque) {
		if (quantidadeEstoque == null || quantidadeEstoque < 0) {
			throw new NegocioException("quantidadeEstoque invalida.", HttpStatus.BAD_REQUEST);
		}
		Produto produto = buscarPorId(id);
		int reservada = produto.getQuantidadeReservada() == null ? 0 : produto.getQuantidadeReservada();
		if (quantidadeEstoque < reservada) {
			throw new NegocioException(
					"quantidadeEstoque nao pode ser menor que a quantidade reservada.",
					HttpStatus.BAD_REQUEST);
		}
		produto.setQuantidadeEstoque(quantidadeEstoque);
		return produtoRepository.save(produto);
	}
}

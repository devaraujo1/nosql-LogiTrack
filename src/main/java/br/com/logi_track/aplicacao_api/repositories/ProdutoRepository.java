package br.com.logi_track.aplicacao_api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String> {

	Optional<Produto> findByCodigo(String codigo);
}

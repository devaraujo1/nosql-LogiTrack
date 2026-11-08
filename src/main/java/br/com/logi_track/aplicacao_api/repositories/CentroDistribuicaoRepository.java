package br.com.logi_track.aplicacao_api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.CentroDistribuicao;

public interface CentroDistribuicaoRepository extends MongoRepository<CentroDistribuicao, String> {
}

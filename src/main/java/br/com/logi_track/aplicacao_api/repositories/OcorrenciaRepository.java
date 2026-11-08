package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Ocorrencia;

public interface OcorrenciaRepository extends MongoRepository<Ocorrencia, String> {

	List<Ocorrencia> findByIdEntrega(String idEntrega);

	List<Ocorrencia> findByIdCentroDistribuicao(String idCentroDistribuicao);
}

package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Motorista;

public interface MotoristaRepository extends MongoRepository<Motorista, String> {

	List<Motorista> findByIdTransportadora(String idTransportadora);

	List<Motorista> findBySituacao(String situacao);
}

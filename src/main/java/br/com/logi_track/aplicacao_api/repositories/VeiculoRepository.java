package br.com.logi_track.aplicacao_api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Veiculo;

public interface VeiculoRepository extends MongoRepository<Veiculo, String> {

	Optional<Veiculo> findByPlaca(String placa);
}

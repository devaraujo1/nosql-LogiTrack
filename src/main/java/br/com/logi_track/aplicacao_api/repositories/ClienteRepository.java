package br.com.logi_track.aplicacao_api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

	Optional<Cliente> findByEmail(String email);

	boolean existsByEmail(String email);
}

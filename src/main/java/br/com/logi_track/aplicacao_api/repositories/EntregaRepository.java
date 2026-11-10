package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.logi_track.aplicacao_api.models.Entrega;

public interface EntregaRepository extends MongoRepository<Entrega, String> {

	@Query("{ 'idPedido': ?0 }")
	Optional<Entrega> findByIdPedido(ObjectId idPedido);

	@Query("{ 'idMotorista': ?0 }")
	List<Entrega> findByIdMotorista(ObjectId idMotorista);

	List<Entrega> findByAtrasadoTrue();

	List<Entrega> findByDestinoEstado(String estado);
}

package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.logi_track.aplicacao_api.models.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

	@Query("{ 'idCliente': ?0 }")
	List<Pedido> findByIdCliente(ObjectId idCliente);

	List<Pedido> findBySituacao(String situacao);
}

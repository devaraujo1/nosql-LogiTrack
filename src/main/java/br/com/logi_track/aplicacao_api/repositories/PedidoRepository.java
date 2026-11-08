package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

	List<Pedido> findByIdCliente(String idCliente);

	List<Pedido> findBySituacao(String situacao);
}

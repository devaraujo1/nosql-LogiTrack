package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Entrega;

public interface EntregaRepository extends MongoRepository<Entrega, String> {

	Optional<Entrega> findByIdPedido(String idPedido);

	List<Entrega> findByIdMotorista(String idMotorista);

	List<Entrega> findByAtrasadoTrue();

	List<Entrega> findByDestinoEstado(String estado);
}

package br.com.logi_track.aplicacao_api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.logi_track.aplicacao_api.models.Rastreamento;

public interface RastreamentoRepository extends MongoRepository<Rastreamento, String> {

	List<Rastreamento> findByIdEntregaOrderByRegistradoEmAsc(String idEntrega);
}

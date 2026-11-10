package br.com.logi_track.aplicacao_api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "veiculos")
public class Veiculo {

	@Id
	private String id;

	@Field("idTransportadora")
	private String idTransportadora;

	@Field("placa")
	private String placa;

	@Field("tipo")
	private String tipo;

	@Field("capacidadeKg")
	private Integer capacidadeKg;

	@Field("situacao")
	private String situacao;

	public Veiculo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdTransportadora() {
		return idTransportadora;
	}

	public void setIdTransportadora(String idTransportadora) {
		this.idTransportadora = idTransportadora;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getCapacidadeKg() {
		return capacidadeKg;
	}

	public void setCapacidadeKg(Integer capacidadeKg) {
		this.capacidadeKg = capacidadeKg;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}

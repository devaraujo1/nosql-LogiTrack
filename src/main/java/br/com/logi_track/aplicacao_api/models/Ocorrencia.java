package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ocorrencias")
public class Ocorrencia {

	@Id
	private String id;

	@Field("idEntrega")
	private String idEntrega;

	@Field("idCentroDistribuicao")
	private String idCentroDistribuicao;

	@Field("idMotorista")
	private String idMotorista;

	@Field("tipo")
	private String tipo;

	@Field("descricao")
	private String descricao;

	@Field("dataHora")
	private LocalDateTime dataHora;

	@Field("cidade")
	private String cidade;

	@Field("minutosAtraso")
	private Integer minutosAtraso;

	@Field("gravidade")
	private String gravidade;

	public Ocorrencia() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdEntrega() {
		return idEntrega;
	}

	public void setIdEntrega(String idEntrega) {
		this.idEntrega = idEntrega;
	}

	public String getIdCentroDistribuicao() {
		return idCentroDistribuicao;
	}

	public void setIdCentroDistribuicao(String idCentroDistribuicao) {
		this.idCentroDistribuicao = idCentroDistribuicao;
	}

	public String getIdMotorista() {
		return idMotorista;
	}

	public void setIdMotorista(String idMotorista) {
		this.idMotorista = idMotorista;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Integer getMinutosAtraso() {
		return minutosAtraso;
	}

	public void setMinutosAtraso(Integer minutosAtraso) {
		this.minutosAtraso = minutosAtraso;
	}

	public String getGravidade() {
		return gravidade;
	}

	public void setGravidade(String gravidade) {
		this.gravidade = gravidade;
	}
}

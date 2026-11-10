package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "rastreamentos")
public class Rastreamento {

	@Id
	private String id;

	@Field(name = "idEntrega", targetType = FieldType.OBJECT_ID)
	private String idEntrega;

	@Field("latitude")
	private Double latitude;

	@Field("longitude")
	private Double longitude;

	@Field("cidade")
	private String cidade;

	@Field("estado")
	private String estado;

	@Field("registradoEm")
	private LocalDateTime registradoEm;

	public Rastreamento() {
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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getRegistradoEm() {
		return registradoEm;
	}

	public void setRegistradoEm(LocalDateTime registradoEm) {
		this.registradoEm = registradoEm;
	}
}

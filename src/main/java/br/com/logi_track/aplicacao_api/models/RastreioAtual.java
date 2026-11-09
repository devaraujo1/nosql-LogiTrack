package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

public class RastreioAtual {

	private Double latitude;
	private Double longitude;
	private String cidade;
	private String estado;
	private LocalDateTime registradoEm;

	public RastreioAtual() {
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

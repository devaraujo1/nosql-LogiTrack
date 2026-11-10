package br.com.logi_track.aplicacao_api.models;

public class Rota {

	private Double distanciaKm;
	private Integer duracaoPrevistaMin;
	private Integer duracaoRealMin;

	public Rota() {
	}

	public Double getDistanciaKm() {
		return distanciaKm;
	}

	public void setDistanciaKm(Double distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public Integer getDuracaoPrevistaMin() {
		return duracaoPrevistaMin;
	}

	public void setDuracaoPrevistaMin(Integer duracaoPrevistaMin) {
		this.duracaoPrevistaMin = duracaoPrevistaMin;
	}

	public Integer getDuracaoRealMin() {
		return duracaoRealMin;
	}

	public void setDuracaoRealMin(Integer duracaoRealMin) {
		this.duracaoRealMin = duracaoRealMin;
	}
}

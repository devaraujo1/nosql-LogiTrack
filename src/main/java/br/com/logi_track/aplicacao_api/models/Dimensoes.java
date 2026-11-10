package br.com.logi_track.aplicacao_api.models;

import jakarta.validation.constraints.NotNull;

public class Dimensoes {

	@NotNull
	private Double comprimentoCm;

	@NotNull
	private Double larguraCm;

	@NotNull
	private Double alturaCm;

	public Dimensoes() {
	}

	public Double getComprimentoCm() {
		return comprimentoCm;
	}

	public void setComprimentoCm(Double comprimentoCm) {
		this.comprimentoCm = comprimentoCm;
	}

	public Double getLarguraCm() {
		return larguraCm;
	}

	public void setLarguraCm(Double larguraCm) {
		this.larguraCm = larguraCm;
	}

	public Double getAlturaCm() {
		return alturaCm;
	}

	public void setAlturaCm(Double alturaCm) {
		this.alturaCm = alturaCm;
	}
}

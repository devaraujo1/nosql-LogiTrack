package br.com.logi_track.aplicacao_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Pagamento {

	@NotBlank
	private String forma;

	@NotNull
	private BigDecimal valor;

	@NotBlank
	private String situacao;

	private LocalDateTime pagoEm;

	public Pagamento() {
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public LocalDateTime getPagoEm() {
		return pagoEm;
	}

	public void setPagoEm(LocalDateTime pagoEm) {
		this.pagoEm = pagoEm;
	}
}

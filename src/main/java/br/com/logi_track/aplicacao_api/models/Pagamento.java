package br.com.logi_track.aplicacao_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

	private String forma;
	private BigDecimal valor;
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

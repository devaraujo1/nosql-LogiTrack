package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

public class HorariosEtapas {

	private LocalDateTime recebidoNoCdEm;
	private LocalDateTime saiuEm;
	private LocalDateTime chegouEm;
	private LocalDateTime entregueEm;

	public HorariosEtapas() {
	}

	public LocalDateTime getRecebidoNoCdEm() {
		return recebidoNoCdEm;
	}

	public void setRecebidoNoCdEm(LocalDateTime recebidoNoCdEm) {
		this.recebidoNoCdEm = recebidoNoCdEm;
	}

	public LocalDateTime getSaiuEm() {
		return saiuEm;
	}

	public void setSaiuEm(LocalDateTime saiuEm) {
		this.saiuEm = saiuEm;
	}

	public LocalDateTime getChegouEm() {
		return chegouEm;
	}

	public void setChegouEm(LocalDateTime chegouEm) {
		this.chegouEm = chegouEm;
	}

	public LocalDateTime getEntregueEm() {
		return entregueEm;
	}

	public void setEntregueEm(LocalDateTime entregueEm) {
		this.entregueEm = entregueEm;
	}
}

package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "entregas")
public class Entrega {

	@Id
	private String id;

	@Field("idPedido")
	private String idPedido;

	@Field("idTransportadora")
	private String idTransportadora;

	@Field("idMotorista")
	private String idMotorista;

	@Field("idVeiculo")
	private String idVeiculo;

	@Field("idCentroDistribuicao")
	private String idCentroDistribuicao;

	@Field("situacao")
	private String situacao;

	@Field("atrasado")
	private Boolean atrasado;

	@Field("prometidoPara")
	private LocalDateTime prometidoPara;

	@Field("destino")
	private Endereco destino;

	@Field("rota")
	private Rota rota;

	@Field("horariosEtapas")
	private HorariosEtapas horariosEtapas;

	@Field("rastreioAtual")
	private RastreioAtual rastreioAtual;

	@Field("criadoEm")
	private LocalDateTime criadoEm;

	@Field("atualizadoEm")
	private LocalDateTime atualizadoEm;

	public Entrega() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(String idPedido) {
		this.idPedido = idPedido;
	}

	public String getIdTransportadora() {
		return idTransportadora;
	}

	public void setIdTransportadora(String idTransportadora) {
		this.idTransportadora = idTransportadora;
	}

	public String getIdMotorista() {
		return idMotorista;
	}

	public void setIdMotorista(String idMotorista) {
		this.idMotorista = idMotorista;
	}

	public String getIdVeiculo() {
		return idVeiculo;
	}

	public void setIdVeiculo(String idVeiculo) {
		this.idVeiculo = idVeiculo;
	}

	public String getIdCentroDistribuicao() {
		return idCentroDistribuicao;
	}

	public void setIdCentroDistribuicao(String idCentroDistribuicao) {
		this.idCentroDistribuicao = idCentroDistribuicao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getAtrasado() {
		return atrasado;
	}

	public void setAtrasado(Boolean atrasado) {
		this.atrasado = atrasado;
	}

	public LocalDateTime getPrometidoPara() {
		return prometidoPara;
	}

	public void setPrometidoPara(LocalDateTime prometidoPara) {
		this.prometidoPara = prometidoPara;
	}

	public Endereco getDestino() {
		return destino;
	}

	public void setDestino(Endereco destino) {
		this.destino = destino;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public HorariosEtapas getHorariosEtapas() {
		return horariosEtapas;
	}

	public void setHorariosEtapas(HorariosEtapas horariosEtapas) {
		this.horariosEtapas = horariosEtapas;
	}

	public RastreioAtual getRastreioAtual() {
		return rastreioAtual;
	}

	public void setRastreioAtual(RastreioAtual rastreioAtual) {
		this.rastreioAtual = rastreioAtual;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}

	public void setAtualizadoEm(LocalDateTime atualizadoEm) {
		this.atualizadoEm = atualizadoEm;
	}
}

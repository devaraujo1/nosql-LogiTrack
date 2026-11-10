package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "entregas")
public class Entrega {

	@Id
	private String id;

	@DocumentReference
	@Field(name = "idPedido", targetType = FieldType.OBJECT_ID)
	@NotNull
	@JsonIgnore
	private Pedido pedido;

	@Field(name = "idTransportadora", targetType = FieldType.OBJECT_ID)
	@NotBlank
	private String idTransportadora;

	@DocumentReference
	@Field(name = "idMotorista", targetType = FieldType.OBJECT_ID)
	@NotNull
	@JsonIgnore
	private Motorista motorista;

	@Field(name = "idVeiculo", targetType = FieldType.OBJECT_ID)
	@NotBlank
	private String idVeiculo;

	@Field(name = "idCentroDistribuicao", targetType = FieldType.OBJECT_ID)
	@NotBlank
	private String idCentroDistribuicao;

	@Field("situacao")
	private String situacao;

	@Field("atrasado")
	private Boolean atrasado;

	@Field("prometidoPara")
	private LocalDateTime prometidoPara;

	@Field("destino")
	@NotNull
	@Valid
	private Endereco destino;

	@Field("rota")
	@NotNull
	@Valid
	private Rota rota;

	@Field("horariosEtapas")
	@Valid
	private HorariosEtapas horariosEtapas;

	@Field("rastreioAtual")
	@Valid
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

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@JsonProperty("idPedido")
	public String getIdPedido() {
		return pedido != null ? pedido.getId() : null;
	}

	@JsonProperty("idPedido")
	public void setIdPedido(String idPedido) {
		if (idPedido == null || idPedido.isBlank()) {
			this.pedido = null;
			return;
		}
		Pedido ref = new Pedido();
		ref.setId(idPedido);
		this.pedido = ref;
	}

	public String getIdTransportadora() {
		return idTransportadora;
	}

	public void setIdTransportadora(String idTransportadora) {
		this.idTransportadora = idTransportadora;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	@JsonProperty("idMotorista")
	public String getIdMotorista() {
		return motorista != null ? motorista.getId() : null;
	}

	@JsonProperty("idMotorista")
	public void setIdMotorista(String idMotorista) {
		if (idMotorista == null || idMotorista.isBlank()) {
			this.motorista = null;
			return;
		}
		Motorista ref = new Motorista();
		ref.setId(idMotorista);
		this.motorista = ref;
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

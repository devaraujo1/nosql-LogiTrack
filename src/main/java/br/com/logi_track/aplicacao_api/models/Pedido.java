package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Document(collection = "pedidos")
public class Pedido {

	@Id
	private String id;

	// referencia para o cliente (DocumentReference)
	@DocumentReference
	@Field(name = "idCliente", targetType = FieldType.OBJECT_ID)
	@NotNull
	@JsonIgnore
	private Cliente cliente;

	@Field("situacao")
	private String situacao;

	@Field("origem")
	@NotNull
	@Valid
	private Endereco origem;

	@Field("destino")
	@NotNull
	@Valid
	private Endereco destino;

	@Field("volumes")
	@NotEmpty
	@Valid
	private List<Volume> volumes;

	@Field("pagamento")
	@NotNull
	@Valid
	private Pagamento pagamento;

	@Field("criadoEm")
	private LocalDateTime criadoEm;

	@Field("atualizadoEm")
	private LocalDateTime atualizadoEm;

	public Pedido() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@JsonProperty("idCliente")
	public String getIdCliente() {
		return cliente != null ? cliente.getId() : null;
	}

	@JsonProperty("idCliente")
	public void setIdCliente(String idCliente) {
		if (idCliente == null || idCliente.isBlank()) {
			this.cliente = null;
			return;
		}
		Cliente ref = new Cliente();
		ref.setId(idCliente);
		this.cliente = ref;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Endereco getOrigem() {
		return origem;
	}

	public void setOrigem(Endereco origem) {
		this.origem = origem;
	}

	public Endereco getDestino() {
		return destino;
	}

	public void setDestino(Endereco destino) {
		this.destino = destino;
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
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

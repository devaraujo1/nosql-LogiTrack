package br.com.logi_track.aplicacao_api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "motoristas")
public class Motorista {

	@Id
	private String id;

	@Field(name = "idTransportadora", targetType = FieldType.OBJECT_ID)
	private String idTransportadora;

	@Field("nome")
	private String nome;

	@Field("documento")
	private String documento;

	@Field("numeroCnh")
	private String numeroCnh;

	@Field("telefone")
	private String telefone;

	@Field("situacao")
	private String situacao;

	public Motorista() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdTransportadora() {
		return idTransportadora;
	}

	public void setIdTransportadora(String idTransportadora) {
		this.idTransportadora = idTransportadora;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNumeroCnh() {
		return numeroCnh;
	}

	public void setNumeroCnh(String numeroCnh) {
		this.numeroCnh = numeroCnh;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}

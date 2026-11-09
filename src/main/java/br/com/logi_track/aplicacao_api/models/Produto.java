package br.com.logi_track.aplicacao_api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "produtos")
public class Produto {

	@Id
	private String id;

	@Field("nome")
	private String nome;

	@Field("codigo")
	private String codigo;

	@Field("categoria")
	private String categoria;

	@Field("pesoKg")
	private Double pesoKg;

	@Field("dimensoes")
	private Dimensoes dimensoes;

	@Field("fragil")
	private Boolean fragil;

	@Field("ativo")
	private Boolean ativo;

	public Produto() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Double getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(Double pesoKg) {
		this.pesoKg = pesoKg;
	}

	public Dimensoes getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(Dimensoes dimensoes) {
		this.dimensoes = dimensoes;
	}

	public Boolean getFragil() {
		return fragil;
	}

	public void setFragil(Boolean fragil) {
		this.fragil = fragil;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}

package br.com.logi_track.aplicacao_api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@Field("quantidadeEstoque")
	private Integer quantidadeEstoque;

	@Field("quantidadeReservada")
	private Integer quantidadeReservada;

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

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Integer getQuantidadeReservada() {
		return quantidadeReservada;
	}

	public void setQuantidadeReservada(Integer quantidadeReservada) {
		this.quantidadeReservada = quantidadeReservada;
	}

	@JsonProperty("quantidadeDisponivel")
	public Integer getQuantidadeDisponivel() {
		int estoque = quantidadeEstoque == null ? 0 : quantidadeEstoque;
		int reservada = quantidadeReservada == null ? 0 : quantidadeReservada;
		return estoque - reservada;
	}
}

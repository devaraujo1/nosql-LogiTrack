package br.com.logi_track.aplicacao_api.models;

public class Volume {

	private String codigo;
	private String idProduto;
	private String nomeProduto;
	private Double pesoKg;
	private Dimensoes dimensoes;
	private Integer quantidade;

	public Volume() {
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}

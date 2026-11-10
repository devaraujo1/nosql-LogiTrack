package br.com.logi_track.aplicacao_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "clientes")
public class Cliente {

	@Id
	private String id;

	@Field("nome")
	@NotBlank
	private String nome;

	@Field("tipo")
	@NotBlank
	private String tipo;

	@Field("documento")
	@NotBlank
	private String documento;

	@Field("email")
	@NotBlank
	@Email
	private String email;

	@Field("telefone")
	@NotBlank
	private String telefone;

	@Field("endereco")
	@NotNull
	@Valid
	private Endereco endereco;

	@Field("criadoEm")
	private LocalDateTime criadoEm;

	@Field("atualizadoEm")
	private LocalDateTime atualizadoEm;

	public Cliente() {
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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

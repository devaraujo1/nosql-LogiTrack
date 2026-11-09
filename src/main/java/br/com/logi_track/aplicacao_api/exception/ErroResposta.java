package br.com.logi_track.aplicacao_api.exception;

import java.time.LocalDateTime;

public class ErroResposta {

	private LocalDateTime timestamp;
	private Integer status;
	private String erro;
	private String mensagem;

	public ErroResposta() {
	}

	public ErroResposta(LocalDateTime timestamp, Integer status, String erro, String mensagem) {
		this.timestamp = timestamp;
		this.status = status;
		this.erro = erro;
		this.mensagem = mensagem;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}

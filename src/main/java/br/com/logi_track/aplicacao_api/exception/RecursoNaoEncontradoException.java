package br.com.logi_track.aplicacao_api.exception;

import org.springframework.http.HttpStatus;

public class RecursoNaoEncontradoException extends NegocioException {

	public RecursoNaoEncontradoException(String mensagem) {
		super(mensagem, HttpStatus.NOT_FOUND);
	}
}

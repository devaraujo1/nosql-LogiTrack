package br.com.logi_track.aplicacao_api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<ErroResposta> handleNegocio(NegocioException ex) {
		HttpStatus status = ex.getStatus();
		ErroResposta corpo = new ErroResposta(
				LocalDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				ex.getMessage());
		return ResponseEntity.status(status).body(corpo);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResposta> handleValidacao(MethodArgumentNotValidException ex) {
		String mensagem = ex.getBindingResult().getFieldErrors().stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.findFirst()
				.orElse("Dados invalidos");
		ErroResposta corpo = new ErroResposta(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				mensagem);
		return ResponseEntity.badRequest().body(corpo);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResposta> handleGeral(Exception ex) {
		ErroResposta corpo = new ErroResposta(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpo);
	}
}

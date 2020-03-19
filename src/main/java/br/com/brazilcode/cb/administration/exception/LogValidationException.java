package br.com.brazilcode.cb.administration.exception;

import br.com.brazilcode.cb.libs.model.Log;

/**
 * Classe responsável por configurar uma exceção personalizada para validação de {@link Log}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:48:37
 * @version 1.0
 */
public class LogValidationException extends Exception {

	private static final long serialVersionUID = -1456098986528080299L;

	public LogValidationException() {
		super();
	}

	public LogValidationException(String message) {
		super(message);
	}

	public LogValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogValidationException(Throwable cause) {
		super(cause);
	}

}

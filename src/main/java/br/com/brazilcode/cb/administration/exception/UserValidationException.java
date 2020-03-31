package br.com.brazilcode.cb.administration.exception;

import br.com.brazilcode.cb.libs.model.User;

/**
 * Classe responsável por configurar uma exceção personalizada para validação de campos obrigatórios para {@link User}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de mar de 2020 16:51:02
 * @version 1.0
 */
public class UserValidationException extends Exception {

	private static final long serialVersionUID = -6033272630611017800L;

	public UserValidationException() {
		super();
	}

	public UserValidationException(String message) {
		super(message);
	}

	public UserValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserValidationException(Throwable cause) {
		super(cause);
	}

}

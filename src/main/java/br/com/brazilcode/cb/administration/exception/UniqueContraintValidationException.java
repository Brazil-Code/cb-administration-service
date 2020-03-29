package br.com.brazilcode.cb.administration.exception;

/**
 * Classe responsável por configurar uma exceção personalizada para validação de campos unique.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de mar de 2020 16:28:31
 * @version 1.0
 */
public class UniqueContraintValidationException extends Exception {

	private static final long serialVersionUID = -5473340082425474058L;

	public UniqueContraintValidationException() {
		super();
	}

	public UniqueContraintValidationException(String message) {
		super(message);
	}

	public UniqueContraintValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniqueContraintValidationException(Throwable cause) {
		super(cause);
	}

}

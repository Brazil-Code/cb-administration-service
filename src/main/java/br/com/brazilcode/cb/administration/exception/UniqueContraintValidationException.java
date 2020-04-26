package br.com.brazilcode.cb.administration.exception;

import lombok.NoArgsConstructor;

/**
 * Class responsible for configuring a custom exception for validating unique fields.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:13:09 PM
 * @version 1.1
 */
@NoArgsConstructor
public class UniqueContraintValidationException extends Exception {

	private static final long serialVersionUID = -5473340082425474058L;

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

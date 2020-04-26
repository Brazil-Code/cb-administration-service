package br.com.brazilcode.cb.administration.exception;

import br.com.brazilcode.cb.libs.model.User;
import lombok.NoArgsConstructor;

/**
 * Class responsible for setting up a custom exception for validating mandatory fields for {@link User}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:13:31 PM
 * @version 1.1
 */
@NoArgsConstructor
public class UserValidationException extends Exception {

	private static final long serialVersionUID = -6033272630611017800L;

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

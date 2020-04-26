package br.com.brazilcode.cb.administration.exception;

import br.com.brazilcode.cb.libs.model.Log;
import lombok.NoArgsConstructor;

/**
 * Class responsible for configuring a custom exception for {@link Log} validation.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:12:39 PM
 * @version 1.1
 */
@NoArgsConstructor
public class LogValidationException extends Exception {

	private static final long serialVersionUID = -1456098986528080299L;

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

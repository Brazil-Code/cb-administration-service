package br.com.brazilcode.cb.administration.constants;

/**
 * Class responsible for storing application security information.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:05:08 PM
 * @version 1.0
 */
public class SecurityConstants {

	public static final String SECRET = "296372BA7674D7A8D4B4AFAF98823F96FAF0BC3BD1EE4EC88D0555264A335B5D";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	public static final long EXPIRATION_TIME = 86400000L;

}

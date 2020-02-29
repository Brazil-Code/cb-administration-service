package br.com.brazilcode.cb.administration.constants;

/**
 * Classe responsável por armazenar as informações de segurança da aplicação.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de fev de 2020 17:29:11
 * @version 1.0
 */
public class SecurityConstants {

	public static final String SECRET = "DevDojoFoda";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	public static final long EXPIRATION_TIME = 86400000L;

}

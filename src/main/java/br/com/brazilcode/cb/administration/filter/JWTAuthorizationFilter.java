package br.com.brazilcode.cb.administration.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.brazilcode.cb.administration.service.CustomUserDetailService;
import io.jsonwebtoken.Jwts;

import static br.com.brazilcode.cb.administration.constants.SecurityConstants.*;

/**
 * Classe responsável por configurar e aplicar o filtro de autorização JWT.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de fev de 2020 17:35:17
 * @version 1.0
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final CustomUserDetailService customUserDetailService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
		super(authenticationManager);
		this.customUserDetailService = customUserDetailService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}

	/**
	 * Método responsável por buscar o token de autenticação do usuário.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param request
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token == null)
			return null;
		String username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
				.getSubject();
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		return username != null ? new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) : null;
	}

}
package br.com.brazilcode.cb.administration.dto;

import br.com.brazilcode.cb.libs.model.Log;

/**
 * Classe de Data Transfer Object para {@link Log}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:53:45
 * @version 1.0
 */
public class LogDTO {

	private Long user;
	private String description;
	private String timestamp;

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}

package br.com.brazilcode.cb.administration.dto;

import br.com.brazilcode.cb.libs.model.User;

/**
 * Classe responsável por agrupar informações sobre {@link User}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de mar de 2020 14:07:35
 * @version 1.0
 */
public class UserDTO {

	private String firstName;
	private String lastName;
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}

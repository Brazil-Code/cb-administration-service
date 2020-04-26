package br.com.brazilcode.cb.administration.dto;

import br.com.brazilcode.cb.libs.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class responsible for grouping information about {@link User}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:03:38 PM
 * @version 2.0
 */
@Getter
@Setter
@ToString
public class UserDTO {

	@ApiModelProperty(value = "User's first name")
	private String firstName;

	@ApiModelProperty(value = "User's last name")
	private String lastName;

	@ApiModelProperty(value = "User's e-mail")
	private String email;

}

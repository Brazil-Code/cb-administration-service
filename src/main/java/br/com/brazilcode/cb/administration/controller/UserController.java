package br.com.brazilcode.cb.administration.controller;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.administration.service.UserService;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;

/**
 * Classe responsável por expor as APIs para Users.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 17 de mar de 2020 22:19:49
 * @version 1.0
 */
@RestController
@RequestMapping("users")
public class UserController implements Serializable {

	private static final long serialVersionUID = 34855461917907245L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Método responsável por verificar se o ID do usuário informado está cadastrado no banco de dados.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	@GetMapping(path = "{id}")
	public ResponseEntity<?> verifyIfExist(@PathVariable("id") final Long id) {
		final String method = "[ UserController.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");
		LOGGER.debug(method + "received ID: " + id);

		try {
			LOGGER.debug(method + "Calling userService.verifyIfExists");
			User user = this.userService.verifyIfExists(id);

			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}

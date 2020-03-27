package br.com.brazilcode.cb.administration.controller;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.administration.service.UserService;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.model.api.response.InternalServerErrorResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe responsável por expor as APIs para Users.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 17 de mar de 2020 22:19:49
 * @version 1.0
 */
@RestController
@RequestMapping("users")
@Api(value = "REST API for Users")
@CrossOrigin(origins = "*")
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
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a User"),
			@ApiResponse(code = 404, message = "User not found for the given ID"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Search for a User in database with the given ID")
	public ResponseEntity<?> verifyIfExist(@PathVariable("id") final Long id) {
		final String method = "[ UserController.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");
		LOGGER.debug(method + "received ID: " + id);

		try {
			LOGGER.debug(method + "Calling userService.verifyIfExists");
			User user = this.userService.verifyIfExists(id);

			// TODO: Register Log

			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	@GetMapping
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a User"),
			@ApiResponse(code = 404, message = "User not found for the given username"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Search for a User in database with the given username")
	public ResponseEntity<?> findByUsername(@RequestParam(name = "username", required = true) final String username) {
		final String method = "[ UserController.findByUsername ] - ";
		LOGGER.debug(method + "BEGIN");
		LOGGER.debug(method + "Received Username: " + username);

		try {
			LOGGER.debug(method + "Calling userService.findByUsername");
			User user = this.userService.findByUsername(username);

			// TODO: Register Log
			
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			final String errorMessage = e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}

package br.com.brazilcode.cb.administration.controller;

import static br.com.brazilcode.cb.libs.constants.ApiResponseConstants.VALIDATION_ERROR_RESPONSE;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.administration.dto.UserDTO;
import br.com.brazilcode.cb.administration.exception.UniqueContraintValidationException;
import br.com.brazilcode.cb.administration.exception.UserValidationException;
import br.com.brazilcode.cb.administration.service.LogService;
import br.com.brazilcode.cb.administration.service.UserService;
import br.com.brazilcode.cb.libs.dto.LogDTO;
import br.com.brazilcode.cb.libs.enumerator.LogActivityTypeEnum;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.model.api.response.BadRequestResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.InternalServerErrorResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.UpdatedResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe responsável por expor as APIs para Users.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 17 de mar de 2020 22:19:49
 * @version 1.2
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

	@Autowired
	private LogService logService;

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
			@ApiResponse(code = 400, message = "User not found for the given ID"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Search for a User in database with the given ID")
	public ResponseEntity<?> verifyIfExist(HttpServletRequest requestContext, @PathVariable("id") final Long id) {
		final String method = "[ UserController.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");
		LOGGER.debug(method + "received ID: " + id);

		try {
			LOGGER.debug(method + "Calling userService.verifyIfExists");
			User user = this.userService.verifyIfExists(id);

			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por buscar um {@link User} no banco de dados, filtrando pelo username informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param username
	 * @return
	 */
	@GetMapping
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a User"),
			@ApiResponse(code = 400, message = "User not found for the given username"),
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

			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por atualizar as informações de um {@link User} pelo ID e dados informados.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @param userDTO
	 * @param request
	 * @return
	 */
	@PutMapping(path = "{id}")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "User updated successfully"),
			@ApiResponse(code = 400, message = "User not found for the given username"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Update user")
	public ResponseEntity<?> update(@PathVariable("id") final Long id, @Valid @RequestBody final UserDTO userDTO, 
			HttpServletRequest request) {
		final String method = "[ UserController.update ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling userService.update");
			this.userService.update(id, userDTO);

			LOGGER.debug(method + "Registering activity log");
			final String description = LogActivityTypeEnum.UPDATE.getDescription() + " as informações do seu perfil";
			LogDTO logDTO = new LogDTO(id, description);
			this.logService.save(logDTO, request);

			return new ResponseEntity<>(new UpdatedResponseObject(id), HttpStatus.OK);
		} catch (UserValidationException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (UniqueContraintValidationException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}

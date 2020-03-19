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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.administration.exception.LogValidationException;
import br.com.brazilcode.cb.administration.service.LogService;
import br.com.brazilcode.cb.libs.dto.LogDTO;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.Log;
import br.com.brazilcode.cb.libs.model.api.response.BadRequestResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.CreatedResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.InternalServerErrorResponseObject;

/**
 * Classe responsável por expor as APIs para Logs.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:10:05
 * @version 1.1
 */
@RestController
@RequestMapping("logs")
public class LogController implements Serializable {

	private static final long serialVersionUID = 5226137304754768180L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

	@Autowired
	private LogService logService;

	/**
	 * Método responsável por buscar um {@link Log}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	@GetMapping(path = "{id}")
	public ResponseEntity<?> findById(@PathVariable("id") final Long id) {
		final String method = "[ LogController.findById ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling logService.verifyIfExists - ID: " + id);
			final Log log = logService.verifyIfExists(id);

			return new ResponseEntity<Log>(log, HttpStatus.OK);
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
	 * Método responsável por salvar um Log.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param requestContext
	 * @param logAuditoria
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> save(HttpServletRequest requestContext, @Valid @RequestBody final LogDTO logDTO) {
		final String method = "[ LogController.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling logService.save... sending: " + logDTO.toString());
			Log log = this.logService.save(logDTO, requestContext);

			return new ResponseEntity<>(new CreatedResponseObject(log.getId()), HttpStatus.CREATED);
		} catch (LogValidationException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.debug(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.debug(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}

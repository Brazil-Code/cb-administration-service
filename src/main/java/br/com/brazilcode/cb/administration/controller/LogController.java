package br.com.brazilcode.cb.administration.controller;

import static br.com.brazilcode.cb.libs.constants.ApiResponseConstants.VALIDATION_ERROR_RESPONSE;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for exposing the APIs to Logs.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:07:08 PM
 * @version 2.0
 */
@RestController
@RequestMapping("logs")
@Api(value = "REST API for Logs")
@CrossOrigin(origins = "*")
@Slf4j
public class LogController implements Serializable {

	private static final long serialVersionUID = 5226137304754768180L;

	@Autowired
	private LogService logService;

	/**
	 * Method responsible for searching for a {@link Log}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	@GetMapping(path = "{id}")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a Log"),
			@ApiResponse(code = 400, message = "Log not found for the given ID"),
			@ApiResponse(code = 500, message = "Unexpected internal error")
		})
	@ApiOperation(value = "Search for a Log in database with the given ID")
	public ResponseEntity<?> findById(@PathVariable("id") final Long id) {
		final String method = "[ LogController.findById ] - ";
		log.info(method + "BEGIN");

		try {
			log.info(method + "Calling logService.verifyIfExists - ID: " + id);
			final Log log = logService.verifyIfExists(id);

			return new ResponseEntity<Log>(log, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			log.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info(method + "END");
		}
	}

	/**
	 * Method responsible for saving a {@link Log}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param requestContext
	 * @param logDTO
	 * @return
	 */
	@PostMapping
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Return the ID of the created Log"),
			@ApiResponse(code = 400, message = "Validation Error"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Register a new Log")
	public ResponseEntity<?> save(HttpServletRequest requestContext, @Valid @RequestBody final LogDTO logDTO) {
		final String method = "[ LogController.save ] - ";
		log.info(method + "BEGIN");

		try {
			log.info(method + "Calling logService.save... sending: " + logDTO.toString());
			Log log = this.logService.save(logDTO, requestContext);

			return new ResponseEntity<>(new CreatedResponseObject(log.getId()), HttpStatus.CREATED);
		} catch (LogValidationException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			log.info(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			log.info(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info(method + "END");
		}
	}

}

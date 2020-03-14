package br.com.brazilcode.cb.administration.controller;

import static br.com.brazilcode.cb.libs.constants.ApiResponseConstants.*;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.administration.dto.LogDTO;
import br.com.brazilcode.cb.administration.exception.LogValidationException;
import br.com.brazilcode.cb.administration.service.LogService;

/**
 * Classe responsável por expor as APIs para Logs.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:10:05
 * @version 1.0
 */
@RestController
@RequestMapping("logs")
public class LogController implements Serializable {

	private static final long serialVersionUID = 5226137304754768180L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

	@Autowired
	private LogService logService;

	/**
	 * Método responsável por salvar um Log.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param requestContext
	 * @param logAuditoria
	 * @return
	 */
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(HttpServletRequest requestContext, @Valid @RequestBody final LogDTO logDTO) {
		String method = "[ LogController.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling logService.save... sending: " + logDTO.toString());
			this.logService.save(logDTO, requestContext);
		} catch (LogValidationException e) {
			LOGGER.debug(method + e.getMessage(), e);
			return new ResponseEntity<>(VALIDATION_ERROR_RESPONSE + e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		LOGGER.debug(method + "END");
		return new ResponseEntity<>(CREATED_RESPONSE, HttpStatus.CREATED);
	}

}

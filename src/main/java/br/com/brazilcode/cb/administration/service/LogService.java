package br.com.brazilcode.cb.administration.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.brazilcode.cb.administration.exception.LogValidationException;
import br.com.brazilcode.cb.libs.dto.LogDTO;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.Log;
import br.com.brazilcode.cb.libs.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for applying the business rules for {@link Log}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:32:07
 * @version 2.0
 */
@Service
@Slf4j
public class LogService implements Serializable {

	private static final long serialVersionUID = -8155339416991096631L;

	@Autowired
	private LogRepository logDAO;

	@Autowired
	private UserService userService;

	/**
	 * Method responsible for saving a {@link Log} in the database applying the business rules.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param logDTO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Log save(LogDTO logDTO, HttpServletRequest request) throws Exception {
		final String method = "[ LogService.save ] - ";
		log.info(method + "BEGIN");

		try {
			log.info(method + "Validating mandatory fields");
			this.validateMandatoryFields(logDTO);

			log.info(method + "Converting DTO to Entity");
			Log logE = this.convertDtoToEntity(logDTO);

			log.info(method + "Getting request IP");
			final String ipAdressRequest = request.getRemoteAddr();
			logE.setIp(ipAdressRequest);

			log.info(method + "Saving: " + log.toString());
			return this.logDAO.save(logE);
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			throw e;
		} finally {
			log.info(method + "END");
		}
	}

	/**
	 * Method responsible for validating the mandatory fields for {@link LogDTO}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param logDTO
	 * @throws LogValidationException
	 */
	private void validateMandatoryFields(LogDTO logDTO) throws LogValidationException {
		final String method = "[ LogService.validateMandatoryFields ] - ";
		log.info(method + "BEGIN");

		StringBuilder warnings = new StringBuilder();

		if (logDTO != null) {
			if (logDTO.getUser() == null) {
				warnings.append(", Field \'user\' cannot be null.");
			}

			if (StringUtils.isBlank(logDTO.getDescription())) {
				warnings.append(", Field \'description\' cannot be null.");
			}
		} else {
			warnings.append(", Object Log cannot be null");
		}

		if (warnings.length() > 1) {
			log.error(method + "Validation warnings" + warnings.toString());
			throw new LogValidationException(warnings.toString());
		}

		log.info(method + "END");
	}

	/**
	 * Method responsible for converting an {@link LogDTO} object to an {@link Log} entity.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param logDTO
	 * @return
	 */
	private Log convertDtoToEntity(LogDTO logDTO) {
		final String method = "[ LogService.convertDtoToEntity ] - ";
		log.info(method + "BEGIN");

		Log logE = new Log();
		try {
			log.info(method + "Loading Log");
			logE.setUser(this.userService.verifyIfExists(logDTO.getUser()));
			logE.setDescription(logDTO.getDescription());
			logE.setTimestamp(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			throw e;
		}

		log.info(method + "END... Returning: " + log.toString());
		return logE;
	}

	/**
	 * Method responsible for verifying whether {@link Log} exists by the given ID.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	public Log verifyIfExists(Long id) {
		return logDAO.findById(id).orElseThrow(() -> new ResourceNotFoundException(", Log not found for the given ID: " + id));
	}

}

package br.com.brazilcode.cb.administration.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.brazilcode.cb.administration.exception.LogValidationException;
import br.com.brazilcode.cb.libs.dto.LogDTO;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.Log;
import br.com.brazilcode.cb.libs.repository.LogRepository;

/**
 * Classe responsável por aplicar as regras de negócio para {@link Log}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 11 de mar de 2020 23:32:07
 * @version 1.0
 */
@Service
public class LogService implements Serializable {

	private static final long serialVersionUID = -8155339416991096631L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

	@Autowired
	private LogRepository logDAO;

	@Autowired
	private UserService userService;

	/**
	 * Método responsável por salvar um {@link Log} no banco de dados aplicando as regras de negócio.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link Log}
	 * @param requestContext
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Log save(LogDTO logDTO, HttpServletRequest request) throws Exception {
		final String method = "[ LogService.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Validating mandatory fields");
			this.validateMandatoryFields(logDTO);

			LOGGER.debug(method + "Converting DTO to Entity");
			Log log = this.convertDtoToEntity(logDTO);

			LOGGER.debug(method + "Getting request IP");
			final String ipAdressRequest = request.getRemoteAddr();
			log.setIp(ipAdressRequest);

			LOGGER.debug(method + "Saving: " + log.toString());
			return this.logDAO.save(log);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por validar os campos obrigatórios para {@link LogDTO}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param logDTO
	 * @throws LogValidationException
	 */
	private void validateMandatoryFields(LogDTO logDTO) throws LogValidationException {
		final String method = "[ LogService.validateMandatoryFields ] - ";
		LOGGER.debug(method + "BEGIN");

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
			LOGGER.error(method + "Validation warnings" + warnings.toString());
			throw new LogValidationException(warnings.toString());
		}

		LOGGER.debug(method + "END");
	}

	/**
	 * Método responsável por converter um objeto {@link LogDTO} para entidade {@link Log}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link LogDTO}
	 * @return {@link Log} com os atributos preenchidos com os dados do objeto DTO
	 */
	private Log convertDtoToEntity(LogDTO logDTO) {
		final String method = "[ LogService.convertDtoToEntity ] - ";
		LOGGER.debug(method + "BEGIN");

		Log log = new Log();
		try {
			LOGGER.debug(method + "Loading Log");
			log.setUser(this.userService.verifyIfExists(logDTO.getUser()));
			log.setDescription(logDTO.getDescription());
			log.setTimestamp(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		}

		LOGGER.debug(method + "END... Returning: " + log.toString());
		return log;
	}

	/**
	 * Método responsável por verificar se o {@link Log} existe pelo ID informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return {@link Log} caso ID seja encontrado na base de dados
	 */
	public Log verifyIfExists(Long id) {
		return logDAO.findById(id).orElseThrow(() -> new ResourceNotFoundException(", Log not found for the given ID: " + id));
	}

}

package br.com.brazilcode.cb.administration.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.administration.dto.UserDTO;
import br.com.brazilcode.cb.administration.exception.UniqueContraintValidationException;
import br.com.brazilcode.cb.administration.exception.UserValidationException;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.repository.UserRepository;

/**
 * Classe de serviço para Users.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 12 de mar de 2020 00:10:08
 * @version 1.3
 */
@Service
public class UserService implements Serializable {

	private static final long serialVersionUID = 246320449672499635L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userDAO;

	/**
	 * Método responsável por verificar se o {@link User} existe pelo ID informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	public User verifyIfExists(Long id) throws ResourceNotFoundException {
		return userDAO.findById(id).orElseThrow(() -> new ResourceNotFoundException(", User not found for the given ID: " + id));
	}

	/**
	 * Método responsável por buscar um User pelo seu username.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) throws ResourceNotFoundException {
		return this.userDAO.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(", User not found for the given username: " + username));
	}

	/**
	 * Método responsável por atualizar um {@link User} aplicando as regras de validação (verificação de campos unique).
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param userDTO
	 * @throws Exception
	 */
	public void update(final Long id, final UserDTO userDTO) throws Exception {
		final String method = "[ UserService.update ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Validating received data: " + userDTO.toString());
			User user = this.validateReceivedData(id, userDTO);

			this.userDAO.save(user);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por validar os dados recebidos.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	private User validateReceivedData(Long id, UserDTO userDTO) throws Exception {
		final String method = "[ UserService.validateReceivedData ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Validating mandatory fields");
			this.validateMandatoryFields(userDTO);

			LOGGER.debug(method + "Verifying if user exists - ID: " + id);
			User currentUser = this.verifyIfExists(id);
			User updatedUser;

			LOGGER.debug(method + "Verifying if username / email have already been taken");
			if (!this.emailHasAlreadyBeenTaken(currentUser.getEmail(), userDTO.getEmail())) {
				LOGGER.debug(method + "Converting DTO to entity");
				updatedUser = this.convertDtoToEntity(currentUser, userDTO);

				return updatedUser;
			} else {
				throw new UniqueContraintValidationException(", Username or E-mail have already been taken");
			}
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por validar os campos obrigatórios.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 */
	private void validateMandatoryFields(UserDTO userDTO) throws UserValidationException {
		final String method = "[ UserService.validateMandatoryFields ] - ";
		LOGGER.debug(method + "BEGIN");

		StringBuilder warnings = new StringBuilder();

		if (userDTO != null) {
			if (StringUtils.isBlank(userDTO.getFirstName())) {
				warnings.append(", Field \'firstName\' cannot be null.");
			}

			if (StringUtils.isBlank(userDTO.getEmail())) {
				warnings.append(", Field \'email\' cannot be null.");
			}
		} else {
			warnings.append(", Object User cannot be null");
		}

		if (warnings.length() > 1) {
			LOGGER.error(method + "Validation warnings" + warnings.toString());
			throw new UserValidationException(warnings.toString());
		}

		LOGGER.debug(method + "END");
	}

	/**
	 * Método responsável por verificar seo username informado já está cadastrado no banco de dados.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param username
	 * @return
	 */
	private boolean emailHasAlreadyBeenTaken(String currentEmail, String newEmail) {
		if (!currentEmail.equalsIgnoreCase(newEmail)) {
			Optional<User> user = this.userDAO.findByEmail(newEmail);

			return user.isPresent();
		}

		return false;
	}

	/**
	 * Método responsável por converter um objeto {@link UserDTO} em entidade {@link User} fazendo as devidades veificações.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param userDTO
	 * @param id
	 * @return
	 */
	private User convertDtoToEntity(User user, UserDTO userDTO) {
		final String method = "[ UserService.convertDtoToEntity ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			return user;
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}

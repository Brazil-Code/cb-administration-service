package br.com.brazilcode.cb.administration.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.administration.dto.UserDTO;
import br.com.brazilcode.cb.administration.exception.UniqueContraintValidationException;
import br.com.brazilcode.cb.administration.exception.UserValidationException;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for {@link User}s.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:31:29 PM
 * @version 2.0
 */
@Service
@Slf4j
public class UserService implements Serializable {

	private static final long serialVersionUID = 246320449672499635L;

	@Autowired
	private UserRepository userDAO;

	/**
	 * Method responsible for verifying whether {@link User} exists by the given 'ID'.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException if user not found
	 */
	public User verifyIfExists(Long id) throws ResourceNotFoundException {
		return userDAO.findById(id).orElseThrow(() -> new ResourceNotFoundException(", User not found for the given ID: " + id));
	}

	/**
	 * Method responsible for searching for an {@link User} by the given 'username'.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param username
	 * @return
	 * @throws ResourceNotFoundException if user not found
	 */
	public User findByUsername(String username) throws ResourceNotFoundException {
		return this.userDAO.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(", User not found for the given username:  " + username));
	}

	/**
	 * Method responsible for updating a {@link User} applying the validation rules (verification of unique fields).
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @param userDTO
	 * @throws Exception
	 */
	public void update(final Long id, final UserDTO userDTO) throws Exception {
		final String method = "[ UserService.update ] - ";
		log.info(method + "BEGIN");

		try {
			log.info(method + "Validating received data: " + userDTO.toString());
			User user = this.validateReceivedData(id, userDTO);

			this.userDAO.save(user);
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			throw e;
		} finally {
			log.info(method + "END");
		}
	}

	/**
	 * Method responsible for validating the received data.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	private User validateReceivedData(Long id, UserDTO userDTO) throws Exception {
		final String method = "[ UserService.validateReceivedData ] - ";
		log.info(method + "BEGIN");

		try {
			log.info(method + "Validating mandatory fields");
			this.validateMandatoryFields(userDTO);

			log.info(method + "Verifying if user exists - ID: " + id);
			User currentUser = this.verifyIfExists(id);
			User updatedUser;

			log.info(method + "Verifying if email have already been taken");
			if (!this.emailHasAlreadyBeenTaken(currentUser.getEmail(), userDTO.getEmail())) {
				log.info(method + "Converting DTO to entity");
				updatedUser = this.convertDtoToEntity(currentUser, userDTO);

				return updatedUser;
			} else {
				throw new UniqueContraintValidationException(", The given e-mail has already been taken");
			}
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			throw e;
		} finally {
			log.info(method + "END");
		}
	}

	/**
	 * Method responsible for validating the mandatory fields for updating an {@link User}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param userDTO
	 * @throws UserValidationException
	 */
	private void validateMandatoryFields(UserDTO userDTO) throws UserValidationException {
		final String method = "[ UserService.validateMandatoryFields ] - ";
		log.info(method + "BEGIN");

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
			log.error(method + "Validation warnings" + warnings.toString());
			throw new UserValidationException(warnings.toString());
		}

		log.info(method + "END");
	}

	/**
	 * Method responsible for verifying whether the informed 'email' is already registered in the database.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param currentEmail
	 * @param newEmail
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
	 * Method responsible for converting an {@link UserDTO} object into an {@link User} entity by doing the proper checks.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param user
	 * @param userDTO
	 * @return
	 */
	private User convertDtoToEntity(User user, UserDTO userDTO) {
		final String method = "[ UserService.convertDtoToEntity ] - ";
		log.info(method + "BEGIN");

		try {
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			return user;
		} catch (Exception e) {
			log.error(method + e.getMessage(), e);
			throw e;
		} finally {
			log.info(method + "END");
		}
	}

}

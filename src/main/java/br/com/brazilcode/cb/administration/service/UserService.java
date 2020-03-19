package br.com.brazilcode.cb.administration.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.repository.UserRepository;

/**
 * Classe de serviço para Users.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 12 de mar de 2020 00:10:08
 * @version 1.0
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userDAO;

	/**
	 * Método responsável por verificar se o {@link User} existe pelo ID informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	public User verifyIfExists(Long id) {
		final Optional<User> user = userDAO.findById(id);
		if (!user.isPresent())
			throw new ResourceNotFoundException(", User not found for the given ID: " + id);

		return user.get();
	}

}

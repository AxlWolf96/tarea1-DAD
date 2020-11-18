package org.nh.asegurando;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<User> consulta() {
		return (List<User>) userRepository.findAll();
	}

	public User consultaPorId(long id) {
		return userRepository.findById(id).get();
	}

	public User registro(User b) {
		return userRepository.save(b);
	}

	public void eliminar(User b) {
		userRepository.delete(b);
	}

	public User consultaPorNombre(String name) {
		List<User> users = consulta();
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	public void borrarPorId(long id) {
		userRepository.deleteById(id);
	}

	public boolean isUsuarioExiste(User user) {
		return consultaPorNombre(user.getName()) != null;
	}

	public void borrarTodos() {
		userRepository.deleteAll();
	}

}

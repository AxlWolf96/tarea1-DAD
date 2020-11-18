package org.nh.asegurando;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
	@Autowired
	UserService userService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {
		logger.info("Mostrando todos los usuarios");
		List<User> users = userService.consulta();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		logger.info("Buscando usuario con id de	entrada {}", id);
		User user = userService.consultaPorId(id);
		if (user == null) {
			logger.error("usuario con id {} no	existe", id);
			return new ResponseEntity(new CustomErrorType("Usuario con id " + id + " no encontrado"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creando usuario en HR");
		if (userService.isUsuarioExiste(user)) {
			logger.error("Error en creacion: usuario con nombre {} ya existe", user.getName());
			return new ResponseEntity(
					new CustomErrorType("No se puede crear usuario	" + user.getName() + " ya	existe"),
					HttpStatus.CONFLICT);
		}
		userService.registro(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		logger.info("Actualizando user {}", id);
		User suser = userService.consultaPorId(id);
		if (suser == null) {
			logger.error("Error en actualizacion: usuario con id {} no existe", id);
			return new ResponseEntity(
					new CustomErrorType("No se puede actualzar usuario " + user.getId() + " ,no	existe"),
					HttpStatus.NOT_FOUND);
		}
		suser.setAge(user.getAge());
		suser.setName(user.getName());
		suser.setSalary(user.getSalary());
		userService.registro(suser);
		return new ResponseEntity<User>(suser, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("eliminando user {}", id);
		User user = userService.consultaPorId(id);
		if (user == null) {
			logger.error("Error en borrado:	usuario con id {} no existe", id);
			return new ResponseEntity(new CustomErrorType("No se puede eliminar usuario " + id + " ,por que no existe"),
					HttpStatus.NOT_FOUND);
		}
		userService.borrarPorId(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/user", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		logger.info("eliminando todos los usuarios");
		userService.borrarTodos();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}

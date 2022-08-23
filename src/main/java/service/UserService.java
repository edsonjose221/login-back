package service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import exception.NotFoundException;
import model.User;
import repository.UserRepository;
import util.UserRoles;

public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private User userLogin;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Optional<User> login(User userLogin) {
		return userRepository.findByEmail(userLogin.getEmail()).map(data -> {
			this.userLogin = data;
			return this.userLogin;
		});
	}
	
	public Optional<Object> listUser(Integer id) throws NotFoundException {
		try {
			 return userRepository.findById(id).map(dados -> {
				 if(dados.getUserRoles() == UserRoles.ADMIN) {
					 return userRepository.findAll();
				 } else {
					 return dados;
				 }
			 });
			
		 } catch (NotFoundException e) {
			 throw new NotFoundException("Usuário não encontrado");
		 }
	}
	
	public User saveUser(User user) throws Exception {
		try {
			if(user.getUserRoles() == null) {
				user.setUserRoles(UserRoles.USER);
			}
			
			return userRepository.save(user);
			
		} catch (Exception e) {
			throw new Exception("Erro ao salvar o usuário");
		}
	}
	
	public Optional<User> editUser(User user) {
		return userRepository.findByEmail(user.getEmail()).map(data -> {
			data.setNome(user.getNome());
			data.setEmail(user.getEmail());
			data.setSenha(user.getSenha());
			
			if(user.getUserRoles() == null) {
				data.setUserRoles(UserRoles.USER);
			}
			
			return userRepository.save(data);
		});
	}
	
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void deleteUser(Integer id) throws Exception {
		try {
			userRepository.deleteById(id);
			
		} catch (Exception e) {
			throw new Exception("Erro ao deletar usuário");
		}
	}
	
	public UserRoles[] roles() {
		return UserRoles.values();
	}
}

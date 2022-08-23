package service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import data.DetailUserData;
import model.User;
import repository.UserRepository;

public class DetailUserService implements UserDetailsService {
	
	private final UserRepository userRepository = null;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		
		if (user.isEmpty()) {
			 throw new UsernameNotFoundException("Usuário [" + username + "] não encontrado");
		}
		return new DetailUserData(user);
	}
	
	
}

package security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import service.DetailUserService;

public class JWTConfiguration extends WebSecurityConfigurerAdapter  {
	
	private final DetailUserService detailUserService;
	private PasswordEncoder passwordEncoder;
	
	public JWTConfiguration(DetailUserService detailUserService, PasswordEncoder passwordEncoder) {
		this.detailUserService = detailUserService;
		this.passwordEncoder = passwordEncoder;
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailUserService).passwordEncoder(passwordEncoder);
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeHttpRequests()
			.antMatchers(HttpMethod.POST, "/login", "/user/saver", "/user/login").permitAll()
			.antMatchers(HttpMethod.GET, "/user/roles").permitAll()
			.antMatchers(HttpMethod.PUT, "/user/editr").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilter(new JWTValidateFilter(authenticationManager()))
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return (CorsConfigurationSource) source;
    }
}

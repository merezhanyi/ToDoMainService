package nextmainfocus.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	AccountService securityService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		List<UserDetails> users = new ArrayList<>();

		// default user
		UserDetails defaultUser = User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER")
				.build();
		users.add(defaultUser);

		// default admin
		UserDetails defaultAdmin = User.withUsername("admin").password(passwordEncoder().encode("admin"))
				.roles("USER", "ADMIN").build();
		users.add(defaultAdmin);

		return new InMemoryUserDetailsManager(users);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers("/",
				"/api/v1/login/").permitAll().anyRequest()
				.authenticated().and().httpBasic();

		return http.build();
	}
}

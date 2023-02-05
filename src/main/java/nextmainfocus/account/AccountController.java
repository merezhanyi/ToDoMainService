package nextmainfocus.account;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;

	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	@Autowired
	public PasswordEncoder passwordEncoder;

	private UserDetails getUserDetailsWithEncodedPassword(Account account) {
		return org.springframework.security.core.userdetails.User.withUsername(account.getUsername())
				.password(passwordEncoder.encode(account.getPassword())).roles(account.getRole()).build();
	}

	private UserDetails getUserDetails(Account account) {
		return org.springframework.security.core.userdetails.User.withUsername(account.getUsername())
				.password(account.getPassword()).roles(account.getRole()).build();
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "login/", produces = "application/json")
	public ResponseEntity<String> login(@RequestBody Account account) {
		logger.info("Received login request for user: {}", account.getUsername());

		HttpStatus status;
		Account accountFromDb = accountService.findByUsername(account.getUsername());

		if (accountFromDb != null) {
			if (passwordEncoder.matches(account.getPassword(), accountFromDb.getPassword())) {

				UserDetails userDetails = getUserDetails(accountFromDb);
				inMemoryUserDetailsManager.createUser(userDetails);

				logger.info("{} logged in", account.getUsername());
				return ResponseEntity.ok(account.getUsername() + " logged in.");
			} else {
				JSONObject body = new JSONObject();
				body.put("code", "NOT_FOUND");
				body.put("message", "Oops! User with the credentials not found.");
				body.put("username", account.getUsername());
				status = HttpStatus.NOT_FOUND;

				logger.error("Password is incorrect for user: {}", account.getUsername());
				return new ResponseEntity<>(body.toString(), status);
			}
		} else {
			JSONObject body = new JSONObject();
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! User not found.");
			body.put("username", account.getUsername());
			status = HttpStatus.NOT_FOUND;

			logger.error( "{} is not in the database", account.getUsername());
			return new ResponseEntity<>(body.toString(), status);
		}
	}

	@GetMapping("logout/")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		logger.info("Logged out");
		return ResponseEntity.ok("Logged out");
	}

	@PostMapping(value = "users/", produces = "application/json")
	public ResponseEntity<String> createUser(@RequestBody Account account) {
		logger.info("Received request to create a new user: {}", account);

		HttpStatus status;

		try {
			Account encodedUser = new Account();
			encodedUser.setUsername(account.getUsername());
			encodedUser.setPassword(passwordEncoder.encode(account.getPassword()));
			encodedUser.setRole(account.getRole());

			Account newDbAccount = accountService.createUser(encodedUser);

			if (newDbAccount != null) {
				UserDetails newAccount = getUserDetailsWithEncodedPassword(account);
				inMemoryUserDetailsManager.createUser(newAccount);

				JSONObject body = new JSONObject();
				body.put("id", newDbAccount.getId());
				body.put("username", newDbAccount.getUsername());
				body.put("role", newDbAccount.getRole());
				status = HttpStatus.OK;

				logger.info("{} was created", account.getUsername());

				return new ResponseEntity<>(body.toString(), status);
			} else {
				JSONObject body = new JSONObject();
				body.put("code", "INTERNAL_SERVER_ERROR");
				body.put("message", "Oops! User creation failed.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;

				logger.error("{} already presents in the database", account.getUsername());
				return new ResponseEntity<>(body.toString(), status);
			}
		} catch (Exception exception) {
			JSONObject body = new JSONObject();
			body.put("code", "INTERNAL_SERVER_ERROR");
			body.put("message", "Oops! User creation failed.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;

			logger.error("{} was not created due to:", account.getUsername());
			logger.error(exception.getMessage());
			return new ResponseEntity<>(body.toString(), status);
		}
	}
}

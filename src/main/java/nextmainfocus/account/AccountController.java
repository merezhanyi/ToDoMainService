package nextmainfocus.account;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import nextmainfocus.error.ErrorMessenger;
import nextmainfocus.util.Utility;

// @CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;

	// @Autowired
	// private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	@Autowired
	public PasswordEncoder passwordEncoder;

	private final JwtService jwtService = new JwtService();
	// private final AuthenticationManager authenticationManager;

	private UserDetails getUserDetailsWithoutPassword(Account account) {
		var user = User.withUsername(account.getUsername());
		// var userP = user.password(passwordEncoder.encode(account.getPassword()));
		var userPR = user.roles(account.getRole());
		var userPRO = userPR.build();
		return userPRO;
		// return User.withUsername(account.getUsername())
		// .password(passwordEncoder.encode(account.getPassword()))
		// .roles(account.getRole())
		// .build();
	}

	private UserDetails getUserDetails(Account account) {
		// return User.builder().username(account.getUsername())
		// 		.roles(account.getRole()).build();
		return User.withUsername(account.getUsername())
		.password(account.getPassword())
		.roles(account.getRole())
		.build();
	}

	@PostMapping(value = "register", produces = "application/json")
	public ResponseEntity<String> register(@RequestBody Account account) {
		logger.info("Received request to create a new user: {}", account);

		String body;
		HttpStatus status;
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Account encodedUser = new Account();
			encodedUser.setUsername(account.getUsername());
			encodedUser.setPassword(passwordEncoder.encode(account.getPassword()));
			encodedUser.setRole("USER");

			var newDbAccount = accountService.createUser(encodedUser);

			if (newDbAccount != null) {
				UserDetails newAccount = getUserDetailsWithoutPassword(newDbAccount);
				// inMemoryUserDetailsManager.createUser(newAccount);
				logger.info("{} was created", account.getUsername());

				var jwtToken = jwtService.generateToken(newAccount);

				status = HttpStatus.CREATED;
				newDbAccount.getUsername();
				// generate response with jwt token and user details
				var response = Map.of("token", jwtToken, "user", newAccount);
				body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_CREATED, response);
				// generate location header with login endpoint
				var location = UriComponentsBuilder.fromUriString("/api/v1/auth/login/").build().toUri();
				responseHeaders.setLocation(location);
				// var location =
				// ServletUriComponentsBuilder.fromUriString("/api/v1/auth/login/")
				// .toUri();
				// responseHeaders.setLocation(location);
			} else {
				logger.error("{} already presents in the database", account.getUsername());

				status = HttpStatus.CONFLICT;
				body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_CONFLICT, null);
			}
		} catch (Exception exception) {
			logger.error("{} was not created due to:", account.getUsername());
			logger.error(exception.getMessage());
			logger.error(exception.toString());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
		}

		return new ResponseEntity<>(body, status);
	}

	@PostMapping(value = "login", produces = "application/json")
	public ResponseEntity<String> authenticate(@RequestBody Account account) {
		logger.info("Received login request for user: {}", account.getUsername());

		String body;
		HttpStatus status;
		Account accountFromDb = accountService.findByUsername(account.getUsername());

		if (accountFromDb != null) {
			// Authentication authentication = authenticationManager.authenticate(
			// new UsernamePasswordAuthenticationToken(
			// account.getUsername(),
			// account.getPassword()));

			if (passwordEncoder.matches(account.getPassword(),
					accountFromDb.getPassword())) {

				UserDetails userDetails = getUserDetails(accountFromDb);
				// var userDetails = (User) authentication.getPrincipal();
				var jwtToken = jwtService.generateToken(userDetails);

				// inMemoryUserDetailsManager.createUser(userDetails);
				logger.info("{} logged in", account.getUsername());

				status = HttpStatus.OK;
				// generate response map with token, username
				var responseMap = Map.of("token", jwtToken, "username", userDetails.getUsername());
				body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_FOUND, responseMap);
			} else {
				logger.error("Password is incorrect for user: {}", account.getUsername());

				status = HttpStatus.NOT_FOUND;
				body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND, null);
			}
		} else {
			logger.error("{} is not in the database", account.getUsername());

			status = HttpStatus.NOT_FOUND;
			body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND, null);
		}

		return new ResponseEntity<>(body, status);
	}

	// logout controller
	@PostMapping(value = "logout", produces = "application/json")
	public ResponseEntity<String> logout(@RequestBody Account account) {
		logger.info("Received logout request for user: {}", account.getUsername());

		String body;
		HttpStatus status;

		Account accountFromDb = accountService.findByUsername(account.getUsername());

		if (accountFromDb != null) {
			if (passwordEncoder.matches(account.getPassword(),
					accountFromDb.getPassword())) {
				logger.info("{} logged out", account.getUsername());

				status = HttpStatus.OK;
				body = Utility.fillResponseBody(status, "OK", null);
			} else {
				logger.error("Password is incorrect for user: {}", account.getUsername());

				status = HttpStatus.NOT_FOUND;
				body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND, null);
			}
		} else {
			logger.error("{} is not in the database", account.getUsername());

			status = HttpStatus.NOT_FOUND;
			body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND, null);
		}

		return new ResponseEntity<>(body, status);
	}

	// @Autowired
	// private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	// @Autowired
	// public PasswordEncoder passwordEncoder;

	// private UserDetails getUserDetailsWithEncodedPassword(Account account) {
	// return
	// org.springframework.security.core.userdetails.User.withUsername(account.getUsername())
	// .password(passwordEncoder.encode(account.getPassword())).roles(account.getRole()).build();
	// }

	// private UserDetails getUserDetails(Account account) {
	// return
	// org.springframework.security.core.userdetails.User.withUsername(account.getUsername())
	// .password(account.getPassword()).roles(account.getRole()).build();
	// }

	// @CrossOrigin(origins = "*")
	// @PostMapping(value = "login/", produces = "application/json")
	// public ResponseEntity<String> login(@RequestBody Account account) {
	// logger.info("Received login request for user: {}", account.getUsername());

	// HttpStatus status;
	// Account accountFromDb = accountService.findByUsername(account.getUsername());

	// if (accountFromDb != null) {
	// if (passwordEncoder.matches(account.getPassword(),
	// accountFromDb.getPassword())) {

	// UserDetails userDetails = getUserDetails(accountFromDb);
	// inMemoryUserDetailsManager.createUser(userDetails);

	// logger.info("{} logged in", account.getUsername());
	// return ResponseEntity.ok(account.getUsername() + " logged in.");
	// } else {
	// JSONObject body = new JSONObject();
	// body.put("code", "NOT_FOUND");
	// body.put("message", "Oops! User with the credentials not found.");
	// body.put("username", account.getUsername());
	// status = HttpStatus.NOT_FOUND;

	// logger.error("Password is incorrect for user: {}", account.getUsername());
	// return new ResponseEntity<>(body.toString(), status);
	// }
	// } else {
	// JSONObject body = new JSONObject();
	// body.put("code", "NOT_FOUND");
	// body.put("message", "Oops! User not found.");
	// body.put("username", account.getUsername());
	// status = HttpStatus.NOT_FOUND;

	// logger.error( "{} is not in the database", account.getUsername());
	// return new ResponseEntity<>(body.toString(), status);
	// }
	// }

	// @GetMapping("logout/")
	// public ResponseEntity<String> logout(HttpServletRequest request,
	// HttpServletResponse response) {
	// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// if (auth != null) {
	// new SecurityContextLogoutHandler().logout(request, response, auth);
	// }
	// logger.info("Logged out");
	// return ResponseEntity.ok("Logged out");
	// }

	// @PostMapping(value = "users/", produces = "application/json")
	// public ResponseEntity<String> createUser(@RequestBody Account account) {
	// logger.info("Received request to create a new user: {}", account);

	// HttpStatus status;

	// try {
	// Account encodedUser = new Account();
	// encodedUser.setUsername(account.getUsername());
	// encodedUser.setPassword(passwordEncoder.encode(account.getPassword()));
	// encodedUser.setRole(account.getRole());

	// Account newDbAccount = accountService.createUser(encodedUser);

	// if (newDbAccount != null) {
	// UserDetails newAccount = getUserDetailsWithEncodedPassword(account);
	// inMemoryUserDetailsManager.createUser(newAccount);

	// JSONObject body = new JSONObject();
	// body.put("id", newDbAccount.getId());
	// body.put("username", newDbAccount.getUsername());
	// body.put("role", newDbAccount.getRole());
	// status = HttpStatus.OK;

	// logger.info("{} was created", account.getUsername());

	// return new ResponseEntity<>(body.toString(), status);
	// } else {
	// JSONObject body = new JSONObject();
	// body.put("code", "INTERNAL_SERVER_ERROR");
	// body.put("message", "Oops! User creation failed.");
	// status = HttpStatus.INTERNAL_SERVER_ERROR;

	// logger.error("{} already presents in the database", account.getUsername());
	// return new ResponseEntity<>(body.toString(), status);
	// }
	// } catch (Exception exception) {
	// JSONObject body = new JSONObject();
	// body.put("code", "INTERNAL_SERVER_ERROR");
	// body.put("message", "Oops! User creation failed.");
	// status = HttpStatus.INTERNAL_SERVER_ERROR;

	// logger.error("{} was not created due to:", account.getUsername());
	// logger.error(exception.getMessage());
	// return new ResponseEntity<>(body.toString(), status);
	// }
	// }
}

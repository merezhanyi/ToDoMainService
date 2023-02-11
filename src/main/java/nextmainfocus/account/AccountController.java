package nextmainfocus.account;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import nextmainfocus.error.ErrorMessenger;
import nextmainfocus.jwt.JwtService;
import nextmainfocus.util.Utility;

// @CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;

	@Autowired
	public PasswordEncoder passwordEncoder;

	private final JwtService jwtService = new JwtService();
	private final AuthenticationManager authenticationManager;

	private Object getUserDetailsWithoutPassword(Account account) {
		return Map.of("username", account.getUsername(), "roles", account.getRole());
	}

	@PostMapping(value = "register", produces = "application/json")
	public ResponseEntity<String> register(@RequestBody Account account) {
		logger.info("🔒 Received request to create new User «{}»", account);

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
				var newAccount = getUserDetailsWithoutPassword(newDbAccount);
				logger.info("🔒 User «{}» was created", newDbAccount.getUsername());

				status = HttpStatus.CREATED;
				var response = new JSONObject().append("user", newAccount);
				body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_CREATED, response);
				var location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/api/v1/auth/login/")
						.build()
						.toUri();
				responseHeaders.setLocation(location);
			} else {
				logger.error("🔒 User «{}» is already present in the database", account.getUsername());

				status = HttpStatus.CONFLICT;
				body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_CONFLICT, null);
			}
		} catch (Exception exception) {
			logger.error("🔒 User «{}» was not created due to:", account.getUsername());
			logger.error(exception.getMessage());
			logger.error(exception.toString());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
		}

		return new ResponseEntity<>(body, responseHeaders, status);
	}

	@PostMapping(value = "login", produces = "application/json")
	public ResponseEntity<String> authenticate(@RequestBody Account account) {
		logger.info("🔒 Received login request for User «{}»", account.getUsername());

		String body;
		HttpStatus status;

		try {
			// The authenticate() method throws AuthenticationException if username/password
			// don't match or user not found
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							account.getUsername(),
							account.getPassword()));

			logger.info("🔒 User {} is logged in", account.getUsername());

			UserDetails userDetailsFromToken = (UserDetails) authentication.getPrincipal();
			// generate Token response map with token and expiration date
			var jwtToken = jwtService.generateToken(userDetailsFromToken);
			var expirationDate = jwtService.extractExpirationDate(jwtToken);
			var token = Map.of("token", jwtToken, "expirationDate", expirationDate);
			var user = getUserDetailsWithoutPassword((Account) userDetailsFromToken);

			// generate response map with token details, user details
			var responseMap = Map.of("token", token, "user", user);

			status = HttpStatus.OK;
			body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_FOUND, responseMap);
		} catch (BadCredentialsException e) {
			logger.error("🔒 Password is incorrect for User «{}»", account.getUsername());

			status = HttpStatus.NOT_FOUND;
			body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND,
					null);
		} catch (AuthenticationException e) {
			logger.error("🔒 User «{}» is not in the database", account.getUsername());

			status = HttpStatus.NOT_FOUND;
			body = Utility.fillResponseBody(status, AccountMessenger.ACCOUNT_NOT_FOUND,
					null);
		} catch (Exception e) {
			logger.error("🔒 User «{}» cannot log in due to:", account.getUsername());
			logger.error(e.getMessage());
			logger.error(e.toString());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
		}

		return new ResponseEntity<>(body, status);
	}

	@PostMapping(value = "logout", produces = "application/json")
	public ResponseEntity<String> logout(@RequestBody Account account, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		logger.info("🔒 Received logout request from User «{}»",
				account.getUsername());

		String body;
		HttpStatus status;
		Account accountFromDb = accountService.findByUsername(account.getUsername());

		if (accountFromDb == null) {
			logger.error("🔒 User «{}» is not in the database", account.getUsername());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		try {
			var jwtToken = jwtService.extractTokenFromHeader(token);
			logger.info("TOKEN: {}", jwtToken);
			logger.info("USER: {}", accountFromDb);
			jwtService.removeToken(jwtToken.get("token"));
			logger.info("🔒 User «{}» is logged out", account.getUsername());

			status = HttpStatus.NO_CONTENT;
			body = Utility.fillResponseBody(status, "OK",
					null);
		} catch (Exception e) {
			logger.error("🔒 User cannot log out due to:");
			logger.error(e.getMessage());
			logger.error(e.toString());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
		}

		return new ResponseEntity<>(body, status);
	}

}

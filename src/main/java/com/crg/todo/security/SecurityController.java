package com.crg.todo.security;

import com.crg.todo.security.entity.Account;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    SecurityService securityService;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @PostMapping(value = "users/", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody Account account) {
        logger.info("Received request to create a new user: " + account.toString());

        HttpStatus status;

        try {
            Account encodedUser = new Account();
            encodedUser.setUsername(account.getUsername());
            encodedUser.setPassword(passwordEncoder.encode(account.getPassword()));
            encodedUser.setRole(account.getRole());

            Account newDbAccount = securityService.createUser(encodedUser);

            if (newDbAccount != null) {
                UserDetails newAccount = getUserDetailsWithEncodedPswd(account);
                inMemoryUserDetailsManager.createUser(newAccount);

                JSONObject body = new JSONObject();
                body.put("id", newDbAccount.getId());
                body.put("username", newDbAccount.getUsername());
                body.put("role", newDbAccount.getRole());
                status = HttpStatus.OK;

                logger.info(account.getUsername() + " was created");

                return new ResponseEntity<>(body, status);
            } else {
                JSONObject body = new JSONObject();
                body.put("code", "INTERNAL_SERVER_ERROR");
                body.put("message", "Oops! User creation failed.");
                status = HttpStatus.INTERNAL_SERVER_ERROR;

                logger.error(account.getUsername() + " already presents in the database");
                return new ResponseEntity<>(body.toString(), status);
            }
        } catch (Exception exception) {
            JSONObject body = new JSONObject();
            body.put("code", "INTERNAL_SERVER_ERROR");
            body.put("message", "Oops! User creation failed.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;

            logger.error(account.getUsername() + " was not created due to:");
            logger.error(exception.getMessage());
            return new ResponseEntity<>(body.toString(), status);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "login/", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody Account account) {
        logger.info("Received login request for user: " + account.getUsername());

        HttpStatus status;
        Account accountFromDb = securityService.findByUsername(account.getUsername());

        if (accountFromDb != null) {
            if (passwordEncoder.matches(account.getPassword(), accountFromDb.getPassword())) {

                UserDetails userDetails = getUserDetails(accountFromDb);
                inMemoryUserDetailsManager.createUser(userDetails);

                logger.info(account.getUsername() + " logged in");
                return ResponseEntity.ok(account.getUsername() + " logged in.");
            } else {
                JSONObject body = new JSONObject();
                body.put("code", "NOT_FOUND");
                body.put("message", "Oops! User with the credentials not found.");
                body.put("username", account.getUsername());
                status = HttpStatus.NOT_FOUND;

                logger.error("Password is incorrect for user: " + account.getUsername());
                return new ResponseEntity<>(body.toString(), status);
            }
        } else {
            JSONObject body = new JSONObject();
            body.put("code", "NOT_FOUND");
            body.put("message", "Oops! User not found.");
            body.put("username", account.getUsername());
            status = HttpStatus.NOT_FOUND;

            logger.error(account.getUsername() + " is not in the database");
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

    private UserDetails getUserDetailsWithEncodedPswd(Account account) {
        return org.springframework.security.core.userdetails.User.withUsername(account.getUsername()).password(passwordEncoder.encode(account.getPassword())).roles(account.getRole()).build();
    }

    private UserDetails getUserDetails(Account account) {
        return org.springframework.security.core.userdetails.User.withUsername(account.getUsername()).password(account.getPassword()).roles(account.getRole()).build();
    }
}

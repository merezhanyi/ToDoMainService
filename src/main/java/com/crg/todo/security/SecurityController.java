package com.crg.todo.security;

import com.crg.todo.security.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class SecurityController {

    private static final Logger
            logger =
            LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    SecurityService securityService;

    @PostMapping(value = "users/", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        logger.info("Received request to create a new user: " +
                user.toString());

        HttpStatus status;

        try {
            User newUser = securityService.createUser(user);

            if (newUser != null) {
                ObjectMapper mapper = new ObjectMapper();
                String body = mapper.writeValueAsString(newUser.toString());
                status = HttpStatus.OK;

                return new ResponseEntity<>(body, status);
            } else {
                JSONObject body = new JSONObject();
                body.put("code", "INTERNAL_SERVER_ERROR");
                body.put("message", "Oops! User creation failed.");
                status = HttpStatus.INTERNAL_SERVER_ERROR;

                return new ResponseEntity<>(body.toString(), status);
            }
        } catch (Exception e) {
            JSONObject body = new JSONObject();
            body.put("code", "INTERNAL_SERVER_ERROR");
            body.put("message", "Oops! User creation failed.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(body.toString(), status);
        }
    }

    @GetMapping("logout/")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response) {
        Authentication
                auth =
                SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logged out");
    }
}

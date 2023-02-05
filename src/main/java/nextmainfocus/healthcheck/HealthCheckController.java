package nextmainfocus.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nextmainfocus.util.Utility;

@RestController
public class HealthCheckController {
	private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

	@CrossOrigin(origins = "*")
	@GetMapping("/")
	public ResponseEntity<String> healthCheck() {
		logger.info("Received health check request");
		logger.info(">>> RESPONSE: {}", HealthcheckMessenger.RESPONSE);

		HttpStatus status = HttpStatus.OK;
		String body = Utility.fillResponseBody(status, HealthcheckMessenger.RESPONSE,
				null);

		return new ResponseEntity<>(body, status);
	}
}

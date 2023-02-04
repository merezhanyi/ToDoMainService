package nextmainfocus.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nextmainfocus.util.Translator;

@RestController
public class HealthCheckController {
	private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

	@CrossOrigin(origins = "*")
	@GetMapping("/")
	public ResponseEntity<String> healthCheck() {
		logger.info("Received health check request");

		String msg = Translator.toLocale(Translator.HEALTHCHECK_RESPONSE);

		return ResponseEntity.ok(msg);
	}
}

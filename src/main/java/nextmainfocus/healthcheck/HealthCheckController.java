package nextmainfocus.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
	private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

	@CrossOrigin(origins = "*")
	@GetMapping("/")
	public ResponseEntity<String> healthCheck() {
		logger.info("Received health check request");

		return ResponseEntity.ok("I feel good! 🎵");
	}
}

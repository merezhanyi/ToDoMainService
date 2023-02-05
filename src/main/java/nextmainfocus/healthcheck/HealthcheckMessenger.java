package nextmainfocus.healthcheck;

import lombok.Getter;
import nextmainfocus.util.Translator;

@Getter
public enum HealthcheckMessenger {
	RESPONSE("healthcheck.response"),
	;

	private String message;

	private HealthcheckMessenger(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return Translator.toLocale(this.message);
	}
}

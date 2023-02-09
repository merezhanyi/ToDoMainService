package nextmainfocus.error;

import lombok.Getter;
import nextmainfocus.util.Translator;

@Getter
public enum ErrorMessenger {
	ERROR_SERVER("error.server"),
	ERROR_CONFLICT("error.conflict"),
	ERROR_INVALID_UUID(
			"error.invalid.uuid"),
	;

	private String message;

	ErrorMessenger(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return Translator.toLocale(this.message);
	}
}

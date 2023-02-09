package nextmainfocus.util;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {
	public static <T, U> String fillResponseBody(HttpStatusCode code, T message, @Nullable U results) {
		JSONObject body = new JSONObject();
		body.put("code", code);
		body.put("message", message);
		body.put("response", results);
		return body.toString();
	}
}

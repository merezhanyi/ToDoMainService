package nextmainfocus.util;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {
	public static <T> String fillResponseBody(HttpStatusCode code, String message, @Nullable List<T> results) {
		JSONObject body = new JSONObject();
		body.put("code", code);
		body.put("message", message);
		body.put("results", results);
		return body.toString();
	}
}

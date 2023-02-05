package nextmainfocus.util;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {
	public static <T, U> String fillResponseBody(HttpStatusCode code, T message, @Nullable List<U> results) {
		JSONObject body = new JSONObject();
		body.put("code", code);
		body.put("message", message);
		body.put("results", results);
		return body.toString();
	}
}

package nextmainfocus.http;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

@Component
@UtilityClass
public class Utility {
	public static <T> String fillResponseBody(String code, String message, @Nullable List<T> results) {
		JSONObject body = new JSONObject();
		body.put("code", code);
		body.put("message", message);
		body.put("results", results);
		return body.toString();
	}
}

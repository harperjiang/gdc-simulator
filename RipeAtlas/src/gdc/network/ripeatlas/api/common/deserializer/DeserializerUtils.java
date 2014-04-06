package gdc.network.ripeatlas.api.common.deserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DeserializerUtils {

	public static String getString(String name, JsonObject object) {
		JsonElement element = object.get(name);
		if (element != null)
			return element.getAsString();
		return null;
	}

	public static Double getDouble(String name, JsonObject object) {
		JsonElement element = object.get(name);
		if (element != null)
			return element.getAsDouble();
		return null;
	}
}

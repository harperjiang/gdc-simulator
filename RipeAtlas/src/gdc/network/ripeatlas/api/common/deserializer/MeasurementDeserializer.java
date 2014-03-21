package gdc.network.ripeatlas.api.common.deserializer;

import gdc.network.ripeatlas.model.Measurement;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class MeasurementDeserializer extends BeanDeserializer<Measurement> {

	public Measurement deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Measurement measurement = super.deserialize(json, typeOfT, context);
		JsonObject object = json.getAsJsonObject();
		measurement.setType(((JsonObject) object.get("type")).get("name")
				.getAsString());
		// measurement.setProbeSources(probeSources);

		return measurement;
	}
}

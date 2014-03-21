package gdc.network.ripeatlas.api.common.deserializer;

import gdc.network.ripeatlas.model.Measurement;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class MeasurementDeserializer implements JsonDeserializer<Measurement> {

	public Measurement deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Measurement measurement = new Measurement();
		JsonObject object = (JsonObject) json;
		measurement.setId(object.get("msm_id").getAsInt());
		measurement.setDstAddr(object.get("dst_addr").getAsString());
		measurement.setDstName(object.get("dst_name").getAsString());
		measurement.setInterval(object.get("interval").getAsInt());
		measurement
				.setStartTime(new Date(object.get("start_time").getAsLong()));
		measurement.setStopTime(new Date(object.get("stop_time").getAsLong()));
		measurement.setType(((JsonObject) object.get("type")).get("name")
				.getAsString());
		// measurement.setProbeSources(probeSources);

		return measurement;
	}
}

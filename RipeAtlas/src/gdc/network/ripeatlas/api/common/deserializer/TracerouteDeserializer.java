package gdc.network.ripeatlas.api.common.deserializer;

import gdc.network.ripeatlas.model.ErrorOutput;
import gdc.network.ripeatlas.model.Output;
import gdc.network.ripeatlas.model.TracerouteOutput;
import gdc.network.ripeatlas.model.TracerouteOutput.TracerouteData;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TracerouteDeserializer implements JsonDeserializer<Output> {

	public Output deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject elemjson = json.getAsJsonObject();
		if (elemjson.get("error") != null) {
			ErrorOutput error = new ErrorOutput();
			error.setErrorMessage(elemjson.get("error").getAsString());
			return error;
		}

		TracerouteOutput tr = new TracerouteOutput();
		tr.setHop(elemjson.get("hop").getAsInt());
		for (JsonElement sr : elemjson.get("result").getAsJsonArray()) {
			JsonObject sro = sr.getAsJsonObject();
			TracerouteData data = new TracerouteData();
			if (sro.get("x") == null) {// If nothing is returned, the result
										// will be like "x":"*"
				data.setFrom(sro.get("from").getAsString());
				data.setRoundTripTime(sro.get("rtt").getAsDouble());
				data.setSize(sro.get("size").getAsInt());
				data.setTimeToLive(sro.get("ttl").getAsInt());
				tr.getData().add(data);
			}
		}
		return tr;
	}
}

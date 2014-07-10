package edu.clarkson.cs.gdc.network.common.deserializer;

import edu.clarkson.cs.gdc.network.ripeatlas.model.Probe;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ProbeDeserializer implements JsonDeserializer<Probe> {

	public Probe deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Probe result = new Probe();
		JsonObject obj = json.getAsJsonObject();

		result.setPublicc(obj.get("is_public").getAsBoolean());
		if (result.isPublicc()) {
			result.setAddressV4(obj.get("address_v4").getAsString());
			result.setAddressV6(obj.get("address_v6").getAsString());
			result.setPrefixV4(obj.get("prefix_v4").getAsString());
			result.setPrefixV6(obj.get("prefix_v6").getAsString());
		}
		result.setAnchor(obj.get("is_anchor").getAsBoolean());
		result.setAsnV4(obj.get("asn_v4").getAsInt());
		result.setAsnV6(obj.get("asn_v6").getAsInt());
		result.setCountryCode(obj.get("country_code").getAsString());
		result.setId(obj.get("id").getAsInt());
		result.setLatitude(obj.get("latitude").getAsDouble());
		result.setLongitude(obj.get("longitude").getAsDouble());
		result.setStatus(obj.get("status").getAsInt());
		result.setStatusSince(new Date(obj.get("status_since").getAsLong()));

		return result;
	}
}

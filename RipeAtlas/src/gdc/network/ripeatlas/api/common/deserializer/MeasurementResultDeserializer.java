package gdc.network.ripeatlas.api.common.deserializer;

import gdc.network.ripeatlas.api.common.MeasurementType;
import gdc.network.ripeatlas.model.MeasurementResult;
import gdc.network.ripeatlas.model.Output;
import gdc.network.ripeatlas.model.TracerouteOutput;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class MeasurementResultDeserializer implements
		JsonDeserializer<MeasurementResult> {

	public MeasurementResult deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = (JsonObject) json;

		MeasurementResult mresult = new MeasurementResult();

		mresult.setAf(jsonObject.get("af").getAsInt());
		mresult.setDstAddr(jsonObject.get("dst_addr").getAsString());
		mresult.setDstName(jsonObject.get("dst_name").getAsString());
		mresult.setEndTime(new Date(jsonObject.get("endtime").getAsLong()));
		mresult.setFw(jsonObject.get("fw").getAsInt());
		mresult.setMeasurementId(jsonObject.get("msm_id").getAsInt());
		mresult.setProbeId(jsonObject.get("prb_id").getAsInt());
		mresult.setProtocol(jsonObject.get("proto").getAsString());
		mresult.setSize(jsonObject.get("size").getAsInt());
		mresult.setTimestamp(new Date(
				jsonObject.get("timestamp").getAsLong() * 1000));

		String type = jsonObject.get("type").getAsString();
		mresult.setType(type);

		MeasurementType mt = MeasurementType.valueOf(type);
		JsonElement result = jsonObject.get("result");

		for (JsonElement elem : result.getAsJsonArray()) {
			switch (mt) {
			case traceroute:
				mresult.getOutputs().add(
						(Output) context.deserialize(elem,
								TracerouteOutput.class));
				break;
			default:
				break;
			}
		}
		return mresult;
	}
}

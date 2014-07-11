package edu.clarkson.cs.gdc.network.ripeatlas.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.JsonObject;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.common.Response;
import edu.clarkson.cs.gdc.network.common.ResponseError;

public abstract class RipeAtlasResponse<T> extends Response<T> {

	protected RipeAtlasResponse(HttpResponse response) throws IOException {
		super(response);
	}

	protected ResponseError buildError(int errorCode, InputStream content) {
		JsonObject json = Environment.getEnvironment().getReader()
				.parse(new InputStreamReader(content)).getAsJsonObject()
				.get("error").getAsJsonObject();
		ResponseError error = new ResponseError();
		error.setCode(json.get("code").getAsInt());
		error.setMessage(json.get("message").getAsString());
		logger.warn("Error code:" + error.getCode());
		logger.warn("Error message:" + error.getMessage());
		return error;
	}
}

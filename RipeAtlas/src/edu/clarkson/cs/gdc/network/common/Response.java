package edu.clarkson.cs.gdc.network.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

public abstract class Response<T extends Object> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private ResponseError error;

	protected Response(HttpResponse response) throws IOException {
		super();
		this.statusCode = response.getStatusLine().getStatusCode();

		if (statusCode >= HttpStatus.SC_OK
				&& statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
			result = buildResult(response.getEntity().getContent());
		} else {
			logger.warn(MessageFormat.format("{0} Http Exception", statusCode));
			result = null;
			try {
				error = buildError(statusCode, response.getEntity()
						.getContent());
			} catch (Exception e) {
				error = new ResponseError();
				error.setCode(statusCode);
			}
		}
	}

	protected abstract ResponseError buildError(int errorCode,
			InputStream content);

	protected T buildResult(InputStream content) {
		JsonElement json = Environment.getEnvironment().getReader()
				.parse(new InputStreamReader(content));
		return buildResult(json);
	}

	public static boolean isNull(JsonElement element) {
		return element == null || element.isJsonNull();
	}

	protected abstract T buildResult(JsonElement json);

	private int statusCode;

	private T result;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public T getResult() {
		return result;
	}

	public ResponseError getError() {
		return error;
	}

	public void setError(ResponseError error) {
		this.error = error;
	}

}

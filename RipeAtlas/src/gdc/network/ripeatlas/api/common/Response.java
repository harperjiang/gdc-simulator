package gdc.network.ripeatlas.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import com.google.gson.JsonElement;

public abstract class Response<T extends Object> {

	protected Response(HttpResponse response) throws IOException {
		super();
		this.statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_OK) {
			result = buildResult(response.getEntity().getContent());
		} else {
			throw new IllegalStateException(MessageFormat.format(
					"{0} Http Exception", statusCode));
		}
	}

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

}

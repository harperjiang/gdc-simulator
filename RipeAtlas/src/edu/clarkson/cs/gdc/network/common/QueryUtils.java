package edu.clarkson.cs.gdc.network.common;

public class QueryUtils {

	public static String fields(String[] fieldNames) {
		StringBuilder builder = new StringBuilder();
		builder.append("fields=");
		for (String field : fieldNames) {
			builder.append(field).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public static String queryString(Object... vals) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < vals.length; i += 2) {
			builder.append(vals[i]).append("=")
					.append(String.valueOf(vals[i + 1])).append("&");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}

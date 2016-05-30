package com.ymiyun.fdfs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Json {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object obj) throws Exception {
		return mapper.writeValueAsString(obj);
	}

	public static <T> T parse(String json, Class<T> clazz) throws Exception {
		return mapper.readValue(json, clazz);
	}

	public static <T> T parseArray(String json, TypeReference<T> typeRef) throws Exception {
		return mapper.readValue(json, typeRef);
	}
}

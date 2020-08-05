package com.forleven.backenddevelopertest.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public enum ExistsContext {
	ALREADY_EXISTS("ALREADY_EXISTS", "already exists", HttpStatus.CONFLICT),
	NOT_FOUND("NOT_FOUND", "does not exist", HttpStatus.NOT_FOUND);
	
	private static final Map<String, ExistsContext> BY_LABEL = new HashMap<>();
	
	static {
		for (ExistsContext ctx : values()) {
			BY_LABEL.put(ctx.label, ctx);
		}
	}
	
	public final String label;
	public final String message;
	public final HttpStatus httpStatus;
	
	private ExistsContext(String label, String message, HttpStatus httpStatus) {
		this.label = label;
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	public static ExistsContext getValue(String label) {
		return BY_LABEL.get(label);
	}
}

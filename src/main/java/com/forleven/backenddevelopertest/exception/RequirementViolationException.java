package com.forleven.backenddevelopertest.exception;

import java.util.HashMap;
import java.util.Map;

public class RequirementViolationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Map<String, String>> violatedRequirements = new HashMap<>();
	
	public RequirementViolationException() { }
	
	public RequirementViolationException(String message) {
		super(message);
	}
	
	public RequirementViolationException(String violatedRequirement, String field, String detail) {
		addDetail(violatedRequirement, field, detail);
	}
	
	public Map<String, Map<String, String>> getViolatedRequirements() {
		return violatedRequirements;
	}
	
	public Map<String, String> getDetails(String violatedRequirement) {
		if (isRequirementViolated(violatedRequirement)) {
			return violatedRequirements.get(violatedRequirement);
		}
		return null;
	}
	
	public void addDetail(String violatedRequirement, String field, String detail) {
		if (!isRequirementViolated(violatedRequirement)) {
			violatedRequirements.put(violatedRequirement, new HashMap<>());
		}
		violatedRequirements.get(violatedRequirement).put(field, detail);
	}
	
	public boolean isRequirementViolated(String requirement) {
		return violatedRequirements.containsKey(requirement);
	}
}

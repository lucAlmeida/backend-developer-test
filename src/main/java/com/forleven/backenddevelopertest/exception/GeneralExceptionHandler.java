package com.forleven.backenddevelopertest.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.forleven.backenddevelopertest.util.ExistsContext;
import com.google.common.base.CaseFormat;

@ControllerAdvice
public class GeneralExceptionHandler {

	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<Map<String, TreeSet<String>>> handleValidationException(ConstraintViolationException exc) {
		Map<String, TreeSet<String>> violations = new TreeMap<>();
		exc.getConstraintViolations().forEach(violation -> {
			String propertyPath = String.valueOf(violation.getPropertyPath());
			String fieldName = (propertyPath.split("\\.").length > 1 ? propertyPath.split("\\.")[1] : propertyPath);
			String violationMessage = violation.getMessage();
			if (!violations.containsKey(fieldName)) {
				violations.put(fieldName, new TreeSet<>());
			}
			violations.get(fieldName).add(violationMessage);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(violations);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<Map<String, TreeSet<String>>> handleValidationException(MethodArgumentNotValidException exc) {
		Map<String, TreeSet<String>> errors = new TreeMap<>();
		exc.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			if (!errors.containsKey(fieldName)) {
				errors.put(fieldName, new TreeSet<>());
			}
			errors.get(fieldName).add(errorMessage);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler({HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<String> handleInvalidRequestException(Exception exc) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
	}
	
	@ExceptionHandler({JsonParseException.class})
	public ResponseEntity<String> handleJsonParseException(JsonParseException exc) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
	}

	@ExceptionHandler({RequirementViolationException.class})
	public ResponseEntity<Map<String, TreeMap<String, List<String>>>> handleBusinessRuleViolationException(RequirementViolationException exc) {
		Map<String, TreeMap<String, List<String>>> errors = new TreeMap<>();
		exc.getViolatedRequirements().forEach((requirement, details) -> {
			String req = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, requirement);
			if (!errors.containsKey(req)) {
				errors.put(req, new TreeMap<>());
			}
			details.forEach((field, detail) -> {
				if (!errors.get(req).containsKey(field)) {
					errors.get(req).put(field, new ArrayList<>());
				}
				errors.get(req).get(field).add(detail);
			});
		});
		if (exc.getViolatedRequirements().containsKey(ExistsContext.NOT_FOUND.label)) {
			return ResponseEntity.status(ExistsContext.NOT_FOUND.httpStatus).body(errors);
		}
		if (exc.getViolatedRequirements().containsKey(ExistsContext.ALREADY_EXISTS.label)) {
			return ResponseEntity.status(ExistsContext.ALREADY_EXISTS.httpStatus).body(errors);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

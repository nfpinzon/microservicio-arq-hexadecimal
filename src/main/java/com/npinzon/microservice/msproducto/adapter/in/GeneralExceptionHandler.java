package com.npinzon.microservice.msproducto.adapter.in;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String ACCESS_DENIED = "Access denied!";
	public static final String INVALID_REQUEST = "Invalid request";
	public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n requested uri: %s";
	public static final String LIST_JOIN_DELIMITER = ",";
	public static final String FIELD_ERROR_SEPARATOR = ": ";
	private static final Logger local_logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
	private static final String ERRORS_FOR_PATH = "errors {} for path {}";
	private static final String PATH = "path";
	private static final String ERRORS = "error";
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	private static final String TIMESTAMP = "timestamp";
	private static final String TYPE = "type";


	/**
	 * A general handler for all uncaught exceptions
	 */
	@ExceptionHandler({ Exception.class,  })
	public Mono<ResponseEntity<Object>> handleAllExceptions(Exception exception) {
		local_logger.info("Entro por el general");
		ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
		final HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
		final String localizedMessage = exception.getLocalizedMessage();
		final String path = "";//---request.getDescription(false);
		String message = (localizedMessage.isEmpty()==false ? localizedMessage : status.getReasonPhrase());
		logger.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
		return getExceptionResponseEntity(exception, status, path, Collections.singletonList(message));
	}

	/**
	 * Build a detailed information about the exception in the response
	 */
	private Mono<ResponseEntity<Object>> getExceptionResponseEntity(final Exception exception, final HttpStatus status,
			final String path, final List<String> errors) {
		final Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIMESTAMP, Instant.now());
		body.put(STATUS, status.value());
		body.put(ERRORS, errors);
		body.put(TYPE, exception.getClass().getSimpleName());
		body.put(PATH, path);
		body.put(MESSAGE, getMessageForStatus(status));
		 final String errorsMessage = errors.isEmpty()==false ? errors.stream().filter(cadena->!cadena.isEmpty()).collect(Collectors.joining(LIST_JOIN_DELIMITER))
		 		: status.getReasonPhrase();
		local_logger.error(ERRORS_FOR_PATH, errorsMessage, path);
		return Mono.just(new ResponseEntity<>(body, status));
	}
	
	@ExceptionHandler({ DataAccessResourceFailureException.class })
	public Mono<ResponseEntity<Object>> handleDataAccessResourceFailureException(DataAccessResourceFailureException exception) {
		ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
		final HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.CONFLICT;
		final String localizedMessage = exception.getLocalizedMessage();
		final String path = "";//request.getDescription(false);
		String message = (localizedMessage.isEmpty()==false ? localizedMessage : status.getReasonPhrase());
		logger.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
		return getExceptionResponseEntity(exception, status, path, Collections.singletonList(message));
	}
	
	@Override
	protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex,
			HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {

				final Map<String, Object> body = new LinkedHashMap<>();
				final String path = exchange.getRequest().getPath().toString();
				body.put(TIMESTAMP, Instant.now());
				body.put(STATUS, status.value());
				body.put(ERRORS, ex.getFieldErrors().stream().map(e-> e.getField() +" "+ e.getDefaultMessage()).toList());
				body.put(TYPE, ex.getObjectName());
				body.put(PATH, path);
				body.put(MESSAGE, status);
				 final String errorsMessage = ex.getFieldErrors().stream().map(e-> e.getField() +" "+ e.getDefaultMessage()).toList().stream().collect(Collectors.joining(LIST_JOIN_DELIMITER));
				local_logger.error(ERRORS_FOR_PATH, errorsMessage, path);
				return Mono.just(new ResponseEntity<>(body, status));


	}

	private String getMessageForStatus(HttpStatus status) {
		switch (status) {
		case UNAUTHORIZED:
			return ACCESS_DENIED;
		case BAD_REQUEST:
			return INVALID_REQUEST;
		default:
			return status.getReasonPhrase();
		}
	}
}

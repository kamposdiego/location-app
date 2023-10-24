package br.com.morsesystems.location.application.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ErrorHandlerControllerAdvice {

    @ExceptionHandler(value = ConversionFailedException.class)
    public ProblemDetail handleConversionFailedException(ConversionFailedException conversionFailedException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, conversionFailedException.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException runtimeException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, runtimeException.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, httpMessageNotReadableException.getLocalizedMessage());
        problemDetail.setTitle("Error processing request");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = InvalidFilterParameterException.class)
    public ProblemDetail handleInvalidFilterParameterException(InvalidFilterParameterException invalidFilterParameterException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, invalidFilterParameterException.getLocalizedMessage());
        problemDetail.setTitle("Error on filter processing");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingServletRequestParameterException(MissingServletRequestParameterException missingServletRequestParameterException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, missingServletRequestParameterException.getLocalizedMessage());
        problemDetail.setTitle("Error processing request");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException notFoundException){
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, notFoundException.getMessage());
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ProblemDetail handleEmptyResultDataAccessException(EmptyResultDataAccessException emptyResultDataAccessException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, emptyResultDataAccessException.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(METHOD_NOT_ALLOWED, httpRequestMethodNotSupportedException.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = CountryRequestProcessedException.class)
    public ProblemDetail handleCountryRequestProcessedException(CountryRequestProcessedException countryRequestProcessedException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, countryRequestProcessedException.getLocalizedMessage());
        problemDetail.setTitle("Country request has already been processed");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = BrazilianStateRequestProcessedException.class)
    public ProblemDetail handleCountryRequestProcessedException(BrazilianStateRequestProcessedException brazilianStateRequestProcessedException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, brazilianStateRequestProcessedException.getLocalizedMessage());
        problemDetail.setTitle("Brazilian State request has already been processed");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException){

        String specificMessageError = NestedExceptionUtils.getMostSpecificCause(dataIntegrityViolationException).getMessage();

        Pattern patternSQLError = Pattern.compile("Detail: (.*)");
        Matcher matcher = patternSQLError.matcher(specificMessageError);

        if (matcher.find()) {
            specificMessageError = matcher.group(1);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, specificMessageError);
        problemDetail.setTitle("Data integrity violation");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException constraintViolationException){
        List<String> errors = new ArrayList<>();

        for(ConstraintViolation<?> violationException : constraintViolationException.getConstraintViolations()){
            errors.add(violationException.getPropertyPath() + ": " + violationException.getMessage());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatus(UNPROCESSABLE_ENTITY);
        problemDetail.setDetail(errors.toString());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
        String parameterName = methodArgumentTypeMismatchException.getName();
        Class<?> parameterType = methodArgumentTypeMismatchException.getRequiredType();
        if(parameterType != null){
            parameterName += ":deve ser " + parameterType.getSimpleName();
        }

        ProblemDetail problemDetail = ProblemDetail.forStatus(UNPROCESSABLE_ENTITY);
        problemDetail.setDetail(Arrays.asList(parameterName).toString());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){

        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        var typeErrors = bindingResult.getGlobalErrors();

        List<String> fieldErrorDtss = fieldErrors.stream()
                .map(fieldError -> fieldError.getField().concat(":").concat(fieldError.getDefaultMessage()))
                .map(String::new)
                .toList();

        List<String> typeErrorDtss = typeErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(String::new)
                .toList();

        List<String> allErrors = Stream.concat(fieldErrorDtss.stream(), typeErrorDtss.stream())
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(UNPROCESSABLE_ENTITY);
        problemDetail.setDetail(allErrors.toString());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = RequestNotPermitted.class)
    public ProblemDetail handleRequestNotPermitted(RequestNotPermitted requestNotPermitted){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(TOO_MANY_REQUESTS, requestNotPermitted.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(value = Exception.class)
    public ProblemDetail handleException(Exception exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        problemDetail.setTitle("Error processing request");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

}


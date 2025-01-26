package com.chatting.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNotFound(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.NOT_FOUND.value();
        String message = "해당 경로를 찾을 수 없습니다.";
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
        String message = "해당 요청을 처리 할 수 없습니다.";
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    // 값 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handlerMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();

        String parameterName = ex.getParameterName();
        String message = MessageFormat.format("{0}의 값이 누락되었습니다.", parameterName);

        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    // 중복 예외
    @ExceptionHandler(DuplicateException.class)
    public void handleDuplicateException(DuplicateException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        String message = ex.getMessage();
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    // 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> message = fieldErrors.stream()
                .map(error -> error.getField() + ", " + error.getDefaultMessage())
                .toList();

        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message.toString());
    }

    // 데이터가 없음
    @ExceptionHandler(NotFoundDataException.class)
    public void handleNotFoundDataException(NotFoundDataException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        String message = ex.getMessage();
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handlerDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = ex.getMessage();
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        String message = ex.getMessage();
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }

    // 모든 예외
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletRequest request, HttpServletResponse response){
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = ex.getMessage();
        ExceptionResponseSender.createExceptionResponse(httpStatus, request, response, message);
    }
}


package com.bt.marketplace.cart.exception;

import com.bt.marketplace.cart.constants.ErrorMessagesImpl;
import com.bt.marketplace.common.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionalHandlerForManagement {
    @Autowired ErrorFormatter errorFormatter;

    @Autowired ErrorMessages errorMessageService;

    @ExceptionHandler(ApiErrorResponseException.class)
    protected ResponseEntity<ApiErrorResponseWrapper> handleGenericException(
        ApiErrorResponseException ex) {
        if (!ObjectUtils.isEmpty(ex.getApiErrorResponse())
            && StringUtils.isEmpty(ex.getApiErrorResponse().getResponse().getStatus())
            && ex.isIncludeFailure()) {
            ex.getApiErrorResponse().getResponse().setStatus("FAILURE");
        }
        return ResponseEntity.status(ex.getHttpStatusCode()).body(ex.getApiErrorResponse());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseWrapper> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {

        ResponseEntity<ApiErrorResponseWrapper> errorResponse = null;
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
            Object invalidValue = invalidFormatException.getValue();
//
            String errorKey  = ErrorMessages.GENERIC_USER_MESSAGE;

            errorResponse = ResponseEntity.status(BAD_REQUEST)
                .body(new ApiErrorResponseWrapper(ApiErrorResponse.builder()
                    .status(("FAILURE"))
                    .errors(List.of(errorMessageService.getApiErrorByKeyAndArg(errorKey, invalidValue))).build()));
        } else {
            errorResponse = ResponseEntity.status(BAD_REQUEST)
                .body(new ApiErrorResponseWrapper(ApiErrorResponse.builder()
                    .status(("FAILURE"))
                    .errors(List.of(errorMessageService.getApiErrorByKey("GENERIC ERROR")))
                    .build()));
        }
        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseWrapper> handleConstraintViolationException(
        ConstraintViolationException e) {
        List<ApiError> apiErrors = new ArrayList<>();
        ApiError apiError =ErrorMessagesImpl.getApiError(e.getMessage());
        apiErrors.add(apiError);
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ApiErrorResponseWrapper(
                ApiErrorResponse.builder().status(("FAILURE"))
                    .errors(apiErrors).build()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiErrorResponseWrapper> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e) {



        List<ApiError> apiErrors = new ArrayList<>();
        ApiError apiError =
            ErrorMessagesImpl.getApiError(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        apiErrors.add(apiError);
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ApiErrorResponseWrapper(
                ApiErrorResponse.builder().status(("FAILURE"))
                    .errors(apiErrors).build()));
    }


}

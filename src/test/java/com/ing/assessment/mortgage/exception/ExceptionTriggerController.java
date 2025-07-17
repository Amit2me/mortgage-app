package com.ing.assessment.mortgage.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestController
@RequestMapping("/test/exception")
class ExceptionTriggerController {

    @GetMapping("/constraint")
    public void throwConstraint() {
        throw new jakarta.validation.ConstraintViolationException("constraint error", java.util.Collections.emptySet());
    }

    @GetMapping("/missing-param")
    public void throwMissingParam() throws MissingServletRequestParameterException {
        throw new org.springframework.web.bind.MissingServletRequestParameterException("foo", "String");
    }

    @GetMapping("/notfound")
    public void throwNotFound() throws NoResourceFoundException {
        throw new org.springframework.web.servlet.resource.NoResourceFoundException(HttpMethod.GET, null);
    }

    @GetMapping("/method-not-allowed")
    public void throwMethodNotAllowed() throws HttpRequestMethodNotSupportedException {
        throw new org.springframework.web.HttpRequestMethodNotSupportedException("PUT");
    }

    @GetMapping("/media-type")
    public void throwMediaType() throws HttpMediaTypeNotSupportedException {
        throw new org.springframework.web.HttpMediaTypeNotSupportedException(MediaType.TEXT_PLAIN, java.util.Collections.emptyList());
    }

    @GetMapping("/illegal-arg")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("business rule violated");
    }

    @GetMapping("/other")
    public void throwOther() {
        throw new RuntimeException("unexpected!");
    }
}

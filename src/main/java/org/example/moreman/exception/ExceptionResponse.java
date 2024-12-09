package org.example.moreman.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public record ExceptionResponse(
        HttpStatus httpStatus,
        String exceptionClassName,
        String message) {


    public static ExceptionResponseBuilder builder() {
        return new ExceptionResponseBuilder();
    }

    public static class ExceptionResponseBuilder {
        private HttpStatus httpStatus;
        private String exceptionClassName;
        private String message;

        public ExceptionResponseBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ExceptionResponseBuilder exceptionClassName(String exceptionClassName) {
            this.exceptionClassName = exceptionClassName;
            return this;
        }

        public ExceptionResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ExceptionResponse build() {
            return new ExceptionResponse(httpStatus, exceptionClassName, message);
        }
    }
}

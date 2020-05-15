package com.pbs.middleware.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The [MiddlewareExceptionResponse] renders an error as per specification, see section "6.3 Error Handling"
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private List<Error> errors;

    public static ErrorResponse of(Error error) {
        return new ErrorResponse(List.of(error));
    }

    public static ErrorResponse of(List<Error> errors) {
        return new ErrorResponse(errors);
    }

}

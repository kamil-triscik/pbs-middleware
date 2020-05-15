package cz.muni.ll.middleware.client.rest.exceptions;

import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.BadRequestException;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.ConflictException;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.ForbiddenException;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.MethodNotAllowedException;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.NonAcceptableRequestException;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.NotFoundException;
import cz.muni.ll.middleware.client.rest.exceptions.recoverable.ServerException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static cz.muni.ll.middleware.client.rest.exceptions.Utils.getResponseBody;
import static feign.FeignException.errorStatus;

public class LLMClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new BadRequestException(getResponseBody(response));
        }
        if (response.status() == 401 || response.status() == 403) {
            return new ForbiddenException(getResponseBody(response));
        }
        if (response.status() == 404) {
            return new NotFoundException(getResponseBody(response));
        }
        if (response.status() == 405) {
            return new MethodNotAllowedException(getResponseBody(response));
        }
        if (response.status() == 406) {
            return new NonAcceptableRequestException(getResponseBody(response));
        }
        if (response.status() == 409) {
            return new ConflictException(getResponseBody(response));
        }
        if (response.status() >= 500) {
            return new ServerException(getResponseBody(response));
        }
        return errorStatus(methodKey, response);
    }
}

package cz.muni.ll.middleware.client.download.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ll.middleware.client.download.domain.FileContent;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.NonRecoverableException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class DownloadDecoder implements Decoder {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        byte[] data = IOUtils.toByteArray(response.body().asInputStream());
        if (isJson(response)) {
            try {
                return mapper.readValue(new String(data), getClass(response.request().requestTemplate().methodMetadata().returnType()));
            } catch (ClassNotFoundException e) {
                throw new NonRecoverableException(e);
            }
        } else {
            String url[] = response.request().url().split("/");
            return new FileContent(data, url[url.length - 1]);
        }
    }

    private boolean isJson(Response response) {
        Collection<String> acceptHeader = response.request().headers().get("Accept");
        return acceptHeader == null || acceptHeader.contains("application/json");
    }

    private Class<?> getClass(Type type) throws ClassNotFoundException {
        if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(List.class)) {
            return ArrayList.class;

        } else {
            return Class.forName(((Class) type).getName());
        }
    }

}

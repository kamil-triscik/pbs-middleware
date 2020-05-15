package cz.muni.ll.middleware.client.rest.exceptions;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.gson.GsonEncoder;
import java.lang.reflect.Type;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class LLMClientEncoder implements Encoder {

    private final GsonEncoder jsonEncode = new GsonEncoder();

    private final FormEncoder formEncode = new FormEncoder();

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (isJson(template)) {
            jsonEncode.encode(object, bodyType, template);
        } else {
            formEncode.encode(object, bodyType, template);
        }
    }

    private boolean isJson(RequestTemplate requestTemplate) {
        return requestTemplate.headers().get("Content-type").contains(APPLICATION_JSON_VALUE);
    }
}

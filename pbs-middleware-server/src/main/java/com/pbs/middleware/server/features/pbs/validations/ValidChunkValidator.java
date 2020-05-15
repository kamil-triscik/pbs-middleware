package com.pbs.middleware.server.features.pbs.validations;

import com.pbs.middleware.api.template.Chunk;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("PMD.UselessParentheses")
@RequiredArgsConstructor
public class ValidChunkValidator implements ConstraintValidator<ValidChunk, Chunk> {

    @Override
    public boolean isValid(Chunk value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.getCount() != null || value.getNcpus() != null || (value.getMem() != null && isNotBlank(value.getMem())) ||
                (value.getCustomResources() != null && !value.getCustomResources().isEmpty()
                        && value.getCustomResources().keySet().stream()
                        .filter(Objects::nonNull)
                        .filter(StringUtils::isNotBlank)
                        .filter(key -> Objects.nonNull(value.getCustomResources().get(key)))
                        .anyMatch(key -> isNotBlank(value.getCustomResources().get(key))));

    }
}

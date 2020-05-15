package com.pbs.middleware.server.features.pbs.validations;

import com.pbs.middleware.api.template.Chunk;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueChunksValidator implements ConstraintValidator<UniqueChunks, List<Chunk>> {

    @Override
    public boolean isValid(List<Chunk> value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || value.stream().map(Chunk::getId).distinct().count() == value.size();
    }
}

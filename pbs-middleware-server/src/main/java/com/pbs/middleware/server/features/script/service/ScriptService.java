package com.pbs.middleware.server.features.script.service;

import com.google.common.collect.ImmutableList;
import com.pbs.middleware.server.features.script.repository.Script;
import com.pbs.middleware.server.features.script.repository.ScriptRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class ScriptService {

    @Value("${middleware.server.features.script.template.path:#{null}}")
    private String scriptTemplatePath;

    @Value("${middleware.server.features.script.template.replace:#{null}}")
    private String scriptTemplateReplace;

    public Script create(@Valid @NonNull Script script) {
        return scriptRepository.save(script);
    }

    @PostConstruct
    private void checkTemplate() throws IOException {
        if (scriptTemplatePath != null) {
            if (Files.notExists(Path.of(scriptTemplatePath))) {
                throw new IllegalStateException("Script template [" + scriptTemplatePath + "] does not exists");
            }

            if (!Files.readString(Path.of(scriptTemplatePath)).contains(scriptTemplateReplace)) {
                throw new IllegalStateException("Script template does not contain replace area string \"" + scriptTemplatePath + "\"");
            }
        }
    }

    @NonNull
    private final ScriptRepository scriptRepository;

    public List<Script> getAll() {
        return ImmutableList.copyOf(scriptRepository.findAll());
    }

    public Script get(@NonNull UUID uuid) {
        return scriptRepository.findById(uuid).orElseThrow(() -> ScriptNotFoundException.of(uuid));
    }

    public Script get(@NonNull String name) {
        return findByName(name).orElseThrow(() -> ScriptNotFoundException.of(name));
    }

    public Optional<Script> findByName(@NonNull String name) {
        return scriptRepository.findByName(name);
    }

    public String getFullScript(@NonNull UUID id) throws IOException {
        return loadTemplate().replace(scriptTemplateReplace, get(id).getCode());
    }

    @SneakyThrows
    public Script getFullScript(@NonNull Script script) {
        if (scriptTemplatePath == null) {
            return script;
        }
        String fullGroovyScript = loadTemplate().replace(scriptTemplateReplace, script.getCode());
        return new Script() {{
            setId(script.getId());
            setDescription(script.getDescription());
            setName(script.getName());
            setCode(fullGroovyScript);
        }};
    }


    public Script update(@Valid @NonNull UUID id, @Valid @NonNull Script script) {
        get(id);
        return scriptRepository.save(script);
    }

    public void delete(UUID domainId) {
        scriptRepository.deleteById(domainId);
    }

    public String loadTemplate() throws IOException {
        return Files.readString(Path.of(scriptTemplatePath));
    }

}

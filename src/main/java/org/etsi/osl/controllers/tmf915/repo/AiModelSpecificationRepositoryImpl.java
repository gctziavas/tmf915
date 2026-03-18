package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AiModelSpecificationRepositoryImpl implements AiModelSpecificationRepository {

    private final ConcurrentHashMap<String, AiModelSpecification> store = new ConcurrentHashMap<>();

    @Override
    public Optional<AiModelSpecification> findByUuid(String uuid) {
        return Optional.ofNullable(store.get(uuid));
    }

    @Override
    public Optional<AiModelSpecification> findByNameAndVersion(String name, String version) {
        return store.values().stream()
                .filter(s -> name.equals(s.getName()) && version.equals(s.getVersion()))
                .findFirst();
    }

    @Override
    public Optional<AiModelSpecification> findByName(String name) {
        return store.values().stream()
                .filter(s -> name.equals(s.getName()))
                .findFirst();
    }

    @Override
    public List<AiModelSpecification> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public AiModelSpecification save(AiModelSpecification spec) {
        store.put(spec.getId(), spec);
        return spec;
    }

    @Override
    public void delete(AiModelSpecification spec) {
        store.remove(spec.getId());
    }
}

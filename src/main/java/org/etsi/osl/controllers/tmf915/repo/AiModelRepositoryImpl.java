package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class AiModelRepositoryImpl implements AiModelRepository {

    private final ConcurrentHashMap<String, AiModel> store = new ConcurrentHashMap<>();

    @Override
    public Optional<AiModel> findByUuid(String uuid) {
        return Optional.ofNullable(store.get(uuid));
    }

    @Override
    public List<AiModel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public AiModel save(AiModel model) {
        store.put(model.getId(), model);
        return model;
    }

    @Override
    public void delete(AiModel model) {
        store.remove(model.getId());
    }

    @Override
    public List<AiModel> findByNameStartingWith(String namePrefix) {
        return store.values().stream()
                .filter(m -> m.getName() != null && m.getName().startsWith(namePrefix))
                .collect(Collectors.toList());
    }

    @Override
    public List<AiModel> findByState(ServiceStateType state) {
        return store.values().stream()
                .filter(m -> state.equals(m.getState()))
                .collect(Collectors.toList());
    }
}

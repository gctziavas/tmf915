package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;

import java.util.List;
import java.util.Optional;

public interface AiModelRepository {

    Optional<AiModel> findByUuid(String uuid);

    List<AiModel> findAll();

    AiModel save(AiModel model);

    void delete(AiModel model);

    List<AiModel> findByNameStartingWith(String namePrefix);

    List<AiModel> findByState(ServiceStateType state);
}

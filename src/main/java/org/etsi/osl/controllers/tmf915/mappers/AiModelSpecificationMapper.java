package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;

public final class AiModelSpecificationMapper {

    private AiModelSpecificationMapper() {}

    public static AiModelSpecification fromCreate(AiModelSpecificationCreate src) {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setDeploymentRecord(src.getDeploymentRecord());
        spec.setDescription(src.getDescription());
        spec.setInheritedModel(src.getInheritedModel());
        spec.setIsBundle(src.getIsBundle());
        spec.setLastUpdate(src.getLastUpdate());
        spec.setLifecycleStatus(src.getLifecycleStatus());
        spec.setModelContractVersionHistory(src.getModelContractVersionHistory());
        spec.setModelDataSheet(src.getModelDataSheet());
        spec.setModelEvaluationData(src.getModelEvaluationData());
        spec.setModelSpecificationHistory(src.getModelSpecificationHistory());
        spec.setModelTrainingData(src.getModelTrainingData());
        spec.setName(src.getName());
        spec.setVersion(src.getVersion());
        spec.setAttachment(src.getAttachment());
        spec.setConstraint(src.getConstraint());
        spec.setEntitySpecRelationship(src.getEntitySpecRelationship());
        spec.setFeatureSpecification(src.getFeatureSpecification());
        spec.setRelatedParty(src.getRelatedParty());
        spec.setResourceSpecification(src.getResourceSpecification());
        spec.setServiceLevelSpecification(src.getServiceLevelSpecification());
        spec.setServiceSpecRelationship(src.getServiceSpecRelationship());
        spec.setSpecCharacteristic(src.getSpecCharacteristic());
        return spec;
    }

    public static void applyUpdate(AiModelSpecification target, AiModelSpecificationUpdate src) {
        if (src.getDeploymentRecord() != null)              target.setDeploymentRecord(src.getDeploymentRecord());
        if (src.getDescription() != null)                   target.setDescription(src.getDescription());
        if (src.getInheritedModel() != null)                target.setInheritedModel(src.getInheritedModel());
        if (src.getIsBundle() != null)                      target.setIsBundle(src.getIsBundle());
        if (src.getLifecycleStatus() != null)               target.setLifecycleStatus(src.getLifecycleStatus());
        if (src.getModelContractVersionHistory() != null)   target.setModelContractVersionHistory(src.getModelContractVersionHistory());
        if (src.getModelDataSheet() != null)                target.setModelDataSheet(src.getModelDataSheet());
        if (src.getModelEvaluationData() != null)           target.setModelEvaluationData(src.getModelEvaluationData());
        if (src.getModelSpecificationHistory() != null)     target.setModelSpecificationHistory(src.getModelSpecificationHistory());
        if (src.getModelTrainingData() != null)             target.setModelTrainingData(src.getModelTrainingData());
        if (src.getName() != null)                          target.setName(src.getName());
        if (src.getVersion() != null)                       target.setVersion(src.getVersion());
        if (src.getAttachment() != null)                    target.setAttachment(src.getAttachment());
        if (src.getConstraint() != null)                    target.setConstraint(src.getConstraint());
        if (src.getEntitySpecRelationship() != null)        target.setEntitySpecRelationship(src.getEntitySpecRelationship());
        if (src.getFeatureSpecification() != null)          target.setFeatureSpecification(src.getFeatureSpecification());
        if (src.getRelatedParty() != null)                  target.setRelatedParty(src.getRelatedParty());
        if (src.getResourceSpecification() != null)         target.setResourceSpecification(src.getResourceSpecification());
        if (src.getServiceLevelSpecification() != null)     target.setServiceLevelSpecification(src.getServiceLevelSpecification());
        if (src.getServiceSpecRelationship() != null)       target.setServiceSpecRelationship(src.getServiceSpecRelationship());
        if (src.getSpecCharacteristic() != null)            mergeSpecCharacteristics(target, src.getSpecCharacteristic());
    }

    private static void mergeSpecCharacteristics(AiModelSpecification target, java.util.List<org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification> incoming) {
        java.util.List<org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification> current = target.getSpecCharacteristic();
        if (current == null) {
            current = new java.util.ArrayList<>();
            target.setSpecCharacteristic(current);
        }

        for (org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification candidate : incoming) {
            if (candidate == null || candidate.getName() == null) {
                continue;
            }

            int existingIdx = -1;
            for (int i = 0; i < current.size(); i++) {
                if (candidate.getName().equals(current.get(i).getName())) {
                    existingIdx = i;
                    break;
                }
            }

            if (existingIdx >= 0) {
                // Keep the same collection instance to avoid JPA orphan-removal issues.
                current.set(existingIdx, candidate);
            } else {
                current.add(candidate);
            }
        }
    }
}

package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;

public final class AiModelMapper {

    private AiModelMapper() {}

    public static AiModel fromCreate(AiModelCreate src) {
        AiModel model = new AiModel();
        model.setCategory(src.getCategory());
        model.setDescription(src.getDescription());
        model.setEndDate(src.getEndDate());
        model.setHasStarted(src.getHasStarted());
        model.setIsBundle(src.getIsBundle());
        model.setIsServiceEnabled(src.getIsServiceEnabled());
        model.setIsStateful(src.getIsStateful());
        model.setName(src.getName());
        model.setServiceDate(src.getServiceDate());
        model.setServiceType(src.getServiceType());
        model.setStartDate(src.getStartDate());
        model.setStartMode(src.getStartMode());
        model.setAiModelSpecification(src.getAiModelSpecification());
        model.setFeature(src.getFeature());
        model.setGpu(src.getGpu());
        model.setNote(src.getNote());
        model.setPlace(src.getPlace());
        model.setRelatedEntity(src.getRelatedEntity());
        model.setRelatedParty(src.getRelatedParty());
        model.setServiceCharacteristic(src.getServiceCharacteristic());
        model.setServiceOrderItem(src.getServiceOrderItem());
        model.setServiceRelationship(src.getServiceRelationship());
        model.setServiceSpecification(src.getServiceSpecification());
        model.setSoftware(src.getSoftware());
        model.setState(src.getState());
        model.setSupportingResource(src.getSupportingResource());
        model.setSupportingService(src.getSupportingService());
        model.setTrainingData(src.getTrainingData());
        return model;
    }

    public static void applyUpdate(AiModel target, AiModelUpdate src) {
        if (src.getCategory() != null)              target.setCategory(src.getCategory());
        if (src.getDescription() != null)           target.setDescription(src.getDescription());
        if (src.getEndDate() != null)               target.setEndDate(src.getEndDate());
        if (src.getHasStarted() != null)            target.setHasStarted(src.getHasStarted());
        if (src.getIsBundle() != null)              target.setIsBundle(src.getIsBundle());
        if (src.getIsServiceEnabled() != null)      target.setIsServiceEnabled(src.getIsServiceEnabled());
        if (src.getIsStateful() != null)            target.setIsStateful(src.getIsStateful());
        if (src.getName() != null)                  target.setName(src.getName());
        if (src.getServiceType() != null)           target.setServiceType(src.getServiceType());
        if (src.getStartDate() != null)             target.setStartDate(src.getStartDate());
        if (src.getStartMode() != null)             target.setStartMode(src.getStartMode());
        if (src.getAiModelSpecification() != null)  target.setAiModelSpecification(src.getAiModelSpecification());
        if (src.getFeature() != null)               target.setFeature(src.getFeature());
        if (src.getGpu() != null)                   target.setGpu(src.getGpu());
        if (src.getNote() != null)                  target.setNote(src.getNote());
        if (src.getPlace() != null)                 target.setPlace(src.getPlace());
        if (src.getRelatedEntity() != null)         target.setRelatedEntity(src.getRelatedEntity());
        if (src.getRelatedParty() != null)          target.setRelatedParty(src.getRelatedParty());
        if (src.getServiceCharacteristic() != null) target.setServiceCharacteristic(src.getServiceCharacteristic());
        if (src.getServiceOrderItem() != null)      target.setServiceOrderItem(src.getServiceOrderItem());
        if (src.getServiceRelationship() != null)   target.setServiceRelationship(src.getServiceRelationship());
        if (src.getServiceSpecification() != null)  target.setServiceSpecification(src.getServiceSpecification());
        if (src.getSoftware() != null)              target.setSoftware(src.getSoftware());
        if (src.getState() != null)                 target.setState(src.getState());
        if (src.getSupportingResource() != null)    target.setSupportingResource(src.getSupportingResource());
        if (src.getSupportingService() != null)     target.setSupportingService(src.getSupportingService());
        if (src.getTrainingData() != null)          target.setTrainingData(src.getTrainingData());
    }
}

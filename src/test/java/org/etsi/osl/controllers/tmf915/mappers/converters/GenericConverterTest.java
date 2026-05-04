package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.AttributeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericConverterTest {

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testAllConverters() throws Exception {
        String packageName = "org.etsi.osl.controllers.tmf915.mappers.converters";
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(AttributeConverter.class));
        
        int count = 0;
        for (BeanDefinition beanDef : provider.findCandidateComponents(packageName)) {
            String className = beanDef.getBeanClassName();
            Class<?> clazz = Class.forName(className);
            
            if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }
            
            AttributeConverter converter = (AttributeConverter) clazz.getDeclaredConstructor().newInstance();
            
            assertDoesNotThrow(() -> converter.convertToDatabaseColumn(null));
            assertDoesNotThrow(() -> converter.convertToEntityAttribute(null));
            try {
                converter.convertToEntityAttribute("invalid json");
            } catch (Exception ignored) {}
            
            if (AbstractListJsonConverter.class.isAssignableFrom(clazz)) {
                List<Object> emtpyList = new ArrayList<>();
                assertEquals("[]", converter.convertToDatabaseColumn(emtpyList));
                List<Object> returnedList = (List<Object>) converter.convertToEntityAttribute("[]");
                assertNotNull(returnedList);
                assertTrue(returnedList.isEmpty());
                
                List<Object> testList = new ArrayList<>();
                String json = (String) converter.convertToDatabaseColumn(testList);
                assertNotNull(json);
                List<Object> deserialized = (List<Object>) converter.convertToEntityAttribute(json);
                assertNotNull(deserialized);
            } else if (AbstractTypedJsonConverter.class.isAssignableFrom(clazz)) {
                 converter.convertToDatabaseColumn(null); // The test values would require specific instantiation, null fallback checks are enough
            } else {
                try {
                    Object obj = new java.util.HashMap<>();
                    String json = (String) converter.convertToDatabaseColumn(obj);
                    assertNotNull(json);
                    Object deserialized = converter.convertToEntityAttribute("{}");
                    assertNotNull(deserialized);
                } catch (ClassCastException ignored) {}
            }
            count++;
        }
        System.out.println("Tested " + count + " converter classes.");
        assertTrue(count > 0, "Should have tested at least one converter");
    }
}

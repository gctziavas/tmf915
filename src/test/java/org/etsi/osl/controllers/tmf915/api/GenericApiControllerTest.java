package org.etsi.osl.controllers.tmf915.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class GenericApiControllerTest {

    @Test
    public void testAllGeneratedApiControllers() throws Exception {
        String packageName = "org.etsi.osl.controllers.tmf915.api";
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*ApiController")));
        
        int tested = 0;
        for (BeanDefinition beanDef : provider.findCandidateComponents(packageName)) {
            String className = beanDef.getBeanClassName();
            // Skip the properly implemented ones we tested above
            if (className.contains("AiModelApiController") || className.contains("AiModelSpecificationApiController")) {
                continue;
            }
            
            Class<?> clazz = Class.forName(className);
            if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }
            
            Object controller = null;
            try {
                // Generated controllers have a constructor with Optional delegate
                controller = clazz.getDeclaredConstructors()[0].newInstance(new Object[]{null});
            } catch (Exception e) {
                // If constructor signature is different, fallback to no-args, else skip
                try {
                    controller = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception ignored) {
                    continue;
                }
            }

            if (controller != null) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    // Test all the default methods inherited from the API Interface
                    if (method.getReturnType().equals(ResponseEntity.class) && !method.getName().equals("handleBadRequest")) {
                        Object[] args = new Object[method.getParameterCount()];
                        try {
                            ResponseEntity<?> response = (ResponseEntity<?>) method.invoke(controller, args);
                            if (response != null) { // some might throw depending on arguments but normally they proxy to delegate
                                assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode(), "Expected generated methods to return NOT_IMPLEMENTED");
                            }
                        } catch (Exception ignored) {
                            // Ignored if invalid args
                        }
                    }
                }
                tested++;
            }
        }
        System.out.println("Tested " + tested + " generated API controllers automatically.");
        assertTrue(tested > 0, "Should have tested some generated API controllers");
    }
}

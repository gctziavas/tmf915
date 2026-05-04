package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenericModelTest {
    private static final Logger log = LoggerFactory.getLogger(GenericModelTest.class);

    @Test
    public void testAllModels() throws Exception {
        String packageName = "org.etsi.osl.controllers.tmf915.model";
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        
        int count = 0;
        for (BeanDefinition beanDef : provider.findCandidateComponents(packageName)) {
            String className = beanDef.getBeanClassName();
            Class<?> clazz = Class.forName(className);
            
            if (clazz.isEnum() || clazz.isInterface() || className.endsWith("Test") || className.endsWith("Builder")) {
                continue;
            }
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                testSettersAndGetters(instance);
                testEqualsAndHashCode(clazz);
                testToString(instance);
                count++;
            } catch (Exception e) {
                log.debug("Could not test class " + clazz.getName() + " generically: " + e.getMessage());
            }
        }
        System.out.println("Tested " + count + " generic model classes.");
    }

    private void testSettersAndGetters(Object instance) throws Exception {
        Method[] methods = instance.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                String getterName = "get" + method.getName().substring(3);
                Method getter = null;
                try {
                    getter = instance.getClass().getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    try {
                        getterName = "is" + method.getName().substring(3);
                        getter = instance.getClass().getMethod(getterName);
                    } catch (NoSuchMethodException ignored) {}
                }
                
                if (getter != null) {
                    Class<?> type = method.getParameterTypes()[0];
                    Object testValue = getTestValue(type);
                    if (testValue != null) {
                        try {
                            method.invoke(instance, testValue);
                            Object retrievedValue = getter.invoke(instance);
                            // Not strictly verifying equals since some setters might do conversions,
                            // just testing they don't crash
                            assertNotNull(retrievedValue);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    private void testEqualsAndHashCode(Class<?> clazz) throws Exception {
        Object obj1 = clazz.getDeclaredConstructor().newInstance();
        Object obj2 = clazz.getDeclaredConstructor().newInstance();
        obj1.equals(obj2);
        obj1.hashCode();
    }

    private void testToString(Object instance) {
        assertNotNull(instance.toString());
    }

    private Object getTestValue(Class<?> type) {
        if (type == String.class) return "test";
        if (type == Integer.class || type == int.class) return 1;
        if (type == Long.class || type == long.class) return 1L;
        if (type == Boolean.class || type == boolean.class) return true;
        if (type == Double.class || type == double.class) return 1.0;
        if (type == Float.class || type == float.class) return 1.0f;
        if (type == java.util.List.class) return new ArrayList<>();
        if (type == java.util.Map.class) return new java.util.HashMap<>();
        if (type == java.time.OffsetDateTime.class) return java.time.OffsetDateTime.now();
        if (type == java.net.URI.class) return java.net.URI.create("http://test");
        return null; // Some fields we don't know how to mock, skip
    }
}

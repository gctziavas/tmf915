package org.etsi.osl.controllers.tmf915.api;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * Registers {@link FieldsResponseBodyAdvice} directly on Spring MVC's handler adapter instead of
 * through {@code @ControllerAdvice}, which springdoc 2.6.0 cannot handle on Spring Framework 7
 * (see the advice's Javadoc). Remove this class and annotate the advice with
 * {@code @ControllerAdvice} once springdoc is upgraded to a Spring Boot 4 compatible release.
 */
@Configuration(proxyBeanMethods = false)
public class FieldsResponseBodyAdviceConfiguration {

    @Bean
    static BeanPostProcessor fieldsResponseBodyAdviceRegistrar(ObjectProvider<JsonMapper> jsonMapper) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                if (bean instanceof RequestMappingHandlerAdapter adapter) {
                    // Runs before afterPropertiesSet(), so @ControllerAdvice beans are still added
                    // after these. JsonViewResponseBodyAdvice is what Spring MVC registers here by
                    // default when Jackson is present; setResponseBodyAdvice replaces the list.
                    adapter.setResponseBodyAdvice(List.of(
                            new JsonViewResponseBodyAdvice(),
                            new FieldsResponseBodyAdvice(jsonMapper.getObject())));
                }
                return bean;
            }
        };
    }
}

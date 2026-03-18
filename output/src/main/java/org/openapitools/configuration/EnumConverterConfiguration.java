package org.openapitools.configuration;

import org.etsi.osl.controllers.tmf915.model.AlarmType;
import org.etsi.osl.controllers.tmf915.model.OrderItemActionType;
import org.etsi.osl.controllers.tmf915.model.PerceivedSeverity;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * This class provides Spring Converter beans for the enum models in the OpenAPI specification.
 *
 * By default, Spring only converts primitive types to enums using Enum::valueOf, which can prevent
 * correct conversion if the OpenAPI specification is using an `enumPropertyNaming` other than
 * `original` or the specification has an integer enum.
 */
@Configuration(value = "org.openapitools.configuration.enumConverterConfiguration")
public class EnumConverterConfiguration {

    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.alarmTypeConverter")
    Converter<String, AlarmType> alarmTypeConverter() {
        return new Converter<String, AlarmType>() {
            @Override
            public AlarmType convert(String source) {
                return AlarmType.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.orderItemActionTypeConverter")
    Converter<String, OrderItemActionType> orderItemActionTypeConverter() {
        return new Converter<String, OrderItemActionType>() {
            @Override
            public OrderItemActionType convert(String source) {
                return OrderItemActionType.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.perceivedSeverityConverter")
    Converter<String, PerceivedSeverity> perceivedSeverityConverter() {
        return new Converter<String, PerceivedSeverity>() {
            @Override
            public PerceivedSeverity convert(String source) {
                return PerceivedSeverity.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.serviceStateTypeConverter")
    Converter<String, ServiceStateType> serviceStateTypeConverter() {
        return new Converter<String, ServiceStateType>() {
            @Override
            public ServiceStateType convert(String source) {
                return ServiceStateType.fromValue(source);
            }
        };
    }

}

package com.homestay.homestay.config.date;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/11/17/18:43
 * @Description: 格式化LocalDateTime类型
 */


@Configuration
public class LocalDateTimeSerializerConfig {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        // 默认LocalDateTime格式化的格式 yyyy-MM-dd HH:mm:ss
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}
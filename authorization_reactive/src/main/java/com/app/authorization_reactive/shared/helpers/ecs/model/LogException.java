package com.app.authorization_reactive.shared.helpers.ecs.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LogException implements Serializable {


    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss:SSSS";

    @Builder.Default
    private String messageId = UUID.randomUUID().toString();
    @Builder.Default
    private String date =  currentDate();
    private String service;
    private Level level;
    private ErrorLog error;

    private static String currentDate(){
        var date = LocalDateTime.now(ZoneOffset.of("-05:00"));
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return  objectMapper.writeValueAsString(this);

        }catch (JsonProcessingException ex){
            return "{\"error:\" \"json conversion fail\"}";
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorLog implements Serializable{
        private String type;
        private String message;
        private String description;
        private Map optionalInfo;
    }

    public enum Level implements Serializable{
        DEBUG("DEBUG"),
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        FATAL("FATAL");

        private String value;

        Level(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}







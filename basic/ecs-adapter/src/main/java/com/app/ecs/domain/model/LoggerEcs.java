package com.app.ecs.domain.model;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggerEcs {

    private LoggerEcs() {
    }

    public static void print(LogRecord<String, String> ex) {

        switch (ex.getLevel()) {
            case DEBUG -> log.debug(ex.toJson());
            case INFO -> log.info(ex.toJson());
            case WARNING -> log.warn(ex.toJson());
            case ERROR -> log.error(ex.toJson());
            case FATAL -> log.fatal(ex.toJson());
        }
    }
}

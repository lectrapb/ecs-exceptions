package com.app.authorization_reactive.shared.helpers.ecs.model;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggerEcs {

    public static void print(LogException ex){

          switch (ex.getLevel()){
              case DEBUG   -> log.debug(ex.toJson());
              case INFO    -> log.info(ex.toJson());
              case WARNING -> log.warn(ex.toJson());
              case ERROR   -> log.error(ex.toJson());
              case FATAL   -> log.fatal(ex.toJson());
          }

    }
}

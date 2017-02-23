package com.jchugh.service;

import com.jchugh.handler.ChatMessageParseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

import java.util.Properties;

import static spark.Spark.path;
import static spark.Spark.post;

/**
 * The Service class for the app, defines the routes.
 */
public class ChatMessageParser implements SparkApplication {
    private final Properties properties;
    private static Logger logger = LoggerFactory.getLogger("ChatMessageParser");

    public ChatMessageParser(Properties properties) {
        this.properties = properties;
        init();
    }

    // Starts the embedded server.
    @Override
    public void init() {
        logger.debug("***** Message Parser Started *****");
        path("/api", () -> {
            path("/v1", () -> {
                post("/parse", new ChatMessageParseHandler());
            });
        });
    }


}

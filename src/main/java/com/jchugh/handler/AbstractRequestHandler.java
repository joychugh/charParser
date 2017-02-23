package com.jchugh.handler;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Optional;

/**
 * Request handler class abstracting over other request handlers.
 */
public abstract class AbstractRequestHandler<V> implements Route {

    private Class<V> typeClass;
    private static final Gson gson = new Gson();
    private static Logger logger = LoggerFactory.getLogger("AbstractRequestHandler");
    public static final int RESPONSE_OK = 302;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_FOUND = 404;
    public static final int BAD_REQUEST = 400;
    public static final String APPLICATION_JSON = "application/json";

    public AbstractRequestHandler(Class<V> typeClass) {
        this.typeClass = typeClass;
    }

    protected Optional<String> toJson(Object pojo) {
        try {
            return Optional.ofNullable(gson.toJson(pojo));
        } catch (Exception e) {
            logger.error("Could not deserialize Object to JSON", e);
            return Optional.empty();
        }
    }

    abstract InternalResponse process(V requestObject, Request request);

    @Override
    public Object handle(Request request, Response response) throws Exception {
        V requestObject;

        try {
            requestObject = gson.fromJson(request.body(), typeClass);
        } catch (Exception e) {
            response.status(BAD_REQUEST);
            return gson.toJsonTree("Invalid request format. JSON possibly malformed.");
        }
        InternalResponse internalResponse = process(requestObject, request);
        response.status(internalResponse.getStatusCode());
        response.type(APPLICATION_JSON);
        return internalResponse.getResponse();
    }
}

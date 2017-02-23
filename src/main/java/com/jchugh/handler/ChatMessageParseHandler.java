package com.jchugh.handler;

import com.jchugh.model.request.MessageParsingRequest;
import com.jchugh.model.response.ExceptionResponse;
import com.jchugh.model.response.MessageParsingResponse;
import com.jchugh.util.TagsExtractor;
import com.jchugh.util.WebsiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Chat message parsing handler class.
 */
public class ChatMessageParseHandler extends AbstractRequestHandler<MessageParsingRequest> {

    private static Logger logger = LoggerFactory.getLogger("ChatMessageParseHandler");

    public ChatMessageParseHandler() {
        super(MessageParsingRequest.class);
    }

    /**
     * This will process the incoming chat message and return an instance of {@link InternalResponse} containing the
     * emoticons, mentions and URL info.
     * @param requestObject The MessageParsingRequest object containing the message to parse.
     * @param request The spark {@link Request} object containing the original request.
     * @return InternalResponse containing the extracted emoticons, mentions, url..
     */
    @Override
    InternalResponse process(MessageParsingRequest requestObject, Request request) {
        MessageParsingResponse parsingResponse = new MessageParsingResponse();
        try {
            TagsExtractor.extractTags(requestObject.getMessage(), TagsExtractor.Tag.EMOTICONS).ifPresent(parsingResponse::setEmoticons);
            TagsExtractor.extractTags(requestObject.getMessage(), TagsExtractor.Tag.MENTION).ifPresent(parsingResponse::setMentions);
            TagsExtractor.extractTags(requestObject.getMessage(), TagsExtractor.Tag.URL).flatMap(this::getLinks).ifPresent(parsingResponse::setLinks);
            Optional<String> responseStr = toJson(parsingResponse);
            // Log the response, to debug
            responseStr.ifPresent(s -> logger.debug("Incoming Request: {}, Response: {}", requestObject.getMessage(), s));
            Optional<InternalResponse> internalResponse = responseStr.map(r -> new InternalResponse(r, RESPONSE_OK));
            if (internalResponse.isPresent()) {
                return internalResponse.get();
            } else {
                logger.error("Failed to generate response while processing the request");
                ExceptionResponse exceptionResponse = new ExceptionResponse();
                exceptionResponse.setMessage("Could not parse chat message");
                return new InternalResponse(toJson(exceptionResponse).get(), INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Encountered error while processing the request", e);
            ExceptionResponse exceptionResponse = new ExceptionResponse();
            exceptionResponse.setMessage("Could not parse chat message");
            return new InternalResponse(toJson(exceptionResponse).get(), INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method will go through all the URLs in the message and use the #{@link WebsiteParser} to get their info
     * @param urls URLs to get title information from
     * @return  Optional of Array of {@link com.jchugh.model.response.MessageParsingResponse.Link} containing the url info.
     */
    private Optional<MessageParsingResponse.Link[]> getLinks(String[] urls) {
        List<MessageParsingResponse.Link> linkList = new LinkedList<>();
        for(String url: urls) {
            MessageParsingResponse.Link link = new MessageParsingResponse.Link();
            link.setUrl(url);
            WebsiteParser.getTitle(url).ifPresent(t -> {
                link.setTitle(t);
                linkList.add(link);
            });
        }
        if (!linkList.isEmpty()) {
            MessageParsingResponse.Link[] ret = new MessageParsingResponse.Link[linkList.size()];
            return Optional.of(linkList.toArray(ret));
        } else {
            return Optional.empty();
        }
    }
}

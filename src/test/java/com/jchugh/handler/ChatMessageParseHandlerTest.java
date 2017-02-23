package com.jchugh.handler;

import com.google.gson.Gson;
import com.jchugh.model.request.MessageParsingRequest;
import com.jchugh.model.response.MessageParsingResponse;
import com.jchugh.util.WebsiteParser;
import org.easymock.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spark.Request;
import spark.Response;

import static com.jchugh.handler.AbstractRequestHandler.APPLICATION_JSON;
import static org.testng.Assert.*;
import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertEqualsNoOrder;

/**
 * @author jchugh
 */

public class ChatMessageParseHandlerTest {

    ChatMessageParseHandler chatMessageParseHandler;
    Gson gson;
    Request mockRequest;
    Response mockResponse;

    @BeforeMethod
    public void setUp() throws Exception {
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        chatMessageParseHandler = new ChatMessageParseHandler();
        gson = new Gson();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        chatMessageParseHandler = null;
        gson = null;
        reset(mockRequest, mockResponse);
    }

    @DataProvider(name = "messageParserHandle")
    public Object[][] getMessageParserTestData() {
        return new Object[][] {
                {"{\"message\": \"Hi @tom and @jerry (hello) (welcome) https://twitter.com/NASA/status/834575390257643521\"}",
                        new String[]{"hello", "welcome"},
                        new String[]{"tom", "jerry"},
                        "“Notice the new @GoogleDoodles? It's about the 7 Earth-sized planets we discovered around nearby star! Get the news: https://t.co/G9tW3cJMnV https://t.co/dOHB0bLqXn”",
                        "https://twitter.com/NASA/status/834575390257643521"},
                {"{\"message\": \"Hi @tom and @jerry (hello) (welcome) http://sparkjava.com\"}",
                        new String[]{"hello", "welcome"},
                        new String[]{"tom", "jerry"},
                        "Spark Framework - A tiny Java web framework",
                        "http://sparkjava.com"}
        };
    }

    @Test(dataProvider = "messageParserHandle")
    public void testHandle(String message, String[] expectedEmojis, String[] expectedMentions, String expectedTitle, String expectedURL) throws Exception {
        expect(mockRequest.body()).andReturn(message);
        mockResponse.status(302);
        mockResponse.type(APPLICATION_JSON);
        replay(mockRequest, mockResponse);
        String response = (String) chatMessageParseHandler.handle(mockRequest, mockResponse);
        MessageParsingResponse actualResponse = gson.fromJson(response, MessageParsingResponse.class);
        verify(mockRequest, mockResponse);
        assertEqualsNoOrder(actualResponse.getEmoticons(), expectedEmojis);
        assertEqualsNoOrder(actualResponse.getMentions(), expectedMentions);
        assertEquals(actualResponse.getLinks().length, 1);
        assertEquals(actualResponse.getLinks()[0].getTitle(), expectedTitle);
        assertEquals(actualResponse.getLinks()[0].getUrl(), expectedURL);
    }

}
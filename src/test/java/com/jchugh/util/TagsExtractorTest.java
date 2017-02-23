package com.jchugh.util;

import org.testng.annotations.DataProvider;

import java.util.Optional;

import static org.testng.Assert.assertEqualsNoOrder;

/**
 * @author jchugh
 */
public class TagsExtractorTest {

    @DataProvider(name="extract-tags", parallel = true)
    public Object[][] dataForTags() {
        return new Object[][] {
                // Chat message                                                             Expected Mentions              Expected Emojis                  Expected URLS
                {"Hi @tom and @jerry (hello) (welcome) http://google.com",               new String[]{"tom", "jerry"},   new String[]{"welcome", "hello"}, new String[]{"http://google.com"}},
                {"Hi @tom@jerry (hello)(welcome) http://google.com https://twitter.com", new String[]{"tom", "jerry"},   new String[]{"welcome", "hello"}, new String[]{"http://google.com", "https://twitter.com"}},
                {"@tom.@jerry (hello)&(welcome) http://google.com",                      new String[]{"tom", "jerry"},   new String[]{"welcome", "hello"}, new String[]{"http://google.com"}},
                {"@tom.jerry (hello).welcome) http://invalidurl.dcom",                   new String[]{"tom"},            new String[]{"hello"},            new String[]{}},
                {"hi @tom (thisisareallylongemoji) (thisisok)",                          new String[]{"tom"},            new String[]{"thisisok"},         new String[]{}},
                {"hi@tom(emoji)http://url.com",                                          new String[]{"tom"},            new String[]{"emoji"},            new String[]{"http://url.com"}}
        };
    }

    @org.testng.annotations.Test(dataProvider = "extract-tags")
    public void testExtractTags(String input, String[] expectedMentions, String[] expectedEmoji, String[] expectedUrls) throws Exception {
        Optional<String[]> actualEmoji = TagsExtractor.extractTags(input, TagsExtractor.Tag.EMOTICONS);
        Optional<String[]> actualMentions = TagsExtractor.extractTags(input, TagsExtractor.Tag.MENTION);
        Optional<String[]> actualUrls = TagsExtractor.extractTags(input, TagsExtractor.Tag.URL);

        if (expectedEmoji.length > 0) {
            assertEqualsNoOrder(actualEmoji.get(), expectedEmoji);
        }
        if (expectedMentions.length > 0) {
            assertEqualsNoOrder(actualMentions.get(), expectedMentions);
        }
        if (expectedUrls.length > 0) {
            assertEqualsNoOrder(actualUrls.get(), expectedUrls);
        }

    }

}
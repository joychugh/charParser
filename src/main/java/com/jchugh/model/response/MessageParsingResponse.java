package com.jchugh.model.response;


/**
 * Class to represent the Response sent to the client.
 * This is used by JSON Serializer/Deserializer
 */
public class MessageParsingResponse {

    public static class Link {
        String url;
        String title;

        public Link() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private String[] emoticons;
    private String[] mentions;
    private Link[] links;

    public String[] getEmoticons() {
        return emoticons;
    }

    public String[] getMentions() {
        return mentions;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setEmoticons(String[] emoticons) {
        this.emoticons = emoticons;
    }

    public void setMentions(String[] mentions) {
        this.mentions = mentions;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    public MessageParsingResponse() {
    }
}

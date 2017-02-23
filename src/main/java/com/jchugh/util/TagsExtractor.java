package com.jchugh.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class to extract text patterns from a given string.
 */
public class TagsExtractor {

    public enum Tag {
        MENTION,
        EMOTICONS,
        URL
    }

    // Compiled regex patterns
    private static final Pattern MENTIONS_RE = Pattern.compile("@(\\w{1,15})(?=\\W|$)");
    private static final Pattern EMOTIOCS_RE = Pattern.compile("\\((\\w{1,15})\\)");
    private static final Pattern URL_RE = Pattern.compile("(?:(?:https?)://)?(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?");
    private static final Map<Tag, Pattern> TAG_PATTERN_MAP = new HashMap<>();
    // Some patterns are extracted from the group 1 (vs base group i.e 0) so we keep a map to make it configurable.
    private static final Map<Tag, Integer> GROUP_MAP = new HashMap<>();
    static {
        TAG_PATTERN_MAP.put(Tag.MENTION, MENTIONS_RE);
        TAG_PATTERN_MAP.put(Tag.EMOTICONS, EMOTIOCS_RE);
        TAG_PATTERN_MAP.put(Tag.URL, URL_RE);
        GROUP_MAP.put(Tag.EMOTICONS, 1);
        GROUP_MAP.put(Tag.URL, 0);
        GROUP_MAP.put(Tag.MENTION, 1);
    }

    /**
     * Extract tags (emoticons, mentions, URLs..) from the given Chat message.
     * @param text The chat message
     * @param tagType The type of information needed to be extracted (eg. {@link Tag#EMOTICONS}
     * @return Optional of Array of Strings representing the tags extracted. If none were found, returns Optional Empty.
     */
    public static Optional<String[]> extractTags(String text, Tag tagType) {
        int groupNumber = GROUP_MAP.get(tagType);
        Pattern re = TAG_PATTERN_MAP.get(tagType);
        Matcher tagMatcher = re.matcher(text);
        Set<String> tags = new HashSet<>();
        while(tagMatcher.find()) {
            tags.add(tagMatcher.group(groupNumber));
        }
        if (tags.isEmpty()) {
            return Optional.empty();
        }
        String[] ret = new String[tags.size()];
        return Optional.of(tags.toArray(ret));
    }



}

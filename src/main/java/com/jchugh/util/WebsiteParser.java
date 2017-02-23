package com.jchugh.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author jchugh
 * Class used to parse websites for Titles and Image
 * The title is the OpenGraph title, if not present, then the HTML Title
 * The Image is the OpenGraph Image, if not present, it's not returned.
 * Credits for OpenGraph parsing: http://stackoverflow.com/a/18880520
 */
public class WebsiteParser {
    private static Logger logger = LoggerFactory.getLogger("WebsiteParser");

    /**
     * This class holds the title and image from a webpage.
     * Title And/Or Image can be empty if there were none found.
     */
    public static class TitleAndImage {
        private final String title;
        private final String imgUrl;

        public TitleAndImage(String title, String imgUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
        }

        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }

        public Optional<String> getImgUrl() {
            return Optional.of(imgUrl);
        }
    }

    /**
     * Get the title for the page. The first attempt is done to extract the OpenGraph Title, if not present
     * then the regular HTML Title is used.
     * @param url The website URL to get Title and Image from
     * @return Optional with The title for the page, if error encountered, Empty Optional is returned.
     */
    public static Optional<String> getTitle(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            String title;
            Elements metaOgDescription = doc.select("meta[property=og:description]");
            Elements metaOgTitle = doc.select("meta[property=og:title]");
            if (!metaOgDescription.isEmpty()) {
                title = metaOgDescription.attr("content");
            } else if (!metaOgTitle.isEmpty()) {
                title = metaOgTitle.attr("content");
            } else {
                title = doc.title();
            }
            return Optional.ofNullable(title);
        } catch (Exception e) {
            logger.error("Invalid URL supplied: {}", url, e);
            return Optional.empty();
        }
    }

    /**
     * Returns the Title and Image for the page.
     * The first attempt is done to extract the OpenGraph Title, if not present
     * then the regular HTML Title is used.
     *
     * The Image is the OpenGraph image URL
     * @param url The website URL to get Title and Image from
     * @return {@link TitleAndImage} Optional with The title and image object, Empty optional if error encountered.
     */
    public static Optional<TitleAndImage> getTitleAndImage(String url) {
        try {
            String title;
            String imgUrl = null;
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            Elements metaOgTitle = doc.select("meta[property=og:title]");
            if (metaOgTitle!=null) {
                title = metaOgTitle.attr("content");
            } else {
                title = doc.title();
            }

            Elements metaOgImage = doc.select("meta[property=og:image]");
            if (metaOgImage!=null) {
                imgUrl = metaOgImage.attr("content");
            }
            TitleAndImage titleAndImage = new TitleAndImage(title, imgUrl);
            return Optional.of(titleAndImage);
        } catch (Exception e) {
            logger.error("Invalid URL supplied: {}", url, e);
            return Optional.empty();
        }
    }
}

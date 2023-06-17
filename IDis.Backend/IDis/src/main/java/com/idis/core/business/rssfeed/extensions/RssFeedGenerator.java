package com.idis.core.business.rssfeed.extensions;
import com.idis.core.domain.posts.parentpost.Post;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RssFeedGenerator {
    private static final String RSS_FILE_PATH = "rss.xml";
    private final List <Post> posts;
    private final Map<UUID,Integer> numOfReplies;

    public RssFeedGenerator(List<Post> posts, Map<UUID,Integer> numOfReplies) {
        this.posts = posts;
        this.numOfReplies = numOfReplies;
    }

    public String generateRSS () throws Exception {

        SyndFeed feed = generateFeed();

        try {
            SyndFeedOutput output = new SyndFeedOutput();
            String xmlString = output.outputString(feed);
            return xmlString;

        } catch (Exception e) {
            System.err.println("Error generating RSS feed XML: " + e.getMessage());
            throw e;
        }
    }

    private SyndFeed generateFeed(){
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("IDislikeIT");
        feed.setLink("https://localhost:7017/main.html");
        feed.setDescription("Welcome to I Dislike It!, a unique platform where users can create posts about various topics, rate them, and share their thoughts.");
        feed.setLanguage("en-us");

        List<SyndEntry> entries = generateEntries();
        feed.setEntries(entries);

        return feed;
    }

    private List<SyndEntry> generateEntries(){

        List<SyndEntry> entries = new ArrayList<>();

        for (Post post : posts) {
            SyndEntry entry = new SyndEntryImpl();
            SyndContent description = new SyndContentImpl();

            String replies = (numOfReplies.get(post.getId()) != null) ? numOfReplies.get(post.getId()).toString() : "0";
            entry.setTitle(post.getTitle()+ " -> "+replies+" replies");
            entry.setLink("http://127.0.0.1:5500/posts/?postId=" + post.getId());
            description.setValue(post.getBody());
            entry.setDescription(description);
            entry.setPublishedDate(post.getCreatedAt());
            entries.add(entry);
        }
        return entries;
    }

    public String getRssFilePath() {
        return RSS_FILE_PATH;
    }
}

package com.idis.core.business.rssfeed.extensions;
import com.idis.core.domain.posts.parentpost.Post;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.WireFeedOutput;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RSSFeedGenerator {

    private static final String RSS_FILE_PATH = "rss.xml";
    private final List <Post> posts;

    public RSSFeedGenerator(List<Post> posts) {
        this.posts = posts;
    }



    public void generateRSS () throws Exception {

        SyndFeed feed = generateFeed();

        try {
            SyndFeedOutput output = new SyndFeedOutput();
            FileWriter fileWriter = new FileWriter(RSS_FILE_PATH);
            output.output(feed, fileWriter);
            fileWriter.close();

            System.out.println("RSS feed XML generated successfully!");
        } catch (IOException | FeedException e) {
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
            SyndContent description = new SyndContentImpl() {
            };

            entry.setTitle(post.getTitle());
            entry.setLink("http://localhost:7101/posts/" + post.getId());
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

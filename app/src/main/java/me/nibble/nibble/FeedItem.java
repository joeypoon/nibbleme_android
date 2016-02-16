package me.nibble.nibble;

/**
 * Created by Whit on 1/23/2016.
 */
public class FeedItem {
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public FeedItem(){}
    public FeedItem(String title) {
        setTitle(title);
    }
}

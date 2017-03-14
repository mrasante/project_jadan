
package support.esri.com.fuse.models;


public class Trend {

    private String name;
    private String url;
    private Object promotedContent;
    private String query;
    private Object tweetVolume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getPromotedContent() {
        return promotedContent;
    }

    public void setPromotedContent(Object promotedContent) {
        this.promotedContent = promotedContent;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Object getTweetVolume() {
        return tweetVolume;
    }

    public void setTweetVolume(Object tweetVolume) {
        this.tweetVolume = tweetVolume;
    }

}
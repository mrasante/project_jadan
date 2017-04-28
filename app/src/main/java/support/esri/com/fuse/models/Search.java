
package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search {

    @SerializedName("/search/tweets")
    @Expose
    private SearchTweets searchTweets;

    public SearchTweets getSearchTweets() {
        return searchTweets;
    }

    public void setSearchTweets(SearchTweets searchTweets) {
        this.searchTweets = searchTweets;
    }

}

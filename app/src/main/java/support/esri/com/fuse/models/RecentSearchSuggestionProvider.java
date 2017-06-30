package support.esri.com.fuse.models;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by kwas7493 on 6/29/2017.
 */

public class RecentSearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public static String AUTHORITY = "support.esri.com.fuse.models.RecentSearchSuggestionProvider";
    public static int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

    public RecentSearchSuggestionProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }

}

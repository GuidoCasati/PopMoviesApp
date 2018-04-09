package guidocasati.popmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by guido on 26/03/2018.
 */

public class PopMovieContract {


    // Content provider constants needed to define how to access the Starred Movies data

    // The authority, defining which Content Provider to access
    public static final String AUTHORITY = "guidocasati.popmoviesapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_STARRED = StarredMovieEntry.TABLE_NAME;




    public static final class StarredMovieEntry implements BaseColumns {
        // Since StarredMovieEntry implements the interface "BaseColumns", it has an automatically produced "_ID" column

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(StarredMovieEntry.TABLE_NAME).build();


        // table name
        public static final String TABLE_NAME = "starredMovies";

        //columns names
        public static final String _ID = "_ID";
        public static final String COLUMN_MOVIE_TITLE = "MovieTitle";
        public static final String COLUMN_POSTER_PATH = "PosterPath";
        public static final String COLUMN_RATING = "Rating";
        public static final String COLUMN_PLOT = "Plot";
        public static final String COLUMN_RELEASE_DATE = "ReleaseDate";

    }
}

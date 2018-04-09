package guidocasati.popmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import guidocasati.popmoviesapp.data.PopMovieContract.*;

/**
 * Created by guido on 26/03/2018.
 */

public class PopMovieDbHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "waitlist.db";
    // used to change DB version each time schema is changed
    private static final int DATABASE_VERSION = 1;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public PopMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold starred movies data
        final String SQL_CREATE_STARREDMOVIES_TABLE = "CREATE TABLE " + StarredMovieEntry.TABLE_NAME + " (" +
                StarredMovieEntry._ID + " INTEGER PRIMARY KEY," +
                StarredMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                StarredMovieEntry.COLUMN_PLOT + " TEXT NOT NULL," +
                StarredMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                StarredMovieEntry.COLUMN_RATING + " TEXT NOT NULL," +
                StarredMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" +
                "); ";
         db.execSQL(SQL_CREATE_STARREDMOVIES_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StarredMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

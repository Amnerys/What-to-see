package amner.android.com.movies.databaseclient;

/**
 * @author amner
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;


class JsonUtilsList {

    public static List<Movie> getSimpleMoviesInformationFromJson(String moviesJsonStr) throws JSONException {

        List<Movie> movieListResults;
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray list = jsonObject.getJSONArray(Constants.RESULTS);
        movieListResults = new ArrayList<>();

        for (int i = 0; i < list.length(); i++) {
            JSONObject movieData = list.getJSONObject(i);
            Movie movieObject = new Movie(movieData.getString(Constants.ID), movieData.getString(Constants.TITLE));
            movieObject.setPoster(movieData.getString(Constants.POSTER_PATH));
            movieObject.setReleaseYear(movieData.getString(Constants.RELEASE_DATE));
            movieObject.setOverview(movieData.getString(Constants.OVERVIEW));
            movieObject.setRating(movieData.getString(Constants.VOTE_AVERAGE));
            movieListResults.add(movieObject);
        }
        return movieListResults;
    }
}

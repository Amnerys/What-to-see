package amner.android.com.movies.databaseclient;

import android.net.Uri;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;

public class DBClient {

    public DBClient() {
    }

    //Method to fetch response from API's URL
    private static String fetchResponseURL(URL url) throws IOException {
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = httpUrlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasResponse = scanner.hasNext();
            if (hasResponse) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpUrlConnection.disconnect();
        }
    }

    //Method to build an Url with the parameters passed
    private static URL buildUrl(String path) {
        Uri uriBuild = Uri.parse(Constants.BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(Constants.QUERY_PARAM, Constants.API_KEY)
                .appendQueryParameter(Constants.LANG_PARAM, Constants.LANG_VALUE)
                .appendQueryParameter(Constants.PAGE_PARAM, Constants.PAGE_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(uriBuild.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Method to fetch the movie details url
    private static URL urlMovieDetails(String id) {
        Uri uriBuild = Uri.parse(Constants.BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(Constants.QUERY_PARAM, Constants.API_KEY)
                .appendQueryParameter(Constants.LANG_PARAM, Constants.LANG_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(uriBuild.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Method to fetch url for similar movies
    private static URL urlSimilarMovies(String id) {
        Uri uriBuild = Uri.parse(Constants.BASE_URL).buildUpon().appendPath(id)
                .appendPath(Constants.SEARCH_PARAM)
                .appendQueryParameter(Constants.QUERY_PARAM, Constants.API_KEY)
                .appendQueryParameter(Constants.LANG_PARAM, Constants.LANG_VALUE)
                .appendQueryParameter(Constants.PAGE_PARAM, Constants.PAGE_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(uriBuild.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Method to populate the list with popular movies
    private List<Movie> fetchPopularList() throws DBClientException {
        URL urlList = buildUrl(Constants.PATH_POPULAR_PARAM);
        try {
            String jsonPopularMoviesResponse = fetchResponseURL(urlList);
            return JsonUtilsList.getSimpleMoviesInformationFromJson(jsonPopularMoviesResponse);
        } catch (IOException | JSONException e) {
            throw new DBClientException(e);
        }
    }

    //Populate list with similar movies
    public List<Movie> fetchSimilarList(String id) throws DBClientException {
        URL urlList = urlSimilarMovies(id);
        try {
            String jsonSimilarMoviesResponse = fetchResponseURL(urlList);
            return JsonUtilsList.getSimpleMoviesInformationFromJson(jsonSimilarMoviesResponse);
        } catch (IOException | JSONException e) {
            throw new DBClientException(e);
        }
    }

    public List<Movie> getSomePopularMovies(int length) throws DBClientException {
        List<Movie> movieList;
        movieList = fetchPopularList();
        return movieList.subList(0, length - 1);
    }

    //Method to get a random number
    public int randomNum(int fromNum, int toNum) {
        final int minNum = fromNum;
        final int maxNum = toNum;
        final int randomNum = new Random().nextInt((maxNum - minNum) + 1) + minNum;
        return randomNum;
    }
}

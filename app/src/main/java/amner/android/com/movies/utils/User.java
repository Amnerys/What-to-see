package amner.android.com.movies.utils;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    public String nameUser;

    public User(String email) {
        String userEmail = email;
        List<Movie> userMovies = new ArrayList<Movie>();
    }

}


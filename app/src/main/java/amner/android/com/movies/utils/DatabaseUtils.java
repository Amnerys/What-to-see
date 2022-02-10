package amner.android.com.movies.utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author amner
 */
public class DatabaseUtils {

    private DatabaseReference databaseReference;
    public DatabaseUtils() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void getUser(String userId, User user) {
        databaseReference.child(Constants.USERS).child(userId).setValue(user);
    }

    public void getMoviePreference(String userID, Movie movie, Movie.PREFERENCE preference) {
        DatabaseReference getReferenceWish = databaseReference.getRef().child(Constants.USERS).child(userID);
        movie.setMoviePreference(preference);
        HashMap<String, Object> map = new HashMap<>();
        map.put(movie.getMovieId(), movie);
        getReferenceWish.child(Constants.MOVIES).updateChildren(map);
    }

    private List<Movie> getMovieList(String userID, final Movie.PREFERENCE preference) {
        FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = moviesList.getReference().child(Constants.USERS).child(userID).child(Constants.MOVIES);
        final List<Movie> list = new ArrayList<Movie>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie != null && movie.getMoviePreference().equals(preference)) {
                        list.add(movie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        moviesListRef.addValueEventListener(valueEventListener);
        return list;
    }

    public List<Movie> getWishMovies(String userID) {
        return getMovieList(userID, Movie.PREFERENCE.WISH);
    }

    public List<Movie> getLikedMovies(String userID) {
        return getMovieList(userID, Movie.PREFERENCE.LIKED);
    }

    public boolean removeAllLists(String userId) {
        try {
            databaseReference.getRef().child(Constants.USERS).child(userId).child(Constants.MOVIES).removeValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long[] checkIfExists(String userID) {
        final Long[] size2 = new Long[1];
        FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = moviesList.getReference().child(Constants.USERS).child(userID).child(Constants.MOVIES);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size2[0] = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        moviesListRef.addValueEventListener(valueEventListener);
        return size2;
    }
}

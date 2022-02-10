package amner.android.com.movies.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import amner.android.com.movies.R;
import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;


class WidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context movieContext;
    private List<Movie> newListMovies;
    private FirebaseAuth firebaseAuth;

    public WidgetProvider(Context mContext) {
        this.movieContext = mContext;
        newListMovies = new ArrayList<>();
    }

    private void initData() throws NullPointerException {
        try {
            this.firebaseAuth = FirebaseAuth.getInstance();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        initData();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = firebaseDatabase.getReference().child(Constants.USERS)
                .child(firebaseAuth.getCurrentUser().getUid()).child(Constants.MOVIES);

        /**
         * Get list of movie id from the Firebase
         */
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie newMovie = ds.getValue(Movie.class);
                    if (newMovie.getMoviePreference().equals(Movie.PREFERENCE.WISH)) {
                        newListMovies.add(newMovie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dbReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDataSetChanged() {
        initData();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = firebaseDatabase.getReference().child(Constants.USERS)
                .child(firebaseAuth.getCurrentUser().getUid()).child(Constants.MOVIES);

        /**
         * Get list of movie id from the Firebase
         */
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie.getMoviePreference().equals(Movie.PREFERENCE.WISH)) {
                        newListMovies.add(movie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        moviesListRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return newListMovies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movie = newListMovies.get(position);
        RemoteViews remoteViews = new RemoteViews(movieContext.getPackageName(), R.layout.widget_wish_list_item);
        remoteViews.setTextViewText(R.id.widget_movie_name, movie.getmTitle());
        remoteViews.setTextViewText(R.id.widget_movie_year, movie.getReleaseYear());

        Bundle extras = new Bundle();
        extras.putParcelable(Constants.WISH_LIST_PARCELED, movie);
        Intent setIntent = new Intent();
        setIntent.putExtra(Constants.RECOMMENDED_MOVIE, movie);
        setIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_item_row, setIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

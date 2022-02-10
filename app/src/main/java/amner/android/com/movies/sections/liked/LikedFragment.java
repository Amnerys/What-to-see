package amner.android.com.movies.sections.liked;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import amner.android.com.movies.R;
import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;
import amner.android.com.movies.utils.DatabaseUtils;

public class LikedFragment extends Fragment {

    private final static String TAG = LikedFragment.class.getSimpleName();
    private int countColumns = 1;
    private List<Movie> likedMoviesList;

    public void likedMovies(List<Movie> movies) {
        this.likedMoviesList = movies;
    }

    //Required empty constructor
    public LikedFragment() {
    }

    @SuppressWarnings("unused")
    public static LikedFragment newInstance(int columnCount) {
        LikedFragment fragment = new LikedFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            likedMoviesList = savedInstanceState.getParcelableArrayList(getString(R.string.LIKED_MOVIES));
        }
        if (getArguments() != null) {
            countColumns = getArguments().getInt(Constants.COLUMN_COUNT);
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.likedMoviesList = databaseUtils.getLikedMovies(firebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (countColumns <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, countColumns));
            }
            recyclerView.setAdapter(new LikedAdapter(likedMoviesList));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.LIKED_MOVIES), (ArrayList<? extends Parcelable>) likedMoviesList);
    }
}

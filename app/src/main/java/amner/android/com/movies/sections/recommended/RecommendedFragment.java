package amner.android.com.movies.sections.recommended;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import amner.android.com.movies.R;
import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;
import amner.android.com.movies.utils.DatabaseUtils;


public class RecommendedFragment extends Fragment {

    private final static String TAG = RecommendedFragment.class.getSimpleName();
    private DatabaseUtils dbUtils;
    private FirebaseAuth firebaseAuth;
    private Movie movie;

    public RecommendedFragment() {
        // Required empty public constructor
    }

    public static RecommendedFragment newInstance(Movie movie) {
        RecommendedFragment fragment = new RecommendedFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.RECOMMENDED_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.movie = savedInstanceState.getParcelable(getString(R.string.PARCELED_MOVIE));
        } else if (getArguments() != null) {
            this.movie = getArguments().getParcelable(getString(R.string.RECOMMENDED_MOVIE));
        }
        firebaseAuth = FirebaseAuth.getInstance();
        dbUtils = new DatabaseUtils();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommended, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_movie_recommended);
        String posterPath = movie.getPoster();

        String MOVIE_IMAGE_URL = "https://image.tmdb.org/t/p/w780/";
        Glide.with(getActivity().getApplicationContext()).load(MOVIE_IMAGE_URL + posterPath).into(imageView);

        final ImageView wishListIcon = (ImageView) rootView.findViewById(R.id.wish_list_button);
        final ImageView likeIcon = (ImageView) rootView.findViewById(R.id.like_button);

        wishListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.getMoviePreference(firebaseAuth.getCurrentUser().getUid(), movie, Movie.PREFERENCE.WISH);
                Toast.makeText(getContext(), R.string.movie_added_wish_list, Toast.LENGTH_LONG).show();
                wishListIcon.setEnabled(false);
                likeIcon.setAlpha(1);
                likeIcon.setEnabled(false);
            }
        });

        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.getMoviePreference(firebaseAuth.getCurrentUser().getUid(), movie, Movie.PREFERENCE.LIKED);
                Toast.makeText(getContext(), R.string.movie_added_like, Toast.LENGTH_LONG).show();
                wishListIcon.setAlpha(1);
                wishListIcon.setEnabled(false);
                wishListIcon.setEnabled(false);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.PARCELED_MOVIE), movie);
    }
}

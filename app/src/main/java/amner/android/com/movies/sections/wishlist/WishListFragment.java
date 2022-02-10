package amner.android.com.movies.sections.wishlist;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;
import amner.android.com.movies.utils.DatabaseUtils;
import amner.android.com.movies.R;

public class WishListFragment extends Fragment {

    private List<Movie> wishMoviesList;
    private int countColumn = 1;

    //Required empty public constructor
    public WishListFragment() {
    }

    @SuppressWarnings("unused")
    public static WishListFragment newInstance(int columnCount) {
        WishListFragment fragment = new WishListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.wishMoviesList = savedInstanceState.getParcelableArrayList("MOVIES_LIST");
        } else if (getArguments() != null) {
            countColumn = getArguments().getInt(Constants.COLUMN_COUNT);
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseUtils dbUtils = new DatabaseUtils();
        this.wishMoviesList = dbUtils.getWishMovies(firebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wish_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (countColumn <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, countColumn));
            }
            recyclerView.setAdapter(new WishListAdapter(wishMoviesList, getActivity().getApplicationContext()));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIES_LIST, new ArrayList<Movie>(wishMoviesList));
    }
}

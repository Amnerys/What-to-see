package amner.android.com.movies.sections;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import amner.android.com.movies.BasicActivity;
import amner.android.com.movies.R;
import amner.android.com.movies.sections.recommended.RecommendedPagerAdapter;
import amner.android.com.movies.sections.liked.LikedFragment;
import amner.android.com.movies.sections.wishlist.WishListFragment;
import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;
import amner.android.com.movies.databaseclient.DBClient;
import amner.android.com.movies.databaseclient.DBClientException;

public class ScreenActivity extends BasicActivity {

    private static final boolean AUTO_HIDE = true;
    private Long movieNumber;
    private List<Movie> recommendedList;
    private List<Movie> likedList;
    private final static String TAG = ScreenActivity.class.getSimpleName();

    private final Handler handler = new Handler();
    private View view;

    private final Runnable hideRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View controlView;

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Display UI items after a time
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            controlView.setVisibility(View.VISIBLE);
        }
    };

    private boolean isVisible;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hideUI();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                hideDelay(Constants.DELAY_AUTO_HIDE);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_section);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        isVisible = true;
        controlView = findViewById(R.id.fullscreen_content_controls);
        view = findViewById(R.id.fullscreen_content);
        TextView screenTitle = findViewById(R.id.feature_title);

        ViewPager moviesViewPager = (ViewPager) findViewById(R.id.container);

        recommendedList = new ArrayList<Movie>();
        Intent previousScreen = getIntent();

          /*
          Screen's number saved if the user was in:
          Wish List : 0
          Recommend : 1
          Liked : 2
         */
        int screenNumber;
        if (previousScreen.getExtras().containsKey(Constants.WISH_LIST_PARCELED)) {
            WishListFragment wishListScreen = (WishListFragment) getFragmentManager().findFragmentById(R.id.screen_content);
            screenNumber = 0;
            screenTitle.setText(R.string.wish_list);
            if (wishListScreen == null) {
                wishListScreen = new WishListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen_content, wishListScreen);
                fragmentTransaction.commit();
            }

        } else if (previousScreen.getExtras().containsKey(Constants.MOVIE_SECTION)) {
            screenNumber = 1;
            screenTitle.setText(R.string.recommended);

            if (savedInstanceState != null) {
                movieNumber = savedInstanceState.getLong(Constants.USER_MOVIES);
            } else if (previousScreen.getExtras().containsKey(Constants.USER_MOVIES)) {
                movieNumber = previousScreen.getExtras().getLong(Constants.USER_MOVIES);
            }

            if (savedInstanceState != null) {
                recommendedList.clear();
                recommendedList = savedInstanceState.getParcelableArrayList(Constants.RECOMMENDED_MOVIE);
            } else if (previousScreen.getExtras().containsKey(Constants.LIKE_MOVIE)) {
                recommendedList = previousScreen.getExtras().getParcelableArrayList(Constants.LIKE_MOVIE);
                likedList = previousScreen.getExtras().getParcelableArrayList(Constants.LIKE_MOVIE);
            }

            if (movieNumber == 0 || recommendedList.size() == 0) {
                final DBClient dbClient = new DBClient();
                class fetchDataMovies extends AsyncTask<String, Void, List<Movie>> {
                    @Override
                    protected List<Movie> doInBackground(String... strings) {
                        List<Movie> movielist = new ArrayList<Movie>();

                        try {
                            movielist = dbClient.getSomePopularMovies(20);
                        } catch (DBClientException e) {
                            e.printStackTrace();
                        }
                        return movielist;
                    }
                }

                try {
                    recommendedList = new fetchDataMovies().execute("").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                final DBClient client = new DBClient();
                int luckyNum = client.randomNum(0, recommendedList.size() - 1);

                class fetchRecommendedMovies extends AsyncTask<String, Void, List<Movie>> {
                    @Override
                    protected List<Movie> doInBackground(String... strings) {
                        List<Movie> recommendedMovies = new ArrayList<Movie>();
                        try {
                            recommendedMovies = client.fetchSimilarList(strings[0]);
                        } catch (DBClientException e) {
                            e.printStackTrace();
                        }
                        return recommendedMovies;
                    }
                }
                try {
                    Movie luckyMovie = recommendedList.get(luckyNum);
                    recommendedList.clear();
                    recommendedList = new fetchRecommendedMovies().execute(luckyMovie.getMovieId()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Movie m : likedList
            ) {
                if (recommendedList.contains(m)) {
                    recommendedList.remove(m);
                }
            }

            RecommendedPagerAdapter recommendedPagerAdapter = new RecommendedPagerAdapter(getSupportFragmentManager());
            if (recommendedList.size() > 10) {
                recommendedPagerAdapter.setList(recommendedList.subList(0, 10));
            }
            recommendedPagerAdapter.setList(recommendedList);
            moviesViewPager.setAdapter(recommendedPagerAdapter);

        } else if (previousScreen.getExtras().containsKey(Constants.LIKED_MOVIE_PARCELED)) {
            screenNumber = 2;
            screenTitle.setText(R.string.like_history);
            List<Movie> movies = previousScreen.getParcelableArrayListExtra(Constants.LIKED_MOVIE_PARCELED);

            LikedFragment likedFragment = (LikedFragment) getFragmentManager().findFragmentById(R.id.screen_content);
            if (likedFragment == null) {
                likedFragment = new LikedFragment();
                likedFragment.likedMovies(movies);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen_content, likedFragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideDelay(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleUI() {
        if (isVisible) {
            hideUI();
        } else {
            showNavBar();
        }
    }

    private void hideUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        controlView.setVisibility(View.GONE);
        isVisible = false;
        handler.removeCallbacks(mShowPart2Runnable);
        handler.postDelayed(hideRunnable, Constants.DELAY_ANIMATION);
    }

    @SuppressLint("InlinedApi")
    private void showNavBar() {
        // Method to show the nav bar
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        isVisible = true;
        // Displaying the UI after seconds
        handler.removeCallbacks(hideRunnable);
        handler.postDelayed(mShowPart2Runnable, Constants.DELAY_ANIMATION);
    }

    private void hideDelay(int delayMillis) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayMillis);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.USER_MOVIES, movieNumber);
        outState.putParcelableArrayList(Constants.RECOMMENDED_MOVIE, (ArrayList<? extends Parcelable>) recommendedList);
    }
}
package amner.android.com.movies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import amner.android.com.movies.sections.ScreenActivity;
import amner.android.com.movies.utils.Constants;
import amner.android.com.movies.utils.Movie;
import amner.android.com.movies.utils.DatabaseUtils;

public class SplashActivity extends BasicActivity implements View.OnClickListener {

    private DatabaseUtils databaseUtils = new DatabaseUtils();
    private FirebaseAuth firebaseAuth;
    private int number = 0;
    private Long[] total;
    private List<Movie> moviesLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode setExplode = new Explode();
            setExplode.setDuration(300);
            Fade setFade = new Fade();
            setFade.setDuration(400);
            getWindow().setEnterTransition(setFade);
            getWindow().setExitTransition(setExplode);
            getWindow().setReenterTransition(setExplode);
        }
        setContentView(R.layout.activity_splash);
        findViewById(R.id.wish_list_button).setOnClickListener(this);
        findViewById(R.id.liked_button).setOnClickListener(this);
        findViewById(R.id.recommended_button).setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        this.moviesLiked = new ArrayList<Movie>();
        this.total = databaseUtils.checkIfExists(firebaseAuth.getCurrentUser().getUid());
        this.moviesLiked = databaseUtils.getLikedMovies(firebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_account_details:
                Toast.makeText(getApplicationContext(), R.string.Edit_account_details, Toast.LENGTH_LONG).show();
                return true;
            case R.id.clear_movie_list:
                if (databaseUtils.removeAllLists(firebaseAuth.getCurrentUser().getUid())) {
                    Toast.makeText(getApplicationContext(), R.string.Clear_movie_list, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.wish_list_button) {
            Intent wishIntent = new Intent(this, ScreenActivity.class);
            wishIntent.putExtra(Constants.WISH_LIST_PARCELED, "");
            startActivity(wishIntent);

        } else if (i == R.id.recommended_button) {
            Movie screenMovie = new Movie();
            Movie newMovie = new Movie();
            ArrayList<Movie> movies = new ArrayList<Movie>();
            movies.add(screenMovie);
            movies.add(newMovie);

            // Open the Recommended screen using the ScreenActivity
            Intent recommendedIntent = new Intent(this, ScreenActivity.class);
            recommendedIntent.putExtra(Constants.MOVIE_SECTION, movies);
            recommendedIntent.putExtra(Constants.USER_MOVIES, total[0]);
            recommendedIntent.putParcelableArrayListExtra(Constants.LIKE_MOVIE, (ArrayList<? extends Parcelable>) this.moviesLiked);
            startActivity(recommendedIntent);
        }

        else if (i == R.id.liked_button) {
            Intent likedIntent = new Intent(this, ScreenActivity.class);
            likedIntent.putExtra(Constants.LIKED_MOVIE_PARCELED, "");
            startActivity(likedIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

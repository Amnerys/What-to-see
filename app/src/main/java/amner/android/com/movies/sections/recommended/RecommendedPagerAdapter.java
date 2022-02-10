package amner.android.com.movies.sections.recommended;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import amner.android.com.movies.utils.Movie;

public class RecommendedPagerAdapter extends FragmentPagerAdapter {

    private List<Movie> recommendedMoviesList;

    public RecommendedPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Movie> list){
        this.recommendedMoviesList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return RecommendedFragment.newInstance(recommendedMoviesList.get(i));
    }

    @Override
    public int getCount() {
        return recommendedMoviesList.size();
    }
}

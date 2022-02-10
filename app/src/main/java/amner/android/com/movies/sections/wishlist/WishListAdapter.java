package amner.android.com.movies.sections.wishlist;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import amner.android.com.movies.R;
import amner.android.com.movies.utils.Movie;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishViewHolder> {

    private final List<Movie> wishList;

    public WishListAdapter(List<Movie> items, Context wishListContext) {
        wishList = items;
        Context context = wishListContext;
    }

    @Override
    public WishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_item, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WishViewHolder holder, int position) {
          holder.movieName.setText(wishList.get(position).getmTitle());
          holder.movieYear.setText(wishList.get(position).getReleaseYear());
          holder.summaryTitle.setText(R.string.title_summary);
          holder.movieSummary.setText(wishList.get(position).getOverview());

        try{
            String wishMovieImage = wishList.get(position).getPoster();
            String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(MOVIE_POSTER_URL + wishMovieImage).into(holder.movieImage);
        }catch (IllegalStateException e){
            holder.movieImage.setImageResource(R.drawable.baseline_movie_filter_black_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public class WishViewHolder extends RecyclerView.ViewHolder {
        final ImageView movieImage;
        final TextView movieName;
        final TextView movieYear;
        final TextView summaryTitle;
        final TextView movieSummary;

        WishViewHolder(View view) {
            super(view);
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
            movieName = (TextView) view.findViewById(R.id.movie_name);
            movieYear = (TextView) view.findViewById(R.id.movie_year);
            summaryTitle = (TextView) view.findViewById(R.id.summary_title);
            movieSummary = (TextView) view.findViewById(R.id.movie_summary);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + movieName.getText() + "'";
        }
    }
}

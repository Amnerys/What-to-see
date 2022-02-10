package amner.android.com.movies.sections.liked;

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

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.LikedItemViewHolder> {

    private final List<Movie> likeList;
    public LikedAdapter(List<Movie> movies) {
        likeList = movies;
    }

    @Override
    public LikedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_liked, parent, false);
        return new LikedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedItemViewHolder holder, int position) {
        holder.nameLike.setText(likeList.get(position).getmTitle());
        holder.yearLike.setText(likeList.get(position).getReleaseYear());
        try{
            String posterPath = likeList.get(position).getPoster();
            String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(MOVIE_POSTER_URL + posterPath).into(holder.imageLike);
        }catch (IllegalStateException e){
            holder.imageLike.setImageResource(R.drawable.baseline_movie_filter_black_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public class LikedItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageLike;
        final TextView nameLike;
        final TextView yearLike;

        LikedItemViewHolder(View view) {
            super(view);
            imageLike = (ImageView) view.findViewById(R.id.movie_image);
            nameLike = (TextView) view.findViewById(R.id.movie_name);
            yearLike = (TextView) view.findViewById(R.id.movie_year);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameLike.getText() + "'";
        }
    }
}

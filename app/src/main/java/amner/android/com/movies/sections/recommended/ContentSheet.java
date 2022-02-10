package amner.android.com.movies.sections.recommended;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amner.android.com.movies.R;
import amner.android.com.movies.utils.Movie;

public class ContentSheet extends BottomSheetDialogFragment {

    private final static String TAG = ContentSheet.class.getSimpleName();
    private Movie movie;

    /**
     * Constructor
     */
    public ContentSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_content, container, false);
        TextView movieSummary = (TextView) view.findViewById(R.id.tex_view_summary);
        movieSummary.setText(movie.getOverview());
        return view;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

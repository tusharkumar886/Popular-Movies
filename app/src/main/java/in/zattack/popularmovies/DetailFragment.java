package in.zattack.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        String title= intent.getStringExtra("Title");
        String poster= intent.getStringExtra("Poster");
        String date = intent.getStringExtra("Date");
        String rating= intent.getStringExtra("Rating");
        String summary= intent.getStringExtra("Summary");

        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.poster);
        Picasso.with(getContext()).load(poster).into(imageView);
        ((TextView) rootView.findViewById(R.id.releaseDate)).setText(date);
        ((TextView) rootView.findViewById(R.id.ratingDetail)).setText(rating + getString(R.string.by10));
        ((TextView) rootView.findViewById(R.id.movieSummary)).setText(summary);

        return rootView;
    }
}

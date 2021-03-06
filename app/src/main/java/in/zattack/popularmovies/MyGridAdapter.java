package in.zattack.popularmovies;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tushar on 03-Oct-16.
 */

public class MyGridAdapter extends ArrayAdapter<Movie> {

    public MyGridAdapter(FragmentActivity context, List<Movie> resource){
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie,parent,false);

        Movie currentMovie = getItem(position);

        ImageView imageView = (ImageView)itemView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(currentMovie.movieImage).into(imageView);

        TextView textView = (TextView)itemView.findViewById(R.id.movieName);
        textView.setText(currentMovie.movieName);

        return itemView;
    }

}

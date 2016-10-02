package in.zattack.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MyGridAdapter adapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Movie[] movieList = {new Movie(R.drawable.island, "Island"),
                new Movie(R.drawable.jeep, "Jeep"),
                new Movie(R.drawable.wave, "Wave"),
                new Movie(R.drawable.wolf, "Wolf")};
                adapter = new MyGridAdapter(getActivity(), Arrays.asList(movieList));
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
                gridview.setAdapter(adapter);
                return rootView;
    }
}

package in.zattack.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MyGridAdapter adapter;
    String sort = "popular";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainactivityfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.top_rated) {
            sort = "topRated";
            MovieDetails movie = new MovieDetails();
            movie.execute(sort);
            return true;
        }
        else if(id == R.id.popular) {
            sort = "popular";
            MovieDetails movie = new MovieDetails();
            movie.execute(sort);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        adapter = new MyGridAdapter(getActivity(), new ArrayList<Movie>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setAdapter(adapter);
        MovieDetails movie = new MovieDetails();
        movie.execute(sort);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie forecast = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), Detail.class)
                        .putExtra("Title", forecast.movieName)
                        .putExtra("Poster", forecast.movieImage)
                        .putExtra("Date", forecast.releaseDate)
                        .putExtra("Rating", forecast.rating)
                        .putExtra("Summary", forecast.summary);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private Movie[] getmovieDataFromJson(String movieInfo) throws JSONException, NullPointerException {

        final String MDB_RESULT = "results";
        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";
        final String baseUrl = "https://image.tmdb.org/t/p/w342";
        final String MDB_DATE = "release_date";
        final String MDB_VOTE = "vote_average";
        final String MDB_OVERVIEW = "overview";
        try {
            JSONObject moviejson = new JSONObject(movieInfo);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);

            Movie[] movieDetails = new Movie[20];

            for (int i = 0; i < 20; i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);

                String movietitle = currentMovie.getString(MDB_TITLE);
                String movieImageURL = baseUrl + currentMovie.getString(MDB_POSTER) ;
                String movieReleaseDate = currentMovie.getString(MDB_DATE);
                String movieRating = currentMovie.getString(MDB_VOTE);
                String movieOverView = currentMovie.getString(MDB_OVERVIEW);

                movieDetails[i] = new Movie(movieImageURL, movietitle, movieReleaseDate, movieRating, movieOverView);
            }
            return movieDetails;
        }catch (NullPointerException e){
            return null;
        }
    }



    public class MovieDetails extends AsyncTask<String,Void,Movie[]>{

        private final String LOG_TAG = MovieDetails.class.getSimpleName();

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                adapter.clear();
                for (Movie oneMovie : result) {
                    adapter.add(oneMovie);
                }
            }
        }


        @Override
        protected Movie[] doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieinfoJsonstr = null;
            try {

                //final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                //final String APPID_PARAM = "APPID_PARAM";

                //String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                //String sortBy = "sort_by=" + params[0]+ ".desc";
                String baseUrl = "";
                String apiKey = "&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

                if(params[0] == "popular")
                    baseUrl = "https://api.themoviedb.org/3/movie/popular?language=en-US";
                else
                    if(params[0] == "topRated"){
                        baseUrl = "https://api.themoviedb.org/3/movie/top_rated?language=en-US";
                    }

                //Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                //       .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                //       .build();
                URL url = new URL(baseUrl.concat(apiKey));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieinfoJsonstr = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                movieinfoJsonstr = null;

            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getmovieDataFromJson(movieinfoJsonstr);
            } catch (JSONException e) {
                Log.e(LOG_TAG,"Error",e);
            }
            return null;
        }


    }
}

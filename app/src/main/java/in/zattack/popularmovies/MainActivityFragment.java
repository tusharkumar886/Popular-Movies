package in.zattack.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        adapter = new MyGridAdapter(getActivity(), new ArrayList<Movie>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setAdapter(adapter);
        MovieDetails weather = new MovieDetails();
        weather.execute();
        return rootView;
    }

    private Movie[] getmovieDataFromJson(String movieInfo) throws JSONException, NullPointerException {

        final String MDB_RESULT = "results";
        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";
        final String baseUrl = "https://image.tmdb.org/t/p/w500";
        try {
            JSONObject moviejson = new JSONObject(movieInfo);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);

            Movie[] movieDetails = new Movie[16];

            for (int i = 0; i < 16; i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);

                String movietitle = currentMovie.getString(MDB_TITLE);
                String movieImageURL = baseUrl + currentMovie.getString(MDB_POSTER) ;

                movieDetails[i] = new Movie(movieImageURL, movietitle);
            }
            return movieDetails;
        }catch (NullPointerException e){
            return null;
        }
    }



    public class MovieDetails extends AsyncTask<Void,Void,Movie[]>{

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
        protected Movie[] doInBackground(Void... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieinfoJsonstr = null;
            try {

                //final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                //final String APPID_PARAM = "APPID_PARAM";

                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                String apiKey = "&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
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

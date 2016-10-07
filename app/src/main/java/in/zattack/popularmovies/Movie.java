package in.zattack.popularmovies;

/**
 * Created by tushar on 02-Oct-16.
 */

public class Movie {
    String movieImage;
    String movieName;
    String releaseDate;
    String rating;
    String summary;

    public Movie(String image, String name, String date, String vote, String plot) {
        this.movieImage = image;
        this.movieName = name;
        this.releaseDate = date;
        this.rating = vote;
        this.summary = plot;
    }
}

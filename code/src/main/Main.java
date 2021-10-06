//in the name of God
package main;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<movie> movies = new ArrayList<>();
        ArrayList<movie_rating> movies_rating = new ArrayList<>();
        read_files(movies, movies_rating);
        menu(movies, movies_rating);

    }

    static void menu(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating) {
        Scanner sc = new Scanner(System.in);
        System.out.println("welcome to imdb\nplease enter a number:\n1.signup\n2.login\n3.top 10 movies");
        switch (sc.nextInt()) {
            case 1:
                signup();
                break;
            case 2:
                login();
                break;
            case 3:
                top_10_movies(movies, movies_rating);
                break;
        }
    }

    static void signup() {
    }

    static void login() {
    }

    static void top_10_movies(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating) {
        ArrayList<movie_rating> j = movies_rating;
        System.out.println("------------------------------");
        for (int i = 0; i < 10; i++) {
            for (movie a : movies) {
                if (a.titleId.equals(Collections.max(j).tconst)) {
                    System.out.println("number "+(i+1));
                    System.out.println("title: " + a.title + "\nregion: " + a.region + "\nlanguage: " + a.language + "\ntypes: " + a.types[0] + "\nattributes: " + a.attributes[0] + "\nisOriginalTitle: " + a.isOriginalTitle);
                    for (movie_rating b : j) {
                        if (b.tconst.equals(a.titleId)) {
                            System.out.println("averageRating: " + b.averageRating + "\nvotes: " + b.numVotes);
                            j.remove(b);
                            break;
                        }
                    }
                    System.out.println("------------------------------");
                    break;
                }

            }
        }
    }

    static void read_files(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating) throws IOException {
        FileReader fin = new FileReader("../title.akas.txt");
        Scanner sc = new Scanner(fin);
        sc.nextLine();
        movie a;
        while (sc.hasNext()) {
            String[] arr = sc.nextLine().split("\t");
            boolean isOriginalTitle;
            if (Integer.parseInt(arr[7]) == 0)
                isOriginalTitle = false;
            else
                isOriginalTitle = true;
            a = new movie(arr[0], arr[2], arr[3], arr[4], Integer.parseInt(arr[1]), isOriginalTitle, new String[]{arr[5]}, new String[]{arr[6]});
            movies.add(a);
        }
        fin.close();
//--------------------------------------------------------------------
        FileReader fin2 = new FileReader("../title.ratings.txt");
        Scanner sc2 = new Scanner(fin2);
        movie_rating b;
        sc2.nextLine();
        while (sc2.hasNext()) {
            String[] arr = sc2.nextLine().split("\t");

            b = new movie_rating(arr[0], Double.parseDouble(arr[1]), Integer.parseInt(arr[2]));
            movies_rating.add(b);
        }
        fin2.close();
    }

}

class movie {
    String titleId, title, region, language;
    int ordering;
    boolean isOriginalTitle;
    String[] types, attributes;

    public movie(String titleId, String title, String region, String language, int ordering, boolean isOriginalTitle, String[] types, String[] attributes) {
        this.titleId = titleId;
        this.title = title;
        this.region = region;
        this.language = language;
        this.ordering = ordering;
        this.isOriginalTitle = isOriginalTitle;
        this.types = types;
        this.attributes = attributes;
    }
}

class movie_rating implements Comparable {
    String tconst;
    double averageRating;
    int numVotes;

    public movie_rating(String tconst, double averageRating, int numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    @Override
    public int compareTo(Object o) {

        if (averageRating > ((movie_rating) o).averageRating)
            return 1;
        else
            return 0;
    }
}


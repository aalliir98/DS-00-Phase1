//in the name of God
package main;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<movie> movies = new ArrayList<>();
        ArrayList<movie_rating> movies_rating = new ArrayList<>();
        ArrayList<user> users = new ArrayList<>();
        read_files(movies, movies_rating);
        System.out.println("welcome to imdb");
        while (true) {
            menu(movies, movies_rating, users);
        }
    }

    static void menu(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating, ArrayList<user> users) throws FileNotFoundException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nplease enter a number:\n1.signup\n2.login\n3.top 10 movies");
        switch (sc.nextInt()) {
            case 1:
                signup(users);
                break;
            case 2:
                login(movies, movies_rating, users);
                break;
            case 3:
                top_10_movies(movies, movies_rating);
                break;
        }
    }

    static void signup(ArrayList<user> users) {
        Scanner sc = new Scanner(System.in);
        boolean exist=false;
        ArrayList<String> voted_list = new ArrayList<>();
        voted_list.add("");
        System.out.println("please enter username and password to sign up");
        String username = sc.next();
        String password = sc.next();
        for (user h:users)
            if (h.username.equals(username)&&h.password.equals(password)){
                exist=true;
                break;
            }
        if (!exist) {
            user a = new user(username, password, voted_list);
            users.add(a);
            System.out.println("signup Done");
        }
        else
            System.out.println("there is another account with this username and password");

    }

    static void login(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating, ArrayList<user> users) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        int y = 0;
        System.out.println("please enter username and password to login");
        String username = sc.next();
        String password = sc.next();
        for (user a : users) {
            if (a.username.equals(username) && a.password.equals(password)) {
                y++;
                show_movies(movies, movies_rating, a);
                write_files(movies_rating);
            }
        }
        if (y == 0)
            System.out.println("there is no account");
    }

    static void top_10_movies(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating) throws InterruptedException {
        ArrayList<movie_rating> j =new ArrayList<>(movies_rating);
        System.out.println("------------------------------");
        for (int i = 0; i < 10; i++) {
            for (movie a : movies) {
                if (a.titleId.equals(Collections.max(j).tconst)) {
                    System.out.println("number " + (i + 1));
                    System.out.println("title: " + a.title + "\nregion: " + a.region + "\nlanguage: " + a.language + "\ntypes: " + a.types[0] + "\nattributes: " + a.attributes[0] + "\nisOriginalTitle: " + a.isOriginalTitle);
                    for (movie_rating b : j) {
                        if (b.tconst.equals(a.titleId)) {
                            System.out.println("averageRating: " + b.averageRating + "\nvotes: " + b.numVotes);
                            j.remove(b);
                            break;
                        }
                    }
                    System.out.println("------------------------------");
                    Thread.sleep(1000);
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

    static void show_movies(ArrayList<movie> movies, ArrayList<movie_rating> movies_rating, user n) {
        int g = 0;

        Scanner sc = new Scanner(System.in);
        for (movie a : movies) {
            boolean voted = false;
            System.out.println("title: " + a.title + "\nregion: " + a.region + "\nlanguage: " + a.language + "\ntypes: " + a.types[0] + "\nattributes: " + a.attributes[0] + "\nisOriginalTitle: " + a.isOriginalTitle);
            for (movie_rating b : movies_rating) {
                if (b.tconst.equals(a.titleId)) {
                    System.out.println("averageRating: " + b.averageRating + "\nvotes: " + b.numVotes);
                    int t = n.voted_list.size();
                    for (int i = 0; i < t; i++) {
                        if (n.voted_list.get(i).equals(b.tconst)) {
                            voted = true;
                            break;
                        }
                    }
                    if (!voted) {
                        System.out.println("Do you want vote this movie?(enter number:1.yes 2.no 3.exit)");
                        switch (sc.nextInt()) {
                            case 1:
                                System.out.println("Enter your vote:(between 1 to 10)");
                                b.averageRating = (sc.nextDouble() + (b.averageRating * b.numVotes)) / ++b.numVotes;
                                n.voted_list.add(b.tconst);
                                break;
                            case 2:
                                break;
                            case 3:
                                g++;
                                break;
                        }
                        break;
                    }
                }
            }
            System.out.println("------------------------------");
            if (g == 1)
                break;
        }
    }

    static void write_files(ArrayList<movie_rating> movies_rating) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("../title.ratings.txt");
        out.print("tconst\taverageRating\tnumVotes\n");
        for (movie_rating a : movies_rating) {
            out.print(a.tconst + "\t" + a.averageRating + "\t" + a.numVotes);
            out.println();
        }
        out.close();
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

class user {
    String username, password;
    ArrayList<String> voted_list;

    public user(String username, String password, ArrayList<String> voted_list) {
        this.username = username;
        this.password = password;
        this.voted_list = voted_list;
    }
}
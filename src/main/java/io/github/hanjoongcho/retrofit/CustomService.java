package io.github.hanjoongcho.retrofit;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CustomService {
    public static final String API_URL_CUSTOM = "https://raw.githubusercontent.com";
    public static final String API_URL_GITHUB = "https://api.github.com";
    
    public static class Contributor {
        public final String login;
        public final String avatar_url;
        public final int contributions;
        
        public Contributor(String login, int contributions, String avatarUrl) {
            this.login = login;
            this.contributions = contributions;
            this.avatar_url = avatarUrl;
        }
    }
    
    public static class User {
        public final String name;
        public final String location;
        public final String blog;
        public final String avatar_url;

        public User(String name, String location, String blog, String avatar_url) {
            this.name = name;
            this.location = location;
            this.blog = blog;
            this.avatar_url = avatar_url;
        }
    }
    
    public interface RepositoryContributors {
        @GET("/hanjoongcho/aaf-easydiary/master/data/contributors.json")
        Call<List<Contributor>> contributors();
    }
    
    public interface UserInformations {
        @GET("/users/{login}")
        Call<User> user(@Path("login") String login);
    }
    
    public static List<Contributor> findContributors() {
        List<Contributor> contributors = null;
     // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit =
                new Retrofit.Builder()
                .baseUrl(API_URL_CUSTOM)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        // Create an instance of our GitHub API interface.
        RepositoryContributors repositoryContributors = retrofit.create(RepositoryContributors.class);
        
        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = repositoryContributors.contributors();
        
        // Fetch and print a list of the contributors to the library.
        try {
            contributors = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return contributors;
    }
    
    public static void getUserInformaion(String loginId) {
        Retrofit retrofit =
                new Retrofit.Builder()
                .baseUrl(API_URL_GITHUB)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        UserInformations userInformations = retrofit.create(UserInformations.class);
        Call<User> call = userInformations.user(loginId);
        try {
            User user = call.execute().body();
//            System.out.println(user.name);
            System.out.println(new Gson().toJson(user, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String... args) throws IOException {
        
        for (Contributor contributor : findContributors()) {
            System.out.println(String.format("%s %d %s", contributor.login, contributor.contributions, contributor.avatar_url));
            getUserInformaion(contributor.login);
        }
    }
}

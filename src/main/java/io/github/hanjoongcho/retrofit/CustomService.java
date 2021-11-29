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
    
    public class Contributor {
        public User user;
        public String login;
        public int contributions;
    }
    
    public class User {
        public String name;
        public String location;
        public String blog;
        public String avatar_url;
    }
    
    public interface CustomData {
        @GET("/hanjoongcho/aaf-easydiary/master/data/contributors.json")
        Call<List<Contributor>> findContributors();
        
        @GET("/users/{login}")
        Call<User> findUser(@Path("login") String login);
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
        CustomData customData = retrofit.create(CustomData.class);
        
        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = customData.findContributors();
        
        // Fetch and print a list of the contributors to the library.
        try {
            contributors = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return contributors;
    }
    
    public static User getUserInformaion(String loginId) {
        User user = null;
        Retrofit retrofit =
                new Retrofit.Builder()
                .baseUrl(API_URL_GITHUB)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        CustomData customData = retrofit.create(CustomData.class);
        Call<User> call = customData.findUser(loginId);
        try {
            user = call.execute().body();
//            System.out.println(user.name);
            System.out.println(new Gson().toJson(user, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    public static void main(String... args) throws IOException {
        for (Contributor contributor : findContributors()) {
//            contributor.user = getUserInformaion(contributor.login); 
            System.out.println(new Gson().toJson(contributor, Contributor.class));
        }
        
    }
}

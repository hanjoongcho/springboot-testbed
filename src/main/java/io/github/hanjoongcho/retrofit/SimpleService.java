package io.github.hanjoongcho.retrofit;

import java.io.IOException;
import java.util.List;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class SimpleService {
    public static final String API_URL = "https://api.github.com";
    
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
    
    public interface RepositoryContributors {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
    }
    
    public static void main(String... args) throws IOException {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit =
                new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        // Create an instance of our GitHub API interface.
        RepositoryContributors repositoryContributors = retrofit.create(RepositoryContributors.class);
        
        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = repositoryContributors.contributors("hanjoongcho", "aaf-easydiary");
        
        // Fetch and print a list of the contributors to the library.
        List<Contributor> contributors = call.execute().body();
        for (Contributor contributor : contributors) {
            System.out.println(String.format("%s %d %s", contributor.login, contributor.contributions, contributor.avatar_url));
        }
    }
}

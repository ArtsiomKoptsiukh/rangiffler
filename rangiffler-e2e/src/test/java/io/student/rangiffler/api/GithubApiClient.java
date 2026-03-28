package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.student.rangiffler.config.Config;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Objects;

public class GithubApiClient {

    public static final String GITHUB_TOKEN_ENV_VAR = "GITHUB_TOKEN";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().githubUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GithubApi githubApi = retrofit.create(GithubApi.class);

    @SneakyThrows
    public String issueStatus(String issueNumber) {
        JsonNode response = githubApi.issue("Bearer " + System.getenv(GITHUB_TOKEN_ENV_VAR), issueNumber)
                .execute()
                .body();
        return Objects.requireNonNull(response).get("state").asText();
    }

}

package com.agileengineapi;

import com.agileengineapi.core.TestBase;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static com.agileengineapi.util.Helpers.getResponse;
import static com.agileengineapi.util.Helpers.getUniqueTweet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class TEST extends TestBase {

    private final static String BASE_URL = "https://api.twitter.com/1.1/statuses";
    private String uniqueText = getUniqueTweet("Test"),
            tweetID;
    private JsonPath json;

    @Test
    public void postTweetPostiveTest() throws Exception {

        Response response = getResponse(Verb.POST, BASE_URL + "/update.json?status=" + uniqueText, service, accessToken);

        assertThat(response.getCode(), equalTo(200));

        json = JsonPath.from(response.getBody());
        tweetID = json.getString("id_str");

    }

    @Test(dependsOnMethods = "postTweetPostiveTest")
    public void duplicateErrorCodeNegativeTest() throws Exception {

        Response response = getResponse(Verb.POST, BASE_URL + "/update.json?status=" + uniqueText, service, accessToken);

        assertThat(response.getCode(), equalTo(403));

    }

    @Test(dependsOnMethods = "duplicateErrorCodeNegativeTest")
    public void verifyTweetFieldsPositiveTest() throws Exception {

        Response response = getResponse(Verb.GET, BASE_URL + "/home_timeline.json", service, accessToken);

        assertThat(response.getCode(), equalTo(200));

        JsonPath actualJson = JsonPath.from(response.getBody());

        assertThat(actualJson.getString("id_str[0]"), equalTo(json.getString("id_str")));
        assertThat(actualJson.getString("text[0]"), equalTo(json.getString("text")));
        assertThat(actualJson.getString("created_at[0]"), equalTo(json.getString("created_at")));
        assertThat(actualJson.getString("retweet_count[0]"), equalTo(json.getString("retweet_count")));

    }

    @Test(dependsOnMethods = "verifyTweetFieldsPositiveTest")
    public void retweetPositiveTest() throws Exception {

        Response response = getResponse(Verb.POST, BASE_URL + "/retweet/" + tweetID + ".json", service, accessToken);

        JsonPath actualJson = JsonPath.from(response.getBody());

        assertThat(actualJson.getInt("retweet_count"), equalTo(1));
    }

    @Test(dependsOnMethods = "retweetPositiveTest")
    public void deleteTweetPositiveTest() throws Exception {

        Response response = getResponse(Verb.POST, BASE_URL + "/destroy/" + tweetID + ".json", service, accessToken);

        assertThat(response.getCode(), equalTo(200));

        Response response2 = getResponse(Verb.GET, BASE_URL + "/home_timeline.json", service, accessToken);

        assertThat(response2.getCode(), equalTo(200));

        JsonPath actualJson = JsonPath.from(response2.getBody());

        assertThat(actualJson.getString("id_str[0]"), not(equalTo(json.getString("id_str"))));
    }

}

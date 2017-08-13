package com.agileengineapi.util;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.util.Random;

public class Helpers {

    public static String getUniqueTweet(String text) {
        Random random = new Random();
        int randomNumber = random.nextInt(99) + 1;

        return text.concat(String.valueOf(randomNumber));
    }

    public static Response getResponse(Verb verb, String url, OAuth10aService service, OAuth1AccessToken accessToken) throws Exception {

        OAuthRequest request = new OAuthRequest(verb, url);
        service.signRequest(accessToken, request);

        return service.execute(request);
    }

}

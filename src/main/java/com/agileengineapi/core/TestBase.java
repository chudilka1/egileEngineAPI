package com.agileengineapi.core;

import com.agileengineapi.pages.AuthorizationPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.Selenide.open;

public class TestBase {

    private final static String API_KEY = "SqoZ67o3RLUkrkL2uH7U0ZclP",
            API_SECRET = "SEBNGyrqroVgan5a8XvQCnarpEJ5vjgOXxD5EGREGAjDXyjHtd";

    protected OAuth10aService service;
    protected OAuth1AccessToken accessToken = null;

    @BeforeSuite
    public void setUp() throws Exception {

        service = new ServiceBuilder(API_KEY)
                .apiSecret(API_SECRET)
                .build(TwitterApi.instance());

        OAuth1RequestToken requestToken = service.getRequestToken();
        String authUrl = service.getAuthorizationUrl(requestToken);

        //get oauth verifier
        Configuration.browser = WebDriverRunner.HTMLUNIT;
        open(authUrl);
        AuthorizationPage authorization = new AuthorizationPage();
        String oauthVerifier = authorization.getOauthVerifier();

        accessToken = service.getAccessToken(requestToken, oauthVerifier);

    }

}

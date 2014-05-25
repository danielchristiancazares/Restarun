package com.example.yelp;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class YelpAuth extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

}
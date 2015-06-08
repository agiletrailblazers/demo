package com.atb.demo.stagger.mustache;

import com.wordnik.swagger.model.Authorization;
import com.wordnik.swagger.model.AuthorizationScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MustacheAuthorization {
    private final List<AuthorizationScope> authorizationScopes = new ArrayList<AuthorizationScope>();
    private final String type;

    public MustacheAuthorization(Authorization authorization) {
        this.type = authorization.type();
        Collections.addAll(this.authorizationScopes, authorization.scopes());
    }

    public List<AuthorizationScope> getAuthorizationScopes() {
        return authorizationScopes;
    }

    public String getType() {
        return type;
    }
}

package com.atb.demo.stagger.mustache;

import com.atb.demo.stagger.util.StringConversions;
import com.wordnik.swagger.model.ApiDescription;

import java.util.LinkedList;
import java.util.List;

public class MustacheApi {
    private final String description;

    private String path;

    private final String url;

    private final List<MustacheOperation> operations = new LinkedList<MustacheOperation>();

    public MustacheApi(String basePath, ApiDescription api) {
        StringConversions stringConversions = new StringConversions();

        this.path = api.path();
        if (this.path != null && !this.path.startsWith("/")) {
            this.path = "/" + this.path;
        }
        this.url = basePath + api.path();
        this.description = stringConversions.getStrInOption(api.description());
    }

    public void addOperation(MustacheOperation operation) {
        operations.add(operation);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public List<MustacheOperation> getOperations() {
        return operations;
    }

    public String getDescription() {
        return description;
    }
}

package com.urbn.stagger;

import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ResourceListing;

import java.util.ArrayList;
import java.util.List;

import com.urbn.stagger.mustache.MustachedOutputGenerator;

public abstract class AbstractDocumentSource {

    protected final LogAdapter LOG;

    protected ResourceListing serviceDocument;

    private String basePath;

    private String apiVersion;

    private MustachedOutputGenerator mustachedOutputGenerator;

    private SwaggerJsonOutputGenerator swaggerJsonOutputGenerator;

    List<ApiListing> validDocuments = new ArrayList<ApiListing>();

    public AbstractDocumentSource(LogAdapter logAdapter, String outputPath, String outputTpl,
                                  String swaggerOutput, String mustacheFileRoot, boolean useOutputFlatStructure,
                                  String fileOutputName, String swaggerUIDocBasePath) {
        LOG = logAdapter;
        mustachedOutputGenerator = new MustachedOutputGenerator(logAdapter, fileOutputName, outputPath, outputTpl, mustacheFileRoot);
        swaggerJsonOutputGenerator = new SwaggerJsonOutputGenerator(logAdapter, swaggerUIDocBasePath,
                                                                    swaggerOutput, useOutputFlatStructure);
    }

    public abstract void loadDocuments() throws Exception, GenerateException;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public List<ApiListing> getValidDocuments() {
        return validDocuments;
    }

    protected void acceptDocument(ApiListing doc) {
        String basePath;

        // will append api's basePath. However, apiReader does not read it correctly by now
        if (doc.basePath() != null) {
            basePath = this.basePath + doc.basePath();
        } else {
            basePath = this.basePath;
        }
        ApiListing newDoc = new ApiListing(doc.apiVersion(), doc.swaggerVersion(),
                basePath, doc.resourcePath(), doc.produces(), doc.consumes(),
                doc.protocols(), doc.authorizations(), doc.apis(), doc.models(), doc.description(), doc.position());
        validDocuments.add(newDoc);
    }

    public void generateSwaggerJsonOutput() throws GenerateException {
        this.swaggerJsonOutputGenerator.generateSwaggerJsonOutput(validDocuments, serviceDocument);
    }

    public void generateMustachedOutputFiles() throws GenerateException {
        this.mustachedOutputGenerator.generateMustachedOutput(validDocuments);
    }
}

package com.urbn.stagger.mavenplugin;

import com.urbn.stagger.AbstractDocumentSource;
import com.urbn.stagger.GenerateException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.util.List;

/**
 * @goal generate
 * @phase compile
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class ApiDocumentMojo extends AbstractMojo {

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private List<ApiSource> apiSources;

    public List<ApiSource> getApiSources() {
        return apiSources;
    }

    public void setApiSources(List<ApiSource> apiSources) {
        this.apiSources = apiSources;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (apiSources == null) {
            throw new MojoFailureException("You must configure at least one apiSources element");
        }
        if (useSwaggerSpec11()) {
            throw new MojoExecutionException("You may use an old version of swagger which is not supported by swagger-maven-plugin 2.0+\n" +
                "swagger-maven-plugin 2.0+ only supports swagger-core 1.3.x");
        }

        try {
            getLog().debug(apiSources.toString());
            for (ApiSource apiSource : apiSources) {
                File outputDirectory = new File(apiSource.getFileOutputPath()).getParentFile();
                if (outputDirectory != null && !outputDirectory.exists()) {
                    if (!outputDirectory.mkdirs()) {
                        throw new MojoExecutionException("Create directory[" + apiSource.getFileOutputPath() + "] for output failed.");
                    }
                }
                AbstractDocumentSource documentSource = new MavenDocumentSource(apiSource, getLog());
                documentSource.loadDocuments();
                documentSource.generateMustachedOutputFiles();
                documentSource.generateSwaggerJsonOutput();
            }

        } catch (GenerateException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private boolean useSwaggerSpec11() {
        try {
            Class<?> tryClass = Class.forName("com.wordnik.swagger.annotations.ApiErrors");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

package com.urbn.stagger.mavenplugin;

import com.urbn.stagger.GenerateException;
import com.wordnik.swagger.annotations.Api;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ApiSource {

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String locations;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String apiVersion;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String basePath;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String fileOutputTemplate;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String fileOutputPath;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String fileOutputName;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String swaggerDirectory;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    public String mustacheFileRoot;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    public boolean useOutputFlatStructure = true;

    /**
     * The tag below makes Maven aware that this is a possible configuration value in the
     * configuration block for this plugin.  Don't REMOVE IT!
     *
     * @parameter The Maven parameter in the configuration block
     */
    private String swaggerUIDocBasePath;


    public Set<Class> getValidClasses() throws GenerateException {
        Set<Class> classes = new HashSet<Class>();
        if (getLocations() == null) {
            Set<Class<?>> c = new Reflections("").getTypesAnnotatedWith(Api.class);
            classes.addAll(c);
        } else {
            if (locations.contains(";")) {
                String[] sources = locations.split(";");
                for (String source : sources) {
                    Set<Class<?>> c = new Reflections(source).getTypesAnnotatedWith(Api.class);
                    classes.addAll(c);
                }
            } else {
                classes.addAll(new Reflections(locations).getTypesAnnotatedWith(Api.class));
            }
        }
        Iterator<Class> it = classes.iterator();
        while (it.hasNext()) {
            if (it.next().getName().startsWith("com.wordnik.swagger")) {
                it.remove();
            }
        }
        return classes;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getFileOutputTemplate() {
        return fileOutputTemplate;
    }

    public void setFileOutputTemplate(String fileOutputTemplate) {
        this.fileOutputTemplate = fileOutputTemplate;
    }

    public String getMustacheFileRoot() {
        return mustacheFileRoot;
    }

    public void setMustacheFileRoot(String mustacheFileRoot) {
        this.mustacheFileRoot = mustacheFileRoot;
    }

    public boolean isUseOutputFlatStructure() {
        return useOutputFlatStructure;
    }

    public void setUseOutputFlatStructure(boolean useOutputFlatStructure) {
        this.useOutputFlatStructure = useOutputFlatStructure;
    }

    public String getFileOutputPath() {
        return fileOutputPath;
    }

    public void setFileOutputPath(String fileOutputPath) {
        this.fileOutputPath = fileOutputPath;
    }

    public String getFileOutputName() {
        return fileOutputName;
    }

    public void setFileOutputName(String fileOutputName) {
        this.fileOutputName = fileOutputName;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getSwaggerDirectory() {
        return swaggerDirectory;
    }

    public void setSwaggerDirectory(String swaggerDirectory) {
        this.swaggerDirectory = swaggerDirectory;
    }

    public void setSwaggerUIDocBasePath(String swaggerUIDocBasePath) {
        this.swaggerUIDocBasePath = swaggerUIDocBasePath;
    }

    public String getSwaggerUIDocBasePath() {
        return swaggerUIDocBasePath;
    }
}

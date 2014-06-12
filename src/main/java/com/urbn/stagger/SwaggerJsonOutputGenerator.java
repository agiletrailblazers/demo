package com.urbn.stagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wordnik.swagger.core.util.JsonSerializer;
import com.wordnik.swagger.core.util.JsonUtil;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ApiListingReference;
import com.wordnik.swagger.model.ResourceListing;
import org.apache.commons.io.FileUtils;
import scala.collection.Iterator;
import scala.collection.JavaConversions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwaggerJsonOutputGenerator {
    protected final LogAdapter LOG;

    private final String swaggerOutputPath;
    private final String swaggerUIDocBasePath;
    protected ResourceListing serviceDocument;

    private boolean useOutputFlatStructure;
    private ObjectMapper mapper = new ObjectMapper();

    public SwaggerJsonOutputGenerator(LogAdapter logAdapter, String swaggerUIDocBasePath,
                                      String swaggerOutputPath, Boolean useOutputFlatStructure) {
        LOG = logAdapter;
        this.useOutputFlatStructure = useOutputFlatStructure;
        this.swaggerOutputPath = swaggerOutputPath;
        this.swaggerUIDocBasePath = swaggerUIDocBasePath;
    }

    public void generateSwaggerJsonOutput(List<ApiListing> validDocuments, ResourceListing serviceDocument) throws GenerateException {
        this.serviceDocument = serviceDocument;

        if (swaggerOutputPath == null) {
            return;
        }
        File dir = new File(swaggerOutputPath);
        if (dir.isFile()) {
            throw new GenerateException(String.format("Swagger-outputDirectory[%s] must be a directory!", swaggerOutputPath));
        }

        if (!dir.exists()) {
            try {
                FileUtils.forceMkdir(dir);
            } catch (IOException e) {
                throw new GenerateException(String.format("Create Swagger-outputDirectory[%s] failed.", swaggerOutputPath));
            }
        }
        cleanupOlds(dir);

        prepareServiceDocument();
        writeInDirectory(dir, serviceDocument, swaggerUIDocBasePath);
        for (ApiListing doc : validDocuments) {
            //rewrite basePath in swagger-ui output file using the value in configuration file.
            writeInDirectory(dir, doc, swaggerUIDocBasePath);
        }
    }

    private void cleanupOlds(File dir) {
        if (dir.listFiles() != null) {
            File[] directoryListing = dir.listFiles();

            if (directoryListing != null) {
                for (File f : directoryListing) {
                    if (f.getName().endsWith("json")) {
                        f.delete();
                    }
                }
            }
        }
    }

    private void prepareServiceDocument() {
        List<ApiListingReference> apiListingReferences = new ArrayList<ApiListingReference>();
        for (Iterator<ApiListingReference> iterator = serviceDocument.apis().iterator(); iterator.hasNext(); ) {
            ApiListingReference apiListingReference = iterator.next();
            String newPath = apiListingReference.path();

            if (useOutputFlatStructure) {
                newPath = newPath.replaceAll("/", "_");
                if (newPath.startsWith("_")) {
                    newPath = "/" + newPath.substring(1);
                }
            }
            newPath += ".{format}";
            apiListingReferences.add(new ApiListingReference(newPath,
                    apiListingReference.description(), apiListingReference.position()));
        }
        // there's no setter of path for ApiListingReference, we need to create a new ResourceListing for new path
        serviceDocument = new ResourceListing(serviceDocument.apiVersion(), serviceDocument.swaggerVersion(),
                scala.collection.immutable.List.fromIterator(JavaConversions.asScalaIterator(apiListingReferences.iterator())),
                serviceDocument.authorizations(), serviceDocument.info());
     }

    protected String resourcePathToFilename(String resourcePath) {
        if (resourcePath == null) {
            return "service.json";
        }
        String name = resourcePath;
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }

        if (useOutputFlatStructure) {
            name = name.replaceAll("/", "_");
        }

        return name + ".json";
    }

    private void writeInDirectory(File dir, ApiListing apiListing, String basePath) throws GenerateException {
        String filename = resourcePathToFilename(apiListing.resourcePath());
        try {
            File serviceFile = createFile(dir, filename);
            String json = JsonSerializer.asJson(apiListing);
            JsonNode tree = mapper.readTree(json);
            if (basePath != null) {
                ((ObjectNode)tree).put("basePath",basePath);
            }
            JsonUtil.mapper().writerWithDefaultPrettyPrinter().writeValue(serviceFile, tree);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }

    private void writeInDirectory(File dir, ResourceListing resourceListing, String basePath) throws GenerateException {
        String filename = resourcePathToFilename(null);
        try {
            File serviceFile = createFile(dir, filename);
            String json = JsonSerializer.asJson(resourceListing);
            JsonNode tree = mapper.readTree(json);
            if (basePath != null) {
                ((ObjectNode)tree).put("basePath",basePath);
            }

            JsonUtil.mapper().writerWithDefaultPrettyPrinter().writeValue(serviceFile, tree);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }

    protected File createFile(File dir, String outputResourcePath) throws IOException {
        File serviceFile;
        int i = outputResourcePath.lastIndexOf("/");
        if (i != -1) {
            String fileName = outputResourcePath.substring(i + 1);
            String subDir = outputResourcePath.substring(0, i);
            File finalDirectory = new File(dir, subDir);
            finalDirectory.mkdirs();
            serviceFile = new File(finalDirectory, fileName);
        } else {
            serviceFile = new File(dir, outputResourcePath);
        }
        while (!serviceFile.createNewFile()) {
            serviceFile.delete();
        }
        LOG.info("Creating file " + serviceFile.getAbsolutePath());
        return serviceFile;
    }
}

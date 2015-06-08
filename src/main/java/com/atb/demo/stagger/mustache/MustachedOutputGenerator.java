package com.atb.demo.stagger.mustache;

import com.atb.demo.stagger.LogAdapter;
import com.atb.demo.stagger.GenerateException;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.wordnik.swagger.model.ApiListing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MustachedOutputGenerator {

    protected final LogAdapter LOG;

    private final String fileOutputPath;

    private final String fileOutputName;

    private final String fileOutputTemplate;

    private final String mustacheFileRoot;

    private OutputTemplate outputTemplate;

    public MustachedOutputGenerator(LogAdapter logAdapter, String fileOutputName, String fileOutputPath, String fileOutputTemplate, String mustacheFileRoot) {
        this.fileOutputPath = fileOutputPath;
        this.fileOutputName = fileOutputName;
        this.fileOutputTemplate = fileOutputTemplate;
        this.mustacheFileRoot = mustacheFileRoot;
        LOG = logAdapter;
    }

    private OutputTemplate prepareMustacheTemplate(List<ApiListing> theDocuments) throws GenerateException {
        this.outputTemplate = new OutputTemplate(theDocuments);
        return outputTemplate;
    }

    public void generateMustachedOutput(List<ApiListing> validDocuments) throws GenerateException {
        if (validDocuments.isEmpty() || (fileOutputName == null || fileOutputName.isEmpty())) {
            LOG.warn("nothing to write.");
            return;
        }

        createFileOutputDirectoryIfAbsent();

        if (fileOutputNameIsTemplatedName()) {
            for (ApiListing apiListing : validDocuments) {
                List<ApiListing> writeThisDocument = new ArrayList<ApiListing>();
                String fileToOutput = convertResourcePathToFileName(apiListing.resourcePath());
                String fileNameFromTemplate = MessageFormat.format(fileOutputName, fileToOutput);
                writeThisDocument.add(apiListing);

                writeTemplatedOutputFile(fileNameFromTemplate, writeThisDocument);
            }
        }
        else {
            String fullOutputPathWithFilename = createUntemplatedFileFullPathString();
            writeTemplatedOutputFile(fullOutputPathWithFilename, validDocuments);
        }
    }

    private void createFileOutputDirectoryIfAbsent() {
        File outputDirectoryTest = new File(fileOutputPath);
        if (!outputDirectoryTest.exists()) {
            outputDirectoryTest.mkdir();
        }
    }

    private String createUntemplatedFileFullPathString() {
        String fullOutputPathWithFile;

        if (fileOutputPath.lastIndexOf("/") > 0) {
            fullOutputPathWithFile = fileOutputPath + "/" + fileOutputName;
        }
        else {
            fullOutputPathWithFile = fileOutputPath + fileOutputName;
        }
        return fullOutputPathWithFile;
    }

    private boolean fileOutputNameIsTemplatedName() {
        return !fileOutputName.isEmpty() && fileOutputName.contains("{");
    }

    private String convertResourcePathToFileName(String resourceNameToConvert) {
        String myOutputFile = resourceNameToConvert.substring(1);
        myOutputFile = myOutputFile.replace("/", "-");
        return fileOutputPath + "/" + myOutputFile;
    }

    private void writeTemplatedOutputFile(String fileToOutputStuffTo, List<ApiListing> apiDocuments) throws GenerateException {
        prepareMustacheTemplate(apiDocuments);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(fileToOutputStuffTo);
        } catch (FileNotFoundException e) {
            throw new GenerateException(e);
        }
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));

        try {
            URL url = getTemplateUri().toURL();
            InputStreamReader reader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
            Mustache mustache = getMustacheFactory().compile(reader, fileOutputTemplate);

            mustache.execute(writer, outputTemplate).flush();
            writer.close();
            LOG.info("Done!");
        } catch (MalformedURLException e) {
            throw new GenerateException(e);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }

    private URI getTemplateUri() throws GenerateException {
        URI uri;
        try {
            uri = new URI(fileOutputTemplate);
        } catch (URISyntaxException e) {
            File file = new File(fileOutputTemplate);
            if (!file.exists()) {
                throw new GenerateException("Template " + file.getAbsoluteFile()
                        + " not found. You can go to https://github.com/kongchen/api-doc-template to get templates.");
            }
            uri = file.toURI();
        }
        if (!uri.isAbsolute()) {
            File file = new File(fileOutputTemplate);
            if (!file.exists()) {
                throw new GenerateException("Template " + file.getAbsoluteFile()
                        + " not found. You can go to https://github.com/kongchen/api-doc-template to get templates.");
            } else {
                uri = new File(fileOutputTemplate).toURI();
            }
        }
        return uri;
    }

    private DefaultMustacheFactory getMustacheFactory() {
        if (mustacheFileRoot == null) {
            return new DefaultMustacheFactory();
        } else {
            return new DefaultMustacheFactory(new File(mustacheFileRoot));
        }
    }
}

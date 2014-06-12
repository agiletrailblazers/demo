package com.comcast.stagger.mavenplugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiDocumentMojoTest {
    ApiDocumentMojo mojo = new ApiDocumentMojo();

    String tmpSwaggerOutputDir = "apidocsf";

    ApiSource apiSource;

    @Before
    public void prepare() {
        List<ApiSource> apiSources = new ArrayList<ApiSource>();
        apiSource = new ApiSource();
        apiSource.setApiVersion("1.0");
        apiSource.setBasePath("http://example.com");
        apiSource.setSwaggerUIDocBasePath("http://localhost/apidocsf");
        apiSource.setLocations("sample.api");
        apiSource.setFileOutputPath("temp.html");
        apiSource.setFileOutputTemplate("https://raw2.github.com/kongchen/api-doc-template/master/v1.1/strapdown.html.mustache");
        apiSource.setSwaggerDirectory(tmpSwaggerOutputDir);
        apiSource.setUseOutputFlatStructure(false);

        apiSources.add(apiSource);
        mojo.setApiSources(apiSources);
    }

    @After
    public void fin() throws IOException {
        File tempOutput = new File(tmpSwaggerOutputDir);
    }

    /*
    @Test
    public void testSwaggerOutputFlat() throws IOException, MojoFailureException, MojoExecutionException {
        apiSource.setSwaggerDirectory(tmpSwaggerOutputDir);
        apiSource.setUseOutputFlatStructure(true);

        File output = new File(tmpSwaggerOutputDir);
        FileUtils.deleteDirectory(output);

        mojo.execute();
        List<String> flatfiles = new ArrayList<String>();

        Collections.addAll(flatfiles, output.list());
        Collections.sort(flatfiles);
        Assert.assertEquals(flatfiles.get(0), "car.json");
        Assert.assertEquals(flatfiles.get(1), "garage.json");
        Assert.assertEquals(flatfiles.get(2), "service.json");
        Assert.assertEquals(flatfiles.get(3), "v2_car.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(FileUtils.readFileToByteArray(new File(output, "service.json")));
        JsonNode apis = node.get("apis");
        Assert.assertEquals(apis.size(), 3);
        List<String> pathInService = new ArrayList<String> ();
        for (JsonNode api : apis) {
            pathInService.add(api.get("path").asText());
        }
        Collections.sort(pathInService);
        Assert.assertEquals(pathInService.get(0), "/car.{format}");
        Assert.assertEquals(pathInService.get(1), "/garage.{format}");
        Assert.assertEquals(pathInService.get(2), "/v2_car.{format}");
    }

    @Test
    public void testSwaggerOutput() throws IOException, MojoFailureException, MojoExecutionException {
        apiSource.setSwaggerDirectory(tmpSwaggerOutputDir);
        apiSource.setUseOutputFlatStructure(false);

        File output = new File(tmpSwaggerOutputDir);
        FileUtils.deleteDirectory(output);

        mojo.execute();
        List<File> outputFiles = new ArrayList<File>();

        Collections.addAll(outputFiles, output.listFiles());
        Collections.sort(outputFiles);
        Assert.assertEquals(outputFiles.get(0).getName(), "car.json");
        Assert.assertEquals(outputFiles.get(1).getName(), "garage.json");
        Assert.assertEquals(outputFiles.get(2).getName(), "service.json");
        Assert.assertEquals(outputFiles.get(3).getName(), "v2");
        File v2 = outputFiles.get(3);
        Assert.assertTrue(v2.isDirectory());
        String[] v2carfile = v2.list();
        Assert.assertEquals(v2carfile[0], "car.json");


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(FileUtils.readFileToByteArray(new File(output, "service.json")));
        JsonNode apis = node.get("apis");
        Assert.assertEquals(apis.size(), 3);
        List<String> pathInService = new ArrayList<String> ();
        for (JsonNode api : apis) {
            pathInService.add(api.get("path").asText());
        }
        Collections.sort(pathInService);
        Assert.assertEquals(pathInService.get(0), "/car.{format}");
        Assert.assertEquals(pathInService.get(1), "/garage.{format}");
        Assert.assertEquals(pathInService.get(2), "/v2/car.{format}");
    }

    @Test
    public void testExecute() throws Exception {
        mojo.execute();
        FileInputStream testOutputIs = new FileInputStream(new File("temp.html"));
        InputStream expectIs = this.getClass().getResourceAsStream("/sample.html");
        int count = 0;
        while (true) {
            count++;
            int expect = expectIs.read();
            int actual = testOutputIs.read();

            Assert.assertEquals( expect, actual);
            if (expect == -1) {
                break;
            }
        }
    }
    */
}

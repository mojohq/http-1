package com.wizzardo.http;

import com.wizzardo.tools.io.FileTools;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by wizzardo on 09/11/16.
 */
public class FileTreeHandlerTest extends ServerTest {
    File testDir;

    @Test
    public void test_prefix() {
        FileTreeHandler handler;

        handler = new FileTreeHandler("/", "");
        Assert.assertEquals("", handler.prefix);

        handler = new FileTreeHandler("/", "/");
        Assert.assertEquals("", handler.prefix);

        handler = new FileTreeHandler("/", "prefix");
        Assert.assertEquals("/prefix", handler.prefix);

        handler = new FileTreeHandler("/", "prefix/");
        Assert.assertEquals("/prefix", handler.prefix);

        handler = new FileTreeHandler("/", "/prefix/");
        Assert.assertEquals("/prefix", handler.prefix);

        handler = new FileTreeHandler("/", "/prefix");
        Assert.assertEquals("/prefix", handler.prefix);
    }

    @Test
    public void test_handle() throws IOException, InterruptedException, NoSuchMethodException, NoSuchFieldException, ClassNotFoundException {
        FileTools.text(new File(testDir, "foo"), "bar");

        handler = new FileTreeHandler(testDir, "");
        Assert.assertEquals("bar", makeRequest("/foo").get().asString());

        handler = new FileTreeHandler(testDir, "prefix");
        Assert.assertEquals("bar", makeRequest("/prefix/foo").get().asString());
        checkResponse(400, "path must starts with prefix '/prefix'. path=/wrong_prefix/foo", makeRequest("/wrong_prefix/foo").get());
        checkResponse(403, "/../foo is forbidden", makeRequest("/prefix/../foo").get());
        checkResponse(404, "/bar not found", makeRequest("/prefix/bar").get());

        new File(testDir, "foo").setReadable(false);
        checkResponse(403, "/foo is forbidden", makeRequest("/prefix/foo").get());

        new File(testDir, "bar").mkdirs();
        handler = new FileTreeHandler(testDir, "").setShowFolder(false);
        checkResponse(403, "/bar is forbidden", makeRequest("/bar").get());

        handler = new FileTreeHandler(testDir, "");
        checkResponse(200, "<!DOCTYPE html><html>" +
                "<header><meta charset=\"utf-8\"><title>/bar/</title></meta></header>" +
                "<body>" +
                "<h1><a href=\"/\">root: </a><a href=\"/\"></a>/<a href=\"/bar/\">bar</a>/</h1>" +
                "<table border=\"0\">" +
                "<tr>" +
                "<th><a href=\"/bar/?sort=name&order=desc\">Name</a></th>" +
                "<th><a href=\"/bar/?sort=modified&order=desc\">Last modified</a></th>" +
                "<th><a href=\"/bar/?sort=size&order=desc\">Size</a></th>" +
                "</tr>\n" +
                "</table></body></html>", makeRequest("/bar").get());


        tearDown();
        context = "context";
        setUp();
        handler = new FileTreeHandler(testDir, "");
        FileTools.text(new File(testDir, "foo"), "bar");
        Assert.assertEquals("bar", makeRequest("/" + context + "/foo").get().asString());
    }

    @Override
    public void setUp() throws NoSuchMethodException, ClassNotFoundException, NoSuchFieldException {
        super.setUp();
        testDir = new File(FileTreeHandlerTest.class.getSimpleName());
        testDir.mkdirs();
    }

    @Override
    public void tearDown() throws InterruptedException {
        super.tearDown();
        FileTools.deleteRecursive(testDir);
    }
}

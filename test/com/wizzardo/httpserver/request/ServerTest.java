package com.wizzardo.httpserver.request;

import com.wizzardo.httpserver.HttpServer;
import com.wizzardo.httpserver.response.Response;
import com.wizzardo.tools.http.HttpClient;
import com.wizzardo.tools.io.IOTools;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

/**
 * @author: moxa
 * Date: 5/7/13
 */
public class ServerTest {

    protected HttpServer server;
    protected int workers = 4;
    protected int port = 9999;
    protected Handler handler;

    @Before
    public void setUp() throws NoSuchMethodException, ClassNotFoundException, NoSuchFieldException {
        server = new HttpServer(null, port, workers) {
            @Override
            public Response handleRequest(Request request) {
                return handler.handleRequest(request);
            }
        };
        server.setIoThreadsCount(1);
        server.start();
    }

    @After
    public void tearDown() throws InterruptedException {
        server.stopEpoll();
        handler = null;
    }

    protected com.wizzardo.tools.http.Request makeRequest(String path) {
        return HttpClient.connect("http://localhost:" + port + path)
                .header("Connection", "Close");
    }

    protected String curl(String path, String... params) {
        Runtime runtime = Runtime.getRuntime();

        String[] args = new String[params.length + 2];
        args[0] = "curl";
        System.arraycopy(params, 0, args, 1, params.length);
        args[args.length - 1] = "http://localhost:" + port + path;

        try {
            Process p = runtime.exec(args);
            return new String(IOTools.bytes(p.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract class Handler {
        protected abstract Response handleRequest(Request request);
    }
}
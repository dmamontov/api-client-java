package ru.retailcrm.apiclient.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ru.retailcrm.apiclient.extra.Tools;
import ru.retailcrm.apiclient.response.ApiResponse;

public class Client {

    public final static String METHOD_GET = "GET";
    public final static String METHOD_POST = "POST";
    public final static String USER_AGENT = "Mozilla/5.0";

    protected String url;
    protected Map<String, Object> defaultParameter;

    /**
     * @param String url
     * @param Map<String, Object> parameters
     */
    public Client(String url, Map<String, Object> parameters) {
        if (url.indexOf("https://") == -1) {
            throw new IllegalArgumentException("API schema requires HTTPS protocol");
        }

        this.url = url;
        this.defaultParameter = parameters;
    }

    /**
     * @param String path
     * @param String method
     * @param Map<String, Object> parameters
     * @return ApiResponse
     */
    public ApiResponse makeRequest(String path, String method, Map<String, Object> parameters) {
        String[] allowedMethods = {Client.METHOD_GET, Client.METHOD_POST};

        if (Arrays.asList(allowedMethods).contains(method) == false) {
            throw new IllegalArgumentException("Method \"" + method + "\" is not valid. Allowed methods are " + String.join(", ", allowedMethods));
        }

        if (parameters == null) {
            parameters = new HashMap<String, Object>();
        }

        parameters.putAll(this.defaultParameter);
        path = this.url + path;

        String httpQuery = Tools.httpBuildQuery(parameters);

        if (method.equals(Client.METHOD_GET) && parameters.isEmpty() == false) {
            path += "?" + httpQuery;
        }

        URL ch = null;
        try {
            ch = new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpsURLConnection connect = null;
        try {
            connect = (HttpsURLConnection) ch.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            connect.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        connect.setRequestProperty("User-Agent", Client.USER_AGENT);

        if (method.equals(Client.METHOD_POST)) {
            connect.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connect.setDoOutput(true);

            DataOutputStream wr;
            try {
                wr = new DataOutputStream(connect.getOutputStream());
                wr.writeBytes(httpQuery);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        int statusCode = 0;
        try {
            statusCode = connect.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader( new InputStreamReader(connect.getInputStream()) );
        } catch (IOException e2) {
            in = new BufferedReader( new InputStreamReader(connect.getErrorStream()) );
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ApiResponse(statusCode, response.toString());
    }
}

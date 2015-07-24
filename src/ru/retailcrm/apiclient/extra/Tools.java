package ru.retailcrm.apiclient.extra;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ru.retailcrm.apiclient.http.Client;

public class Tools {

    /**
     * @param String url
     * @param String apiKey
     * @param String apiVersion
     * @return Client
     */
    public static Client createClient(String url, String apiKey, String apiVersion) {
        if (url.endsWith("/")) {
            url += "/";
        }

        url += "api/" + apiVersion;

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("apiKey", apiKey);

        return new Client(url, parameters);
    }

    /**
     * @param String by
     * @return void
     */
    public static void checkIdParameter(String by) {
        String[] allowedForBy = {"externalId", "id"};
        if (Arrays.asList(allowedForBy).contains(by) == false) {
            throw new IllegalArgumentException("Value \"" + by + "\" for parameter \"by\" is not valid. Allowed values are " + String.join(", ", allowedForBy));
        }
    }

    /**
     * @param Map<?, ?> map
     * @return String
     */
    public static String jsonEncode(Map<?, ?> map) {
        String json = null;
        JSONObject resultJson = new JSONObject(map);
        json = resultJson.toString();

        return json;
    }

    /**
     * @param String string
     * @return Map<String, Object>
     * @throws ParseException
     */
    public static Map<String, Object> jsonDecode(String string) throws org.json.simple.parser.ParseException {
        Map<String, Object> data = new HashMap<String, Object>();

        JSONParser parser = new JSONParser();
        Object unitsObj = parser.parse(string);
        JSONObject unitsJson = (JSONObject) unitsObj;

        if (unitsJson != null) {
            data = Tools.jsonToMap(unitsJson);
        }

        return data;
    }

    /**
     * @param JSONObject json
     * @return Map<String, Object>
     */
    protected static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();

        Set<?> setJson = json.entrySet();
        Iterator<?> interatorJson = setJson.iterator();

        while (interatorJson.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry mapEntry = (Map.Entry) interatorJson.next();
            String key = mapEntry.getKey().toString();
            Object value = mapEntry.getValue();

            if(value instanceof JSONObject)
            {
                value = Tools.jsonToMap((JSONObject) value);
            }
            map.put(key, value);
        }

        return map;
    }

    /**
     * @param Map<String, Object> parameters
     * @return String
     */
    public static String httpBuildQuery(Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (builder.length() > 0) {
                builder.append('$');
            }

            String name = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                List<String> baseParam = new ArrayList<String>();
                baseParam.add(name);
                @SuppressWarnings("unchecked")
                String str = buildUrlFromMap(baseParam, (Map<Object, Object>) value);
                builder.append(str);
            } else {
                builder.append(Tools.encodeParam(name));
                builder.append("=");
                builder.append(Tools.encodeParam(value));
            }
        }

        return builder.toString();
    }

    /**
     * @param List<String> baseParam
     * @param Map<Object, Object> map
     * @return String
     */
    private static String buildUrlFromMap(List<String> baseParam, Map<Object, Object> map) {
        StringBuilder builder = new StringBuilder();
        String token;

        for (Map.Entry<Object, Object> entry : map.entrySet()) {

            if (builder.length() > 0) {
                builder.append('&');
            }

            String name = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Map) {
                List<String> baseParam2 = new ArrayList<String>(baseParam);
                baseParam2.add(name);
                @SuppressWarnings("unchecked")
                String str = Tools.buildUrlFromMap(baseParam2, (Map<Object, Object>) value);
                builder.append(str);

            } else {
                token = Tools.getBaseParamString(baseParam) + "[" + name + "]=" + Tools.encodeParam(value);
                builder.append(token);
            }
        }

        return builder.toString();
    }

    /**
     * @param List<String> baseParam
     * @return String
     */
    private static String getBaseParamString(List<String> baseParam) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < baseParam.size(); i++) {
            String s = baseParam.get(i);
            if (i == 0) {
                builder.append(s);
            } else {
                builder.append("[" + s + "]");
            }
        }
        return builder.toString();
    }

    /**
     * @param Object param
     * @return String
     */
    @SuppressWarnings("deprecation")
    private static String encodeParam(Object param) {
        try {
            return URLEncoder.encode(String.valueOf(param), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(String.valueOf(param));
        }
    }
}

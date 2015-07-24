package ru.retailcrm.apiclient.response;

import java.util.Map;

import org.json.simple.parser.ParseException;

import ru.retailcrm.apiclient.extra.Tools;

public class ApiResponse {

    protected int statusCode;
    protected Map<String, Object> response;

    /**
     * @param int statusCode
     * @param String responseBody
     */
    public ApiResponse(int statusCode, String responseBody) {
         this.statusCode = statusCode;

         if (responseBody != null && responseBody.isEmpty() == false) {
            try {
                this.response = Tools.jsonDecode(responseBody);
            } catch (ParseException e) {
                e.printStackTrace();
            }
         }
    }

    /**
     * @return int
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * @return boolean
     */
    public boolean isSuccessful() {
        return this.statusCode < 400;
    }

    /**
     * @return Map<String, Object>
     */
    public Map<String, Object> getResult() {
        return this.response;
    }

}

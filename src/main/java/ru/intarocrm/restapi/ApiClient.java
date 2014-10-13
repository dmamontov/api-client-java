package ru.intarocrm.restapi;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ApiClient {

    protected final String apiUrl;
    protected final String apiKey;
    protected final String apiVersion = "3";
    protected Date generatedAt;
    protected Map<String, String> parameters;

    private final String userAgent = "Mozilla/5.0";

    /**
     * @param String crmUrl - адрес CRM
     * @param String crmKey - ключ для работы с api
     */
    public ApiClient(String crmUrl, String crmKey) {
        apiUrl = crmUrl + "/api/v" + apiVersion + "/";
        apiKey = crmKey;
        parameters = new HashMap<String, String>();
        parameters.put( "apiKey", apiKey );
    }

    /**
     * Получение заказа по id
     *
     * @param String id - идентификатор заказа
     * @param String by - поиск заказа по id или externalId
     * @return Map<?, ?> - информация о заказе
     * @throws ApiClientException 
     */
    public Map<?, ?> orderGet(int id, String by) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "orders/" + Integer.toString(id);
        if (by.equals( (String) "externalId" )) {
            parameters.put( "by", by );
        }
        result = request(url, "GET");

        return result;
    }

    /**
     * Создание заказа
     *
     * @param Map<?, ?> order- информация о заказе
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderCreate(Map<?, ?> order) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(order);
        parameters.put("order", dataJson);
        String url = apiUrl + "orders/create";
        result = request(url, "POST");

        return result;
    }

    /**
     * Изменение заказа
     *
     * @param Map<?, ?> order- информация о заказе
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderEdit(Map<?, ?> order) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(order);
        parameters.put("order", dataJson);
        String url = apiUrl + "orders/" + order.get("externalId").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Пакетная загрузка заказов
     *
     * @param Map<?, ?> orders - массив заказов
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderUpload(Map<?, ?> orders) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(orders);
        parameters.put("orders", dataJson);
        String url = apiUrl + "orders/" + orders.get("externalId").toString() + "/edit";
        result = request(url, "POST");

        if (isEmpty(result) && result.containsKey("uploadedOrders")) {
            return (Map<?, ?>) result.get("uploadedOrders");
        }

        return result;
    }

    /**
     * Обновление externalId у заказов с переданными id
     *
     * @param Map<?, ?> orders- массив, содержащий id и externalId заказа
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderFixExternalIds(Map<?, ?> orders) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(orders);
        parameters.put("orders", dataJson);
        String url = apiUrl + "orders/fix-external-ids";
        result = request(url, "POST");

        return result;
    }

    /**
     * Удаление заказа
     *
     * @param int id - идентификатор заказа
     * @param String by - поиск заказа по id или externalId
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderDelete(int id, String by) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "orders/" + Integer.toString(id) + "/delete";
        if (by.equals( (String) "externalId" )) {
            parameters.put( "by", by );
        }
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение последних измененных заказов
     *
     * @param Date startDate - начальная дата выборки
     * @param Date endDate - конечная дата
     * @param int limit - ограничение на размер выборки
     * @param int offset - сдвиг
     * @return Map<?, ?> result - массив заказов
     * @throws ApiClientException 
     */
    public Map<?, ?> orderHistory(Date startDate, Date endDate, int limit, int offset) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "orders/history";
        parameters.put( "startDate", startDate.toString() );
        parameters.put( "endDate", endDate.toString() );
        parameters.put( "limit", Integer.toString(limit) );
        parameters.put( "offset", Integer.toString(offset) );
        result = request(url, "GET");

        return result;
    }

    /**
     * Получение клиента по id
     *
     * @param String id - идентификатор
     * @param String by - поиск заказа по id или externalId
     * @return Map<?, ?> result - информация о клиенте
     * @throws ApiClientException 
     */
    public Map<?, ?> customerGet(String id, String by) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "customers/" + id;
        if (by.equals( (String) "externalId" )) {
            parameters.put( "by", by );
        }
        result = request(url, "GET");

        return result;
    }

    /**
     * Получение списка клиентов в соответсвии с запросом
     *
     * @param String phone - телефон
     * @param String email - почтовый адрес
     * @param String fio - фио пользователя
     * @param int limit - ограничение на размер выборки
     * @param int offset - сдвиг
     * @return Map<?, ?> result - массив клиентов
     * @throws ApiClientException 
     */
    public Map<?, ?> customers(String phone, String email, String fio, int limit, int offset) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "customers";
        parameters.put( "phone", phone );
        parameters.put( "email", email );
        parameters.put( "fio", fio );
        parameters.put( "limit", Integer.toString(limit) );
        parameters.put( "offset", Integer.toString(offset) );
        result = request(url, "GET");

        return result;
    }

    /**
     * Создание клиента
     *
     * @param Map<?, ?> customer - информация о клиенте
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> customerCreate(Map<?, ?> customer) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(customer);
        parameters.put("customer", dataJson);
        String url = apiUrl + "customers/create";
        result = request(url, "POST");

        return result;
    }

    /**
     * Редактирование клиента
     *
     * @param Map<?, ?> customer - информация о клиенте
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> customerEdit(Map<?, ?> customer) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(customer);
        parameters.put("customer", dataJson);
        String url = apiUrl + "customers/" + customer.get("externalId").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Пакетная загрузка клиентов
     *
     * @param Map<?, ?> customers - массив клиентов
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> customerUpload(Map<?, ?> customers) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(customers);
        parameters.put("customers", dataJson);
        String url = apiUrl + "customers/upload";
        result = request(url, "POST");

        if (isEmpty(result) && result.containsKey("uploaded")) {
            return (Map<?, ?>) result.get("uploaded");
        }

        return result;
    }

    /**
     * Обновление externalId у клиентов с переданными id
     *
     * @param Map<?, ?> customers- массив, содержащий id и externalId заказа
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> customerFixExternalIds(Map<?, ?> customers) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(customers);
        parameters.put("customers", dataJson);
        String url = apiUrl + "customers/fix-external-ids";
        result = request(url, "POST");

        return result;
    }

    /**
     * Удаление клиента
     *
     * @param String id - идентификатор
     * @param String by - поиск заказа по id или externalId
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> customerDelete(String id, String by) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "customers/" + id + "/delete";
        if (by.equals( (String) "externalId" )) {
            parameters.put( "by", by );
        }
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка заказов клиента
     *
     * @param String id - идентификатор клиента
     * @param String by - поиск заказа по id или externalId
     * @param Date startDate - начальная дата выборки
     * @param Date endDate - конечная дата
     * @param int limit - ограничение на размер выборки
     * @param int offset - сдвиг
     * @return Map<?, ?> result - массив заказов
     * @throws ApiClientException 
     */
    public Map<?, ?> customerOrdersList(String id, Date startDate, Date endDate, int limit, int offset, String by) throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "customers/" + id + "/orders";
        if (by.equals( (String) "externalId" )) {
            parameters.put( "by", by );
        }
        parameters.put( "startDate", startDate.toString() );
        parameters.put( "endDate", endDate.toString() );
        parameters.put( "limit", Integer.toString(limit) );
        parameters.put( "offset", Integer.toString(offset) );
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка типов доставки
     *
     * @return Map<?, ?> result - массив типов доставки
     * @throws ApiClientException 
     */
    public Map<?, ?> deliveryTypesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/delivery-types";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование типа доставки
     *
     * @param Map<?, ?> deliveryType - информация о типе доставки
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> deliveryTypeEdit(Map<?, ?> deliveryType) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(deliveryType);
        parameters.put("deliveryType", dataJson);
        String url = apiUrl + "reference/delivery-types/" + deliveryType.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка служб доставки
     *
     * @return Map<?, ?> result - массив типов доставки
     * @throws ApiClientException 
     */
    public Map<?, ?> deliveryServicesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/delivery-services";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование службы доставки
     *
     * @param Map<?, ?> deliveryService - информация о типе доставки
     * @return Map<?, ?> result
     * @throws ApiClientException
     */
    public Map<?, ?> deliveryServiceEdit(Map<?, ?> deliveryService) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(deliveryService);
        parameters.put("deliveryService", dataJson);
        String url = apiUrl + "reference/delivery-services/" + deliveryService.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка типов оплаты
     *
     * @return Map<?, ?> result - массив типов оплаты
     * @throws ApiClientException
     */
    public Map<?, ?> paymentTypesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/payment-types";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование типа оплаты
     *
     * @param Map<?, ?> paymentType - информация о типе оплаты
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> paymentTypesEdit(Map<?, ?> paymentType) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(paymentType);
        parameters.put("paymentType", dataJson);
        String url = apiUrl + "reference/payment-types/" + paymentType.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка статусов оплаты
     *
     * @return Map<?, ?> result - массив статусов оплаты
     * @throws ApiClientException 
     */
    public Map<?, ?> paymentStatusesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/payment-statuses";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование статуса оплаты
     *
     * @param Map<?, ?> paymentStatus - информация о статусе оплаты
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> paymentStatusesEdit(Map<?, ?> paymentStatus) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(paymentStatus);
        parameters.put("paymentStatus", dataJson);
        String url = apiUrl + "reference/payment-statuses/" + paymentStatus.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка типов заказа
     *
     * @return Map<?, ?> result - массив типов заказа
     * @throws ApiClientException 
     */
    public Map<?, ?> orderTypesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/order-types";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование типа заказа
     *
     * @param Map<?, ?> orderType - информация о типе заказа
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderTypesEdit(Map<?, ?> orderType) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(orderType);
        parameters.put("orderType", dataJson);
        String url = apiUrl + "reference/order-types/" + orderType.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка способов оформления заказа
     *
     * @return Map<?, ?> result - массив способов оформления заказа
     * @throws ApiClientException
     */
    public Map<?, ?> orderMethodsList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/order-methods";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование способа оформления заказа
     *
     * @param Map<?, ?> orderMethod - информация о способе оформления заказа
     * @return Map<?, ?> result
     * @throws ApiClientException
     */
    public Map<?, ?> orderMethodsEdit(Map<?, ?> orderMethod) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(orderMethod);
        parameters.put("orderMethod", dataJson);
        String url = apiUrl + "reference/order-methods/" + orderMethod.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка статусов заказа
     *
     * @return Map<?, ?> result - массив статусов заказа
     * @throws ApiClientException 
     */
    public Map<?, ?> orderStatusesList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/statuses";
        result = request(url, "GET");

        return result;
    }

    /**
     * Редактирование статуса заказа
     *
     * @param Map<?, ?> status - информация о статусе заказа
     * @return Map<?, ?> result
     * @throws ApiClientException 
     */
    public Map<?, ?> orderStatusEdit(Map<?, ?> status) throws ApiClientException {
        Map<?, ?> result = null;
        String dataJson = jsonEncode(status);
        parameters.put("status", dataJson);
        String url = apiUrl + "reference/statuses/" + status.get("code").toString() + "/edit";
        result = request(url, "POST");

        return result;
    }

    /**
     * Получение списка групп статусов заказа
     *
     * @return Map<?, ?> result - массив групп статусов заказа
     * @throws ApiClientException 
     */
    public Map<?, ?> orderStatusGroupsList() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "reference/status-groups";
        result = request(url, "GET");

        return result;
    }

    /**
     * Обновление статистики
     *
     * @return Map<?, ?> result - статус вып обновления
     * @throws ApiClientException 
     */
    public Map<?, ?> statisticUpdate() throws ApiClientException {
        Map<?, ?> result = null;
        String url = apiUrl + "statistic/update";
        result = request(url, "GET");

        return result;
    }

    /**
     * @return Date
     */
    public Date getGeneratedAt() {
        return generatedAt;
    }

    /**
     * @param String url
     * @param String method
     * @return Map<?, ?>
     * @throws ApiClientException
     */
    protected Map<?, ?> request(String url, String method) throws ApiClientException {
        Map<?, ?> data = null;
        String urlParameters = null;
        try {
            urlParameters = httpBuildQuery(parameters);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        if (method.equals( (String) "GET" ) && !isEmpty( parameters )) {
            url += "?" + urlParameters;
        }

        URL object = null;
        try {
            object = new URL(url);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        HttpsURLConnection connect = null;
        try {
            connect = (HttpsURLConnection) object.openConnection();
        } catch (IOException e1) {
           e1.printStackTrace();
        }

        try {
            connect.setRequestMethod(method);
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        }
        connect.setRequestProperty("User-Agent", userAgent);

        if (method.equals( (String) "POST" )) {
            connect.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connect.setDoOutput(true);

            DataOutputStream wr;
            try {
                wr = new DataOutputStream(connect.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }

        int statusCode = 0;
        try {
            statusCode = connect.getResponseCode();
        } catch (IOException e3) {
            e3.printStackTrace();
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
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        parameters.clear();
        parameters.put( "apiKey", apiKey );
        
        try {
            data = jsonDecode(response.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(data.containsKey("generatedAt")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                generatedAt = simpleDateFormat.parse(data.get("generatedAt").toString());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            data.remove("generatedAt");
        }
        
        String success = data.get("success").toString();
        if (statusCode >= 400 || data.containsKey("success") && success.equals( (String) "false" )) {
            throw new ApiClientException(getErrorMessage(data));
        }

        data.remove("success");

        if (data.size() == 0) {
            return null;
        }

        return data;
    }

    /**
     * @param Map<?, ?> data
     * @return String error
     */
    protected String getErrorMessage(Map<?, ?> data) {
        String error = "";

        if (data.containsKey("message")) {
        	error = data.get("message").toString();
        } else if(data.containsKey("0")){
            Map <?, ?> subStr = (Map<?, ?>) data.get("0");
            error = subStr.get("message").toString();
        } else if(data.containsKey("errorMsg")) {
        	error = data.get("errorMsg").toString();
        } else if(data.containsKey("error")) {
            Map <?, ?> subStr = (Map<?, ?>) data.get("error");
            if (subStr.containsKey("message")) {
            	error = subStr.get("message").toString();
            }
        } 

        if (data.containsKey("errors")) {
            Map <?,?> subErrors = (Map<?, ?>) data.get("errors");
            if (subErrors.size() > 1) {
            	Set<?> set = subErrors.entrySet();
                Iterator<?> interator = set.iterator();
                while (interator.hasNext()) {
                    @SuppressWarnings("rawtypes")
                    Map.Entry mapEntry = (Map.Entry) interator.next();
                    String value = mapEntry.getValue().toString();

                    error += ". " + value;
                }
            }
        }

        if (isEmpty(error)) {
            return "Application Error";
        }

        return error;
    }

    /**
     * @param String string - json строка
     * @return Map<?, ?> data
     * @throws ParseException 
     */
    protected Map<?, ?> jsonDecode(String string) throws ParseException {
        Map<String, Object> data = new HashMap<String, Object>();

        JSONParser parser = new JSONParser();
        Object unitsObj = parser.parse(string);
        JSONObject unitsJson = (JSONObject) unitsObj;

        if (unitsJson != null) {
            data = jsonToMap(unitsJson);
        }

        return data;
    }

    /**
     * @param Map<?, ?> map
     * @return String json
     */
    protected String jsonEncode(Map<?, ?> map) {
        String json = null;
        JSONObject resultJson = new JSONObject(map);
        json = resultJson.toString();

        return json;
    }

    /**
     * @param JSONObject json - json
     * @return Map<String, Object> map
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
                value = jsonToMap((JSONObject) value);
            }
            map.put(key, value);
        }

        return map;
    }

    /**
     * @param Map<?, ?> value - ассоциативный массив параметров
     * @return boolean
     */
    protected static boolean isEmpty(Map<?, ?> value) {
        return value == null || value.isEmpty();
    }

    /**
     * @param String value
     * @return boolean
     */
    protected static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * @param Map<?, ?> data - ассоциативный массив параметров
     * @return String
     * @throws UnsupportedEncodingException
     */
    protected String httpBuildQuery(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();

        for (Entry<?, ?> pair : data.entrySet()) {
            queryString.append( URLEncoder.encode ( (String) pair.getKey (), "UTF-8" ) + "=" );
            queryString.append( URLEncoder.encode ( (String) pair.getValue (), "UTF-8" ) + "&" );
        }

        if (queryString.length () > 0) {
            queryString.deleteCharAt ( queryString.length () - 1 );
        }

        return queryString.toString();
    }

}

package ru.retailcrm.apiclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.retailcrm.apiclient.extra.Tools;
import ru.retailcrm.apiclient.http.Client;
import ru.retailcrm.apiclient.response.ApiResponse;

public class ApiClient {

    protected final String apiVersion = "v3";
    protected Client client;
    protected String siteCode;

    /**
     * @param String url
     * @param String crmKey
     */
    public ApiClient(String url, String apiKey) {
        this.client = Tools.createClient(url, apiKey, this.apiVersion);
    }

    /**
     * @param String url
     * @param String crmKey
     * @param String site
     */
    public ApiClient(String url, String apiKey, String site) {
        this.client = Tools.createClient(url, apiKey, this.apiVersion);
        this.siteCode = site;
    }

    /**
     * @param Map<String, Object> order
     * @return ApiResponse
     */
    public ApiResponse ordersCreate(Map<String, Object> order) {
        if (order.isEmpty() || order == null) {
            throw new IllegalArgumentException("Parameter `order` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("order", Tools.jsonEncode(order));

        return this.client.makeRequest("/orders/create", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> order
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse ordersCreate(Map<String, Object> order, String site) {
        if (order.isEmpty() || order == null) {
            throw new IllegalArgumentException("Parameter `order` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("order", Tools.jsonEncode(order));

        return this.client.makeRequest("/orders/create", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> order
     * @return ApiResponse
     */
    public ApiResponse ordersEdit(Map<String, Object> order) {
        if (order.isEmpty() || order == null) {
            throw new IllegalArgumentException("Parameter `order` must contains a data");
        }

        if (order.containsKey("externalId") == false) {
            throw new IllegalArgumentException("Order array must contain the \"externalId\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("order", Tools.jsonEncode(order));

        return this.client.makeRequest("/orders/" + order.get("externalId").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> order
     * @param String by
     * @return ApiResponse
     */
    public ApiResponse ordersEdit(Map<String, Object> order, String by) {
        if (order.isEmpty() || order == null) {
            throw new IllegalArgumentException("Parameter `order` must contains a data");
        }

        Tools.checkIdParameter(by);

        if (order.containsKey(by) == false) {
            throw new IllegalArgumentException("Order array must contain the \"" + by + "\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("order", Tools.jsonEncode(order));

        return this.client.makeRequest("/orders/" + order.get(by).toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> order
     * @param String by
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse ordersEdit(Map<String, Object> order, String by, String site) {
        if (order.isEmpty() || order == null) {
            throw new IllegalArgumentException("Parameter `order` must contains a data");
        }

        Tools.checkIdParameter(by);

        if (order.containsKey(by) == false) {
            throw new IllegalArgumentException("Order array must contain the \"" + by + "\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("order", Tools.jsonEncode(order));

        return this.client.makeRequest("/orders/" + order.get(by).toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> orders
     * @return ApiResponse
     */
    public ApiResponse ordersUpload(Map<String, Object> orders) {
        if (orders.isEmpty() || orders == null) {
            throw new IllegalArgumentException("Parameter `orders` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("orders", Tools.jsonEncode(orders));

        return this.client.makeRequest("/orders/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> orders
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse ordersUpload(Map<String, Object> orders, String site) {
        if (orders.isEmpty() || orders == null) {
            throw new IllegalArgumentException("Parameter `orders` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("orders", Tools.jsonEncode(orders));

        return this.client.makeRequest("/orders/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @param String id
     * @return ApiResponse
     */
    public ApiResponse ordersGet(String id) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("by", "externalId");

        return this.client.makeRequest("/orders/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param String id
     * @param String by
     * @return ApiResponse
     */
    public ApiResponse ordersGet(String id, String by) {
        Tools.checkIdParameter(by);

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("by", by);

        return this.client.makeRequest("/orders/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param String id
     * @param String by
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse ordersGet(String id, String by, String site) {
        Tools.checkIdParameter(by);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("by", by);

        return this.client.makeRequest("/orders/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param Date startDate
     * @param Date endDate
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("startDate", dateFormat.format(startDate));
        parameters.put("endDate", dateFormat.format(endDate));

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param int limit
     * @param int offset
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(int limit, int offset) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("limit", Integer.toString(limit));
        parameters.put("offset", Integer.toString(offset));

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param boolean skipMyChanges
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(boolean skipMyChanges) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("skipMyChanges", skipMyChanges);

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param Date startDate
     * @param Date endDate
     * @param int limit
     * @param int offset
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(Date startDate, Date endDate, int limit, int offset) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("startDate", dateFormat.format(startDate));
        parameters.put("endDate", dateFormat.format(endDate));
        parameters.put("limit", Integer.toString(limit));
        parameters.put("offset", Integer.toString(offset));

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param Date startDate
     * @param Date endDate
     * @param boolean skipMyChanges
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(Date startDate, Date endDate, boolean skipMyChanges) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("startDate", dateFormat.format(startDate));
        parameters.put("endDate", dateFormat.format(endDate));
        parameters.put("skipMyChanges", skipMyChanges);

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param int limit
     * @param int offset
     * @param boolean skipMyChanges
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(int limit, int offset, boolean skipMyChanges) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("limit", Integer.toString(limit));
        parameters.put("offset", Integer.toString(offset));
        parameters.put("skipMyChanges", skipMyChanges);

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param Date startDate
     * @param Date endDate
     * @param int limit
     * @param int offset
     * @param boolean skipMyChanges
     * @return ApiResponse
     */
    public ApiResponse ordersHistory(Date startDate, Date endDate, int limit, int offset, boolean skipMyChanges) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("startDate", dateFormat.format(startDate));
        parameters.put("endDate", dateFormat.format(endDate));
        parameters.put("limit", Integer.toString(limit));
        parameters.put("offset", Integer.toString(offset));
        parameters.put("skipMyChanges", skipMyChanges);

        return this.client.makeRequest("/orders/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @return ApiResponse
     */
    public ApiResponse ordersList(Map<String, Object> filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);

        return this.client.makeRequest("/orders", Client.METHOD_GET, parameters);
    }

    /**
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse ordersList(int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse ordersList(Map<String, Object> filter, int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> ids
     * @param Map<String, Object> externalIds
     * @return ApiResponse
     */
    public ApiResponse ordersStatuses(Map<String, Object> ids, Map<String, Object> externalIds) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        if (ids.isEmpty() == false && ids != null) {
            parameters.put("ids", ids);
        }
        if (externalIds.isEmpty() == false && externalIds != null) {
            parameters.put("externalIds", externalIds);
        }

        return this.client.makeRequest("/orders/statuses", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> ids
     * @return ApiResponse
     */
    public ApiResponse ordersFixExternalId(Map<String, Object> ids) {
        if (ids.isEmpty() || ids == null) {
            throw new IllegalArgumentException("Method parameter must contains at least one IDs pair");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orders", Tools.jsonEncode(ids));

        return this.client.makeRequest("/orders/fix-external-ids", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customer
     * @return ApiResponse
     */
    public ApiResponse customersCreate(Map<String, Object> customer) {
        if (customer.isEmpty() || customer == null) {
            throw new IllegalArgumentException("Parameter `customer` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("customer", Tools.jsonEncode(customer));

        return this.client.makeRequest("/customers/create", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customer
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse customersCreate(Map<String, Object> customer, String site) {
        if (customer.isEmpty() || customer == null) {
            throw new IllegalArgumentException("Parameter `customer` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("customer", Tools.jsonEncode(customer));

        return this.client.makeRequest("/customers/create", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customer
     * @return ApiResponse
     */
    public ApiResponse customersEdit(Map<String, Object> customer) {
        if (customer.isEmpty() || customer == null) {
            throw new IllegalArgumentException("Parameter `customer` must contains a data");
        }

        if (customer.containsKey("externalId") == false) {
            throw new IllegalArgumentException("Customer array must contain the \"externalId\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("customer", Tools.jsonEncode(customer));

        return this.client.makeRequest("/customers/" + customer.get("externalId").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customer
     * @param String by
     * @return ApiResponse
     */
    public ApiResponse customersEdit(Map<String, Object> customer, String by) {
        if (customer.isEmpty() || customer == null) {
            throw new IllegalArgumentException("Parameter `customer` must contains a data");
        }

        Tools.checkIdParameter(by);

        if (customer.containsKey(by) == false) {
            throw new IllegalArgumentException("Customer array must contain the \"" + by + "\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("customer", Tools.jsonEncode(customer));

        return this.client.makeRequest("/customers/" + customer.get(by).toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customer
     * @param String by
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse customersEdit(Map<String, Object> customer, String by, String site) {
        if (customer.isEmpty() || customer == null) {
            throw new IllegalArgumentException("Parameter `customer` must contains a data");
        }

        Tools.checkIdParameter(by);

        if (customer.containsKey(by) == false) {
            throw new IllegalArgumentException("Customer array must contain the \"" + by + "\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("customer", Tools.jsonEncode(customer));

        return this.client.makeRequest("/customers/" + customer.get(by).toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customers
     * @return ApiResponse
     */
    public ApiResponse customersUpload(Map<String, Object> customers) {
        if (customers.isEmpty() || customers == null) {
            throw new IllegalArgumentException("Parameter `customers` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("customers", Tools.jsonEncode(customers));

        return this.client.makeRequest("/customers/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> customers
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse customersUpload(Map<String, Object> customers, String site) {
        if (customers.isEmpty() || customers == null) {
            throw new IllegalArgumentException("Parameter `customers` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("customers", Tools.jsonEncode(customers));

        return this.client.makeRequest("/customers/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @param String id
     * @return ApiResponse
     */
    public ApiResponse customersGet(String id) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("by", "externalId");

        return this.client.makeRequest("/customers/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param String id
     * @param String by
     * @return ApiResponse
     */
    public ApiResponse customersGet(String id, String by) {
        Tools.checkIdParameter(by);

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("by", by);

        return this.client.makeRequest("/customers/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param String id
     * @param String by
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse customersGet(String id, String by, String site) {
        Tools.checkIdParameter(by);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("by", by);

        return this.client.makeRequest("/customers/" + id, Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @return ApiResponse
     */
    public ApiResponse customersList(Map<String, Object> filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);

        return this.client.makeRequest("/customers", Client.METHOD_GET, parameters);
    }

    /**
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse customersList(int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/customers", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse customersList(Map<String, Object> filter, int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/customers", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> ids
     * @return ApiResponse
     */
    public ApiResponse customersFixExternalIds(Map<String, Object> ids) {
        if (ids.isEmpty() || ids == null) {
            throw new IllegalArgumentException("Method parameter must contains at least one IDs pair");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("customers", Tools.jsonEncode(ids));

        return this.client.makeRequest("/customers/fix-external-ids", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @return ApiResponse
     */
    public ApiResponse packsList(Map<String, Object> filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);

        return this.client.makeRequest("/orders/packs", Client.METHOD_GET, parameters);
    }

    /**
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse packsList(int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders/packs", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse packsList(Map<String, Object> filter, int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders/packs", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> pack
     * @return ApiResponse
     */
    public ApiResponse packsCreate(Map<String, Object> pack) {
        if (pack.isEmpty() || pack == null) {
            throw new IllegalArgumentException("Parameter `pack` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pack", Tools.jsonEncode(pack));

        return this.client.makeRequest("/orders/packs/create", Client.METHOD_POST, parameters);
    }
    
    /**
     * @param Map<String, Object> filter
     * @return ApiResponse
     */
    public ApiResponse packsHistory(Map<String, Object> filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);

        return this.client.makeRequest("/orders/packs/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse packsHistory(int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders/packs/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse packsHistory(Map<String, Object> filter, int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/orders/packs/history", Client.METHOD_GET, parameters);
    }

    /**
     * @param String id
     * @return ApiResponse
     */
    public ApiResponse packsGet(String id) {
        return this.client.makeRequest("/orders/packs/" + id, Client.METHOD_GET, null);
    }

    /**
     * @param String id
     * @return ApiResponse
     */
    public ApiResponse packsDelete(String id) {
        return this.client.makeRequest("/orders/packs/" + id + "/delete", Client.METHOD_POST, null);
    }

    /**
     * @param Map<String, Object> customer
     * @param String by
     * @return ApiResponse
     */
    public ApiResponse packsEdit(String id, Map<String, Object> pack) {
        if (pack.isEmpty() || pack == null) {
            throw new IllegalArgumentException("Parameter `pack` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pack", Tools.jsonEncode(pack));

        return this.client.makeRequest("/orders/packs/" + id + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @return ApiResponse
     */
    public ApiResponse inventoriesList(Map<String, Object> filter) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);

        return this.client.makeRequest("/store/inventories", Client.METHOD_GET, parameters);
    }

    /**
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse inventoriesList(int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/store/inventories", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> filter
     * @param int page
     * @param int limit
     * @return ApiResponse
     */
    public ApiResponse inventoriesList(Map<String, Object> filter, int page, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("filter", filter);
        parameters.put("page", Integer.toString(page));
        parameters.put("limit", Integer.toString(limit));

        return this.client.makeRequest("/store/inventories", Client.METHOD_GET, parameters);
    }

    /**
     * @param Map<String, Object> offers
     * @return ApiResponse
     */
    public ApiResponse inventoriesUpload(Map<String, Object> offers) {
        if (offers.isEmpty() || offers == null) {
            throw new IllegalArgumentException("Parameter `offers` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        if (this.siteCode != null && this.siteCode.isEmpty() == false) {
            parameters.put("site", this.siteCode);
        }
        parameters.put("offers", Tools.jsonEncode(offers));

        return this.client.makeRequest("/store/inventories/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> offers
     * @param String site
     * @return ApiResponse
     */
    public ApiResponse inventoriesUpload(Map<String, Object> offers, String site) {
        if (offers.isEmpty() || offers == null) {
            throw new IllegalArgumentException("Parameter `offers` must contains a data");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", site);
        parameters.put("offers", Tools.jsonEncode(offers));

        return this.client.makeRequest("/store/inventories/upload", Client.METHOD_POST, parameters);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse deliveryServicesList() {
        return this.client.makeRequest("/reference/delivery-services", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse deliveryTypesList() {
        return this.client.makeRequest("/reference/delivery-types", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse orderMethodsList() {
        return this.client.makeRequest("/reference/order-methods", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse orderTypesList() {
        return this.client.makeRequest("/reference/order-types", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse paymentStatusesList() {
        return this.client.makeRequest("/reference/payment-statuses", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse paymentTypesList() {
        return this.client.makeRequest("/reference/payment-types", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse productStatusesList() {
        return this.client.makeRequest("/reference/product-statuses", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse statusGroupsList() {
        return this.client.makeRequest("/reference/status-groups", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse statusesList() {
        return this.client.makeRequest("/reference/statuses", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse sitesList() {
        return this.client.makeRequest("/reference/statuses", Client.METHOD_GET, null);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse storesList() {
        return this.client.makeRequest("/reference/stores", Client.METHOD_GET, null);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse deliveryServicesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deliveryService", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/delivery-services/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse deliveryTypesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deliveryType", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/delivery-types/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse orderMethodsEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orderMethod", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/order-methods/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse orderTypesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orderType", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/order-types/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse paymentStatusesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("paymentStatus", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/payment-statuses/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse paymentTypesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("paymentType", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/payment-types/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse productStatusesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("productStatus", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/product-statuses/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse statusesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("status", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/statuses/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse sitesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("site", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/sites/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @param Map<String, Object> data
     * @return ApiResponse
     */
    public ApiResponse storesEdit(Map<String, Object> data) {
        if (data.containsKey("code") == false) {
            throw new IllegalArgumentException("Data must contain \"code\" parameter");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("store", Tools.jsonEncode(data));

        return this.client.makeRequest("/reference/stores/" + data.get("code").toString() + "/edit", Client.METHOD_POST, parameters);
    }

    /**
     * @return ApiResponse
     */
    public ApiResponse statisticUpdate() {
        return this.client.makeRequest("/statistic/update", Client.METHOD_GET, null);
    }

    /**
     * @return String
     */
    public String getSite() {
        return this.siteCode;
    }

    /**
     * @param String site
     */
    public void setSite(String site) {
        this.siteCode = site;
    }
}

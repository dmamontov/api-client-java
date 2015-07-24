Java-клиент для retailCRM API
=============================

Java-клиент для работы с [RetailCRM API](http://www.retailcrm.ru/docs/rest-api/index.html).

version: 3.0.2

Обязательные требования
-----------------------
* [json-simple](https://code.google.com/p/json-simple/)

Установка через Maven
---------------------
В pom.xml своего проекта добавляем репозиторий:
``` xml
<repositories>
    <repository>
        <id>api-client-java-mvn-repo</id>
        <url>https://raw.github.com/retailcrm/api-client-java/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
После этого maven будет понимать откуда брать вашу библиотеку.

Теперь нужно подключить саму зависимость:
``` xml
<dependency>
    <groupId>ru.retailcrm.apiclient</groupId>
    <artifactId>RetailCRM.ApiClient</artifactId>
    <version>3.0.2</version>
</dependency>
```

Примеры использования
---------------------

### Получение информации о заказе

``` java

ApiClient api = new ApiClient(
    "https://demo.intarocrm.ru",
    "T9DMPvuNt7FQJMszHUdG8Fkt6xHsqngH"
);

ApiResponse response = api.ordersGet("M-2342");
Map<String, Object> result = response.getResult();

if (response.isSuccessful()) {
    System.out.println(result.totalSumm.ToString());
} else {
    System.out.println(
        "Ошибка получения информации о заказа: [Статус HTTP-ответа " +
        Integer.toString(response.getStatusCode()) + "] " +
        result.errorMsg.ToString()
    );
}
```
### Создание заказа

``` java

ApiClient api = new ApiClient(
    "https://demo.intarocrm.ru",
    "T9DMPvuNt7FQJMszHUdG8Fkt6xHsqngH"
);

Map<String, Object> order = new HashMap<String, Object>();
order.put("number", "example");
order.put("externalId", "example");
order.put("createdAt", "2015-05-12 12:27:00");
order.put("discount", 50);
order.put("phone", "example@gmail.com");
...
Map<String, Object> items = new HashMap<String, Object>();
Map<String, Object> item = new HashMap<String, Object>();
item.put("initialPrice", 100);
item.put("quantity", 1);
item.put("productId", 55);
item.put("productName", "example");

items.put("0", item);

order.put("items", items);
...
ApiResponse response = api.ordersEdit(order);
Map<String, Object> result = response.getResult();

if (response.isSuccessful() && 201 == response.getStatusCode()) {
    System.out.println("Заказ успешно создан. ID заказа в retailCRM = " + result.id);
} else {
    System.out.println(
        "Ошибка создания заказа: [Статус HTTP-ответа " +
        Integer.toString(response.getStatusCode()) + "] " +
        result.errorMsg.ToString()
    );
}

```

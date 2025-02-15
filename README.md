# Store

## Additional

Папки, которые расположены в корневой папке, а именно:
**store-discovery, store-config, store-gateway, store-order, store-product-catalog, store-email-notification** – следует
рассматривать как отдельные независимые модули.

## Modules

* **store-discovery**
    * port: 8761
* **store-config**
    * port: 8104
* **store-gateway**
    * port: 8100
* **store-order**
    * context-path: /store-order
    * port: 8102
* **store-product-catalog**
    * context-path: /store-product-catalog
    * port: 8101
* **store-email-notification**
    * context-path: /store-email-notification
    * port: 8105

### Deprecated

* **store-authorization**

## Run in Intellij IDEA

1. Откройте папку с проектом в Intellij IDEA
2. Добавьте каждый модуль как Maven проект

Например (модуль **store-discovery**)

* Открыть папку **store-discovery** ->
* найти файл **pom.xml** и кликнуть по нему правой клавишей мыши ->
* в меню выберите пункт **Add as Maven Project** ->
* найдите в **src** папке класс **StoreDiscoveryApplication**, выполните **run**

### Swagger-ui

http://localhost:8101/store-product-catalog/swagger-ui/index.html

http://localhost:8102/store-order/swagger-ui/index.html

### Actuator

http://localhost:8102/store-order/actuator/circuitbreakerevents

http://localhost:8102/store-order/actuator/retryevents
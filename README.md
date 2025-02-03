# Store

## Additional

Папки, которые расположены в корневой папке, а именно:
**store-authorization, store-discovery, store-gateway, store-order, store-product-catalog** – следует рассматривать как
отдельные независимые модули.

## Modules

* **store-authorization**
    * context-path: /store-authorization
    * port: 8103
* **store-discovery**
    * port: 8761
* **store-gateway**
    * port: 8100
* **store-order**
    * context-path: /store-order
    * port: 8102
* **store-product-catalog**
    * context-path: /store-product-catalog
    * port: 8101

## Run in Intellij IDEA

1. Откройта папку с проектом в Intellij IDEA
2. Добавьте каждый модуль как Maven проект

Например (модуль **store-discovery**)

* Открыть папку **store-discovery** ->
* найти файл **pom.xml** и кликнуть по нему правой клавишей мыши ->
* в меню выберите пункт **Add as Maven Project** ->
* найдите в **src** папке класс **StoreDiscoveryApplication**, выполните **run**

### Swagger-ui

http://localhost:8101/store-product-catalog/swagger-ui/index.html

http://localhost:8102/store-order/swagger-ui/index.html

http://localhost:8103/store-authorization/swagger-ui/index.html

### DB

http://localhost:8101/store-product-catalog/h2-console

http://localhost:8102/store-order/h2-console

http://localhost:8103/store-authorization/h2-console

### Actuator

http://localhost:8102/store-order/actuator/circuitbreakerevents

http://localhost:8102/store-order/actuator/retryevents
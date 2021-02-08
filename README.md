# Instrucciones de prueba de las historias de usuario:
En el archivo api-tests.postman_collection.json se encuentran las peticiones REST de cada historia de usuario. Este archivo se puede importar en Postman para probar el API fácilmente.
Alternativamente, estos son algunos ejemplos de peticiones que se deben hacer para los distintos endpoints:
* Realizar un pedido:

POST localhost:8080/purchases/

Cuerpo de la petición:

    {
        "customerAddress": "carrera 11# 14-08",
        "customerId": 12345,
        "products": [
            {
                "name": "prod1",
                "cost": 20000
            },
            {
                "name": "prod2",
                "cost": 50000
            }
        ]
    }
    
* Editar un pedido:

PUT localhost:8080/purchases/id_pedido

Cuerpo de la petición:

    {
        "customerAddress": "carrera 11# 14-08",
        "customerId": 12345,
        "products": [
            {
                "name": "prod1",
                "cost": 20000
            },
            {
                "name": "prod2",
                "cost": 50000
            },
            {
                "name": "prod3",
                "cost": 50000
            }
        ]
    }
    
* Cancelar un pedido:

DELETE localhost:8080/purchases/id_pedido

Adicionalmente, para ver los pedidos almacenados se puede usar GET localhost:8080/purchases/

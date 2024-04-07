
# Microservicio Arquitectura Hexadecimal

## Objetivo
Implementar un microservicio utilizando Java 17, Spring Boot 3, y Spring
WebFlux, aplicando los principios de la arquitectura hexagonal. El servicio se
conectará a una base de datos reactiva, gestionará los errores de forma
adecuada, e implementará una lógica de negocio utilizando Drools para decidir
el acceso a uno de dos posibles repositorios de datos. Adicionalmente, se
integrará un mecanismo de reintentos y circuit breaker para la conexión a la
base de datos. Se valorará el uso de buenas prácticas de programación y diseño
de software.

## Features

- Implementación de Circuit Breackers
- Conexión a dos bd reactivas de mongodb
- Manejo de errores en clase generica
- Logica de negocio con drools.
- Docker de MongoDB


## API Reference

#### GET all productos

```http
  GET /api/productos/{repositorio}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `repositorio` | `string` | **Required**. REPOA o REPOB para intercalar entre las dos fuentes de datos |

#### GET producto

```http
  GET /api/productos/{repositorio}/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `repositorio` | `string` | **Required**. REPOA o REPOB para intercalar entre las dos fuentes de datos |
| `id`      | `string` | **Required**. Id del producto |


#### POST producto
```http
  POST /api/productos/{repositorio}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `repositorio` | `string` | **Required**. REPOA o REPOB para intercalar entre las dos fuentes de datos |

**Objeto en Body**
    
    {
        "nombre": "Tv LG 8k 80 pulgadas",
        "precio": 7600000.0    
    }

#### PUT producto

```http
  PUT /api/productos/{repositorio}/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `repositorio` | `string` | **Required**. REPOA o REPOB para intercalar entre las dos fuentes de datos |
| `id`      | `string` | **Required**. Id del producto |

**Objeto en Body**
    
    {
        "nombre": "Tv LG 8k 80 pulgadas",
        "precio": 7600000.0    
    }

#### DELETE producto

```http
  DELETE /api/productos/{repositorio}/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `repositorio` | `string` | **Required**. REPOA o REPOB para intercalar entre las dos fuentes de datos |
| `id`      | `string` | **Required**. Id del producto |

**Objeto en Body**
    
    {
        "nombre": "Tv LG 8k 80 pulgadas",
        "precio": 7600000.0    
    }


## Running Tests

Para correr las pruebas unitaras, se posicionan en la carpeta del proyecto y ejecutan el comando

```bash
  mvn test
```


## Deployment MongoDB

Para desplegar la base de datos ingresar a la carpeta docker y ejecutar el comando

```bash
  docker-compose up
```


## Circuit Breacker

Para realizar las pruebas controladas de Circuit Breacker. Se adicionar la palabra error a una consulta por producto

**Ejemplo**
    
    GET /api/v1/productos/REPOB/66130f3fd319953e1d166a66error

La respuesta sera parecida a la siguiente

    {
        "timestamp": "2024-04-07T22:01:44.532545Z",
        "status": 500,
        "error": [
            "Error controlado para CircuitBreacker"
        ],
        "type": "Exception",
        "path": "",
        "message": "Internal Server Error"
    }

El sliding-windows-size esta parametrizado en 10 y el rango de error es del 50%, asi que deben generar el error por lo menos 6 veces para que el circuito se abra. 
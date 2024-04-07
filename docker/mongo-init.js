db1 = db.getSiblingDB("a-store");

db1.createCollection('productos');

db1.productos.insertMany([
    {
        "id": "66121266e84f646c0836b5df",
        "nombre": "Lavadora LG",
        "precio": 1400000.0,
        "createAt": new ISODate("2024-04-07T03:26:30.775+00:00")
    },
    {
        "id": "66121266e84f646c0836b5de",
        "nombre": "iPhone 14",
        "precio": 4000000.0,
        "createAt": new ISODate("2024-04-07T03:26:30.775+00:00")
    },
    {
        "id": "66121266e84f646c0836b5dd",
        "nombre": "Tv Samsung",
        "precio": 2000000.0,
        "createAt": new ISODate("2024-04-07T03:26:30.751+00:00")
    }
]);

db2 = db.getSiblingDB("b-store");

db2.createCollection('productos');


db2.productos.insertMany([
    {
        "id": "66121266e84f646c0836b5e2",
        "nombre": "Teatro en casa Bose",
        "precio": 3400000.0,
        "createAt": new ISODate("2024-04-07T03:26:30.779+00:00")
    },
    {
        "id": "66121266e84f646c0836b5e1",
        "nombre": "iPhone 14 Pro Max",
        "precio": 8000000.0,
        "createAt": new ISODate("2024-04-07T03:26:30.778+00:00")
    },
    {
        "id": "6612e590b0dbe44b845fc32b",
        "nombre": "Tv LG 8k 80 pulgadas",
        "precio": 7600000.0,
        "createAt": new ISODate("2024-04-07T18:27:28.113+00:00")
    }
]);
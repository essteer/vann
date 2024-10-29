# Endpoints

This page is a reference list of commonly used endpoints for this backend server. See the controller layer classes for a full list of available endpoints.

Click [here](http://localhost:9001) to return to the landing page.

## Categories

### Bulk create categories

POST [http://localhost:9001/api/v1/categories](http://localhost:9001/api/v1/categories)

Request body:

```json
[
  {
    "type": "BRACELET",
    "name": "bracelets"
  }
]
```

### Get all categories

GET [http://localhost:9001/api/v1/categories](http://localhost:9001/api/v1/categories)

Response body:

```json
[
  {
    "id": "{id}",
    "type": "BRACELET",
    "name": "bracelets"
  }
]
```

## Products

### Bulk create products

POST [http://localhost:9001/api/v1/products](http://localhost:9001/api/v1/products)

Request body:

```json
[
  {
    "category": { "name": "bracelets" },
    "name": "NOVA BRACELET",
    "price": 365,
    "imageURI": "{image-uri}",
    "colour": "WHITE_AND_YELLOW_GOLD"
  },
  {
    "category": { "name": "bracelets" },
    "name": "PEARL BRACELET",
    "price": 285,
    "imageURI": "{image-uri}",
    "colour": "DEEP_PINK",
    "size": "SMALL"
  },
  ...
]
```

### Create a single product

POST [http://localhost:9001/api/v1/products](http://localhost:9001/api/v1/products)

Request body:

```json
{
  "category": { "name": "earrings" },
  "name": "OYSTER STUD EARRINGS",
  "price": 300,
  "imageURI": "{image-uri}",
  "colour": "COBALT_BLUE"
}
```

### Get all products

GET [http://localhost:9001/api/v1/products](http://localhost:9001/api/v1/products)

Response body:

```json
[
  {
    "id": "{id}",
    "category": {
      "id": "{id}",
      "type": "BRACELET",
      "name": "bracelets"
    },
    "name": "FLAT ROCK AND SPIKY KNOT BRACELET",
    "price": 430.0,
    "imageURI": "{image-uri}",
    "size": "MEDIUM",
    "colour": "WHITE_GOLD",
    "featuredStatus": false
  },
  ...
]
```

### Get a single product by ID

GET [http://localhost:9001/api/v1/products/{id}](http://localhost:9001/api/v1/products/{id})

Response body:

```json
{
  "id": "{id}",
  "category": {
    "id": "{category-id}",
    "type": "EARRING",
    "name": "earrings"
  },
  "name": "XL MOLTEN EARRINGS",
  "price": 575.0,
  "imageURI": "{image-uri}",
  "size": null,
  "colour": "SILVER",
  "featuredStatus": true
}
```

## Users

### Create a single user

POST [http://localhost:9001/api/v1/users](http://localhost:9001/api/v1/users)

Request body:

```json
{
  "name": "Demo User",
  "email": "demo-user@domain.com"
}
```

### Get all users

GET [http://localhost:9001/api/v1/users](http://localhost:9001/api/v1/users)

Response body:

```json
[
  {
    "id": "{id}",
    "name": "Demo User",
    "email": "demo-user@domain.com"
  },
  ...
]
```

### Get a single user by ID

GET [http://localhost:9001/api/v1/users/{id}](http://localhost:9001/api/v1/users/{id})

Response body:

```json
{
  "id": "{id}",
  "name": "Demo User",
  "email": "demo-user@domain.com"
}
```

## Carts

### Get a single cart by user ID

GET [http://localhost:9001/api/v1/carts/user/{id}](http://localhost:9001/api/v1/carts/user/{id})

Response body:

```json
{
  "id": "{id}",
  "user": {
    "id": "{user-id}",
    "name": "Demo User",
    "email": "demo-user@domain.com"
  },
  "cartItems": {}
}
```

### Update a cart by cart ID

PUT [http://localhost:9001/api/v1/carts/{id}](http://localhost:9001/api/v1/carts/{id})

Request body:

```json
{
  "{product-id}": 1,
  "{product-id}": 2
}
```

### Checkout a cart by cart ID

PUT [http://localhost:9001/api/v1/carts/checkout/{id}](http://localhost:9001/api/v1/carts/checkout/{id})

Request body:

```json
{
  "billingAddress": "123 High Street",
  "shippingAddress": "45 Main Street"
}
```

Response body:

```json
{
  "id": "{invoice-id}",
  "user": {
    "id": "{id}",
    "name": "Demo User",
    "email": "demo-user@domain.com"
  },
  "billingAddress": "123 High Street",
  "shippingAddress": "45 Main Street",
  "totalAmount": 710.0,
  "invoiceItems": [
    {
      "id": "{id}",
      "productId": "{id}",
      "quantity": 1,
      "unitPrice": 430.0,
      "productDetails": "..."
    },
    {
      "id": "{id}",
      "productId": "{id}",
      "quantity": 2,
      "unitPrice": 140.0,
      "productDetails": "..."
    }
  ]
}
```

## Invoices

### Get all invoices

GET [http://localhost:9001/api/v1/invoices](http://localhost:9001/api/v1/invoices)

Response body:

```json
[
  {
    "id": "{invoice-id}",
    "user": {
      "id": "{id}",
      "name": "Demo User",
      "email": "demo-user@domain.com"
    },
    "billingAddress": "123 High Street",
    "shippingAddress": "45 Main Street",
    "totalAmount": 710.0,
    "invoiceItems": [
      {
        "id": "{id}",
        "productId": "{id}",
        "quantity": 1,
        "unitPrice": 430.0,
        "productDetails": "..."
      },
      {
        "id": "{id}",
        "productId": "{id}",
        "quantity": 2,
        "unitPrice": 140.0,
        "productDetails": "..."
      }
    ]
  },
  ...
]
```

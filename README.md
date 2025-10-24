# 🏭 Warehouse Inventory Management System

A backend system for managing a shop's warehouse inventory, built with Java 17, Spring Boot 3.x, and H2 in-memory database.  
This application allows tracking of items, their variants, pricing, and stock levels, and provides RESTful APIs for managing inventory, selling items, and adjusting stock.

---

## 📚 Table of Contents
- [Features](#features)
- [Technical Stack](#technical-stack)
- [Architecture Overview](#architecture-overview)
- [Getting Started](#getting-started)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Design Decisions](#design-decisions)
- [Error Handling](#error-handling)
- [Assumptions](#assumptions)
- [Sample Data](#sample-data)

---

## ✨ Features
- **Items & Variants:** Manage items and their variants (e.g., different sizes, colors)  
- **Pricing:** Each variant has its own price  
- **Stock Management:** Track inventory and prevent selling out-of-stock items  
- **CRUD Operations:** Full create, read, update, delete for items and variants  
- **Bulk Operations:** Sell multiple variants in one request  
- **RESTful API:** Exposes endpoints for managing inventory and sales  
- **Transactional Integrity:** Stock updates are handled transactionally  

---

## 🧰 Technical Stack
- **Language:** Java 17  
- **Framework:** Spring Boot 3.x  
- **Database:** H2 (in-memory)  
- **Build Tool:** Maven  
- **Version Control:** Git  
- **Libraries:**
  - Lombok
  - Spring Data JPA
  - Jakarta Validation
  - SLF4J Logging

---

## 🧱 Architecture Overview

### 📊 High-Level Design
```
        +----------------------------+
        |        Client (API)        |
        +-------------+--------------+
                      |
                      v
          +-----------+-----------+
          |       Controller      |   <-- REST layer (HTTP endpoints)
          +-----------+-----------+
                      |
                      v
          +-----------+-----------+
          |        Service         |   <-- Business logic (validation, transaction)
          +-----------+-----------+
                      |
                      v
          +-----------+-----------+
          |       Repository       |   <-- Data access via Spring Data JPA
          +-----------+-----------+
                      |
                      v
          +-----------+-----------+
          |         Database       |   <-- H2 in-memory database
          +------------------------+
```

### 🧩 Layers
- **Controller Layer:** Handles HTTP requests and returns standardized responses.  
- **Service Layer:** Contains business logic, validation, and transactional control.  
- **Repository Layer:** Handles persistence using JPA repositories.  
- **Database:** Stores items and variants with referential integrity.

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.x
- Git

### Steps
# Clone the repository
git clone https://github.com/kwirawibawa/warehouse.git
cd warehouse

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run


### Access Points
- API Base URL → `http://localhost:8080`
- H2 Console → `http://localhost:8080/h2-console`  
  JDBC URL: jdbc:h2:mem:warehouse
  Username: sa
  Password:

---

## 🗄️ Database Schema

### Item
| Column       | Type     | Description          |
|--------------|----------|----------------------|
| id           | BIGINT   | Primary Key          |
| name         | VARCHAR  | Item name (unique)   |
| description  | VARCHAR  | Item description     |

### Variant
| Column    | Type     | Description               |
|-----------|----------|---------------------------|
| id        | BIGINT   | Primary Key               |
| sku       | VARCHAR  | Unique SKU                |
| name      | VARCHAR  | Variant name              |
| price     | DECIMAL  | Variant price             |
| stock     | BIGINT   | Available stock           |
| version   | BIGINT   | Optimistic locking version|
| item_id   | BIGINT   | FK → Item(id)             |

---

## 🔌 API Endpoints

### **Items**
- `POST /item/create` → Create new item with optional variants  
- `GET /item/getItems` → Get all items  
- `GET /item/{id}` → Get item by ID  
- `PUT /item/update` → Update item and variants  
- `DELETE /item/delete/{id}` → Delete item  

### **Variants**
- `POST /variant/create` → Create a variant for an existing item  
- `GET /variant/getVariants` → Get all variants  
- `GET /variant/{id}` → Get variant by ID  
- `GET /variant/sku/{sku}` → Get variant by SKU  
- `PUT /variant/update` → Update variant  
- `DELETE /variant/delete/{id}` → Delete variant  

### **Stock & Sales**
- `POST /warehouse/sell` → Sell variant by ID  
- `POST /warehouse/sku/sell` → Sell variant by SKU  
- `POST /warehouse/sell/bulk` → Sell multiple variants  
- `GET /warehouse/stock/{id}` → Get stock by variant ID  
- `GET /warehouse/stock/sku/{sku}` → Get stock by SKU  
- `PATCH /warehouse/stock/adjust` → Adjust stock by ID  
- `PATCH /warehouse/stock/sku/adjust` → Adjust stock by SKU  
- `GET /stock/getAll` → Retrieve total stock summary  

---

## 📬 Example Requests

### Create a new item with variants
curl -X POST http://localhost:8080/item/create -H "Content-Type: application/json" -d '{
   "name": "T-Shirt",
   "description": "Cotton T-shirt",
   "variants": [
      {"sku": "TSHIRT-RED-M", "name": "Red - M", "price": 120000, "stock": 10},
      {"sku": "TSHIRT-BLUE-L", "name": "Blue - L", "price": 115000, "stock": 5}
   ]
}'

### Create a variant for an existing item

curl -X POST http://localhost:8080/variant/create -H "Content-Type: application/json" -d '{
   "itemId": 1,
   "sku": "HOODIE-BLACK-XL",
   "name": "Hoodie Black - XL",
   "price": 260000,
   "stock": 10
}'


### Get all stock
curl -X GET http://localhost:8080/stock/getAll


### Example Success Response
{
  "code": "200",
  "message": "Item created successfully",
  "data": {
    "id": 1,
    "name": "T-Shirt",
    "variants": [
      {"id": 1, "sku": "TSHIRT-RED-M", "price": 120000, "stock": 10}
    ]
  }
}

---

## ⚠️ Error Handling

All errors are returned in a consistent JSON structure.

### Example Error Response
{
  "code": "409",
  "message": "Variant with SKU 'TSHIRT-RED-M' already exists"
}

### Common Error Codes
| HTTP Status  | Code                  | Description                         |
|--------------|-----------------------|-------------------------------------|
| 400          | BAD_REQUEST           | Invalid request or validation error |
| 404          | NOT_FOUND             | Resource not found                  |
| 409          | CONFLICT              | Duplicate item or SKU               |
| 500          | INTERNAL_SERVER_ERROR | Unhandled exception                 |

### Exception Handled
- `EntityNotFoundException` → 404 Not Found  
- `ResponseStatusException` → Uses defined HTTP status (e.g. 409 Conflict)  
- `MethodArgumentNotValidException` → 400 Bad Request (validation errors)  
- `NotEnoughStockException` → 400 Bad Request (insufficient stock)  
- Generic `Exception` → 500 Internal Server Error  

---

## 🧠 Design Decisions

- **Layered Architecture:** Clear separation between controller, service, and repository.  
- **Validation Rules:**
  - Item name must be unique  
  - SKU must be unique across variants  
  - Price must be positive  
  - Stock must be zero or greater  
- **Transactional Operations:** Critical updates (create, sell, adjust) are annotated with `@Transactional`.  
- **DTO Mapping:** Clean separation between entities and exposed data.  
- **Error Consistency:** All errors follow a single standardized response format.  
- **Logging:** Handled via SLF4J for clean tracking of warehouse operations.  
- **H2 Database:** Used for simplicity and automatic persistence during runtime.  

---

## 🧩 Assumptions

- Item can exist without variants  
- SKU is globally unique  
- Stock cannot be negative  
- No authentication/authorization included  
- Optimistic locking used via `version` column  

---

## 🧪 Sample Data
INSERT INTO ITEM (name, description)
VALUES
    ('Hoodie', 'Comfy warm hoodie');

INSERT INTO VARIANT (sku, name, price, stock, item_id, version)
VALUES
    ('HOODIE-BLACK-L','Black - L', 250000, 7,  1, 0),
    ('HOODIE-GRAY-M', 'Gray - M',  245000, 12, 1, 0);


---

**👨‍💻 Author:** Kresna Wirawibawa  

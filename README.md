# E-commerce Backend

A robust and scalable Spring Boot application for managing an e-commerce platform. This backend solution offers features like order management, real-time notifications, and efficient stock control, ensuring seamless operations for online businesses.

---

## **Features**

### **Order Management**
- Place, update, and cancel orders with built-in validations.
- Automatic stock updates during order processing.
- Integrated payment validation to ensure seamless transactions.

### **Real-time Notifications**
- Send instant notifications to users using WebSocket (STOMP).
- User-specific notifications for order status changes.

### **Error Handling**
- Comprehensive exception handling with meaningful error messages.
- Global exception management via `@ControllerAdvice`.

### **Stock Management**
- Real-time updates for product stock.
- Products marked "Out of Stock" when unavailable.

---

## **Technologies Used**

- **Programming Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **ORM**: Hibernate/JPA
- **Real-time Communication**: WebSocket (STOMP)
- **Logging**: SLF4J
- **API Documentation**: Swagger UI

---

## **Getting Started**

### **Prerequisites**
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### **Setup Instructions**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/sanjaysamsoth/E-commerce-Backend.git
   cd E-commerce-Backend
   ```

2. **Configure the Database**:
   Update the `application.properties` file:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
   spring.datasource.username=<your-db-username>
   spring.datasource.password=<your-db-password>
   ```

3. **Build and Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access API Documentation**:
   Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to explore the available endpoints.

---

## **Endpoints Overview**

### **Orders**
| Method | Endpoint             | Description               |
|--------|----------------------|---------------------------|
| POST   | `/orders`            | Place a new order         |
| GET    | `/orders`            | Retrieve all orders       |
| GET    | `/orders/{orderId}`  | Retrieve an order by ID   |
| PUT    | `/orders/{orderId}`  | Update an existing order  |
| DELETE | `/orders/{orderId}`  | Cancel an order           |

---

### **Products**
| Method | Endpoint                  | Description                           |
|--------|---------------------------|---------------------------------------|
| POST   | `/products`               | Add a new product to the catalog      |
| GET    | `/products`               | Retrieve all products                 |
| GET    | `/products/{productId}`   | Retrieve a product by ID              |
| PUT    | `/products/{productId}`   | Update a product                      |
| DELETE | `/products/{productId}`   | Delete a product                      |

---

### **Notifications**
| Method | Endpoint                    | Description                           |
|--------|-----------------------------|---------------------------------------|
| GET    | `/notifications/{userId}`   | Fetch notifications for a specific user |
| POST   | `/notifications/broadcast`  | Broadcast a message to all users      |
| POST   | `/notifications/send`       | Send a notification to a specific user |

---

### **Customers**
| Method | Endpoint                    | Description                           |
|--------|-----------------------------|---------------------------------------|
| POST   | `/customers/register`       | Register a new customer               |
| GET    | `/customers/{customerId}`   | Retrieve a customer by ID             |
| PUT    | `/customers/{customerId}`   | Update customer information           |
| DELETE | `/customers/{customerId}`   | Delete a customer account             |

---

### **Sellers**
| Method | Endpoint                    | Description                           |
|--------|-----------------------------|---------------------------------------|
| POST   | `/sellers/register`         | Register a new seller                 |
| GET    | `/sellers/{sellerId}`       | Retrieve a seller by ID               |
| PUT    | `/sellers/{sellerId}`       | Update seller information             |
| DELETE | `/sellers/{sellerId}`       | Delete a seller account               |

---

## **Sample API Response for Customer Login**

#### **POST**   `localhost:8009/login/customer`

#### Request Body
```json
{
    "mobileId": "9876543210",
    "password": "sandeep123456"
}
```

#### Response
```json
{
    "sessionId": 3,
    "token": "customer_0ad87569",
    "userId": 20,
    "userType": "customer",
    "sessionStartTime": "2025-01-11T11:48:20.0109626",
    "sessionEndTime": "2025-01-11T11:55:20.0109626"
}
```

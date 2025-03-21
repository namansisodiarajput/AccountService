# Account Service

This is a backend service for managing bank accounts and handling transactions. It is built using **Spring Boot** and follows RESTful API principles.

---

## APIs Supported

### **Account APIs**

1. **Create a New Account**
    - **Endpoint:**  
      **POST** `/api/accounts`

    - **Request Body:**
      ```json
      {
        "accountHolderName": "Rajesh Kumar",
        "accountNumber": "987654321098",
        "ifscCode": "HDFC0001234",
        "accountType": "CURRENT",
        "balance": 5000.00
      }
      ```

    - **Response:**
      ```json
      {
        "accountId": "8f47a62c-1234-4567-8901-fedcba098765"
      }
      ```

    - **Description:**  
      This API endpoint creates a new bank account with the given details.

---

2. **Get Account by ID**
    - **Endpoint:**  
      **GET** `/api/accounts/{accountId}`

    - **Path Parameter:**
        - `accountId`: The UUID of the account.

    - **Response:**
      ```json
      {
        "accountId": "123e4567-e89b-12d3-a456-426614174000",
        "accountHolderName": "Sneha Nair",
        "accountNumber": "445566778899",
        "ifscCode": "SBI12345678",
        "accountType": "CURRENT",
        "balance": 120000.00,
        "createdOn": "2025-03-20T11:15:00",
        "updatedOn": "2025-03-21T09:45:00"
      }
      ```
<img width="798" alt="image" src="https://github.com/user-attachments/assets/ca98ea37-b9fe-4b2b-a65a-ca6865594d20" />
<img width="871" alt="image" src="https://github.com/user-attachments/assets/77f50c8b-6653-4be3-ba90-1d989d1c54e1" />
---

### **Transaction APIs**

1. **Create a New Transaction**
    - **Endpoint:**  
      **POST** `/api/transactions`

    - **Request Body:**
      ```json
      {
        "amount": 1000.00,
        "transactionType": "CREDIT",
        "transactionDate": "2025-03-21T14:30:00",
        "accountId": "123e4567-e89b-12d3-a456-426614174000"
      }
      ```

    - **Response:**
      ```json
      {
        "transactionId": "9f21c62c-9876-5432-1234-fedcba098765"
      }
      ```

    - **Description:**  
      This API endpoint creates a new transaction (credit or debit) for a specified account.

    - **Notes:**
        - `amount`: The transaction amount (must be a non-negative decimal).
        - `transactionType`: Can be either `CREDIT` or `DEBIT`.
        - `transactionDate`: The date and time when the transaction took place.
        - `accountId`: The UUID of the account to which this transaction is linked.

---

2. **Get Transactions by Account ID**
    - **Endpoint:**  
      **GET** `/api/transactions/{accountId}`

    - **Path Parameter:**
        - `accountId`: The UUID of the account whose transactions are to be retrieved.

    - **Response:**
      ```json
      [
        {
          "transactionId": "9f21c62c-9876-5432-1234-fedcba098765",
          "amount": 1000.00,
          "transactionType": "CREDIT",
          "transactionDate": "2025-03-21T14:30:00",
          "accountId": "123e4567-e89b-12d3-a456-426614174000"
        },
        {
          "transactionId": "4f32d11a-5678-4321-9876-cba098765432",
          "amount": 500.00,
          "transactionType": "DEBIT",
          "transactionDate": "2025-03-21T15:45:00",
          "accountId": "123e4567-e89b-12d3-a456-426614174000"
        }
      ]
      ```

    - **Description:**  
      This API endpoint fetches a list of transactions associated with the given account ID.

<img width="927" alt="image" src="https://github.com/user-attachments/assets/8ee34691-2968-4bf9-be9d-eb91ad2ccb34" />
<img width="601" alt="image" src="https://github.com/user-attachments/assets/b4b474b2-3892-4aac-bb55-0e383222ba31" />

---

## **H2 Database Console**

The application uses **H2 In-Memory Database** for easy testing and demo purposes.  
To access the H2 Console, use the following URL after starting the application:  
**[H2 Database Console](http://localhost:8080/h2-console)**

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty by default)

<img width="547" alt="image" src="https://github.com/user-attachments/assets/7761e12c-c80d-41b0-9418-427e19a7046f" />
<img width="429" alt="image" src="https://github.com/user-attachments/assets/84c1063b-d4d6-4a74-8454-bb509dc07af8" />
<img width="1233" alt="image" src="https://github.com/user-attachments/assets/b7e2761b-9ae9-4463-8aa4-c44212fb2665" />
<img width="1221" alt="image" src="https://github.com/user-attachments/assets/dac41520-54da-4b29-81c3-228af7aa4ce0" />

---

## **Swagger Documentation**

The API comes with integrated Swagger UI for API documentation and testing. You can access it at:  
**[Swagger UI](http://localhost:8080/swagger-ui.html)**
<img width="1583" alt="image" src="https://github.com/user-attachments/assets/2d78fdb7-e3d4-41bf-8298-bd671db5bcdc" />

---

## **Tests and Their Locations**
<img width="374" alt="image" src="https://github.com/user-attachments/assets/99850881-f033-430c-aa96-7d95a558e728" />

### 1. **Unit Tests**
- **Purpose:** Test individual methods and functionalities in isolation.
- **Location:**
    - `src/test/java/com/allica/backend/services`
    - Example tests include `AccountServiceTest` and `TransactionServiceTest`.

### 2. **Integration Tests**
- **Purpose:** Test the integration between various components (e.g., controllers, services, and repositories) and ensure they work together as expected.
- **Location:**
    - `src/test/java/com/allica/backend/integration`
    - Example tests include `AccountControllerIntegrationTest` and `TransactionControllerIntegrationTest`.

### 3. **API Tests**
- **Purpose:** Test the REST APIs end-to-end, including input validation, response structure, and error handling.
- **Location:**
    - `src/test/java/com/allica/backend/controllers`
    - Example tests include `AccountControllerTest` and `TransactionControllerTest`.

---

## **Project Structure**

### **Core Packages:**
<img width="601" alt="image" src="https://github.com/user-attachments/assets/04def3c7-f361-426a-83ea-91242dc74907" />

1. **Controllers** – Handle REST APIs.
   - Location: `src/main/java/com/allica/backend/controllers`
   - Example: `AccountController`, `TransactionController`

2. **Entities** – Represent database tables.
   - Location: `src/main/java/com/allica/backend/entities`
   - Example: `AccountEntity`, `TransactionEntity`

3. **Enums** – Define constants.
   - Location: `src/main/java/com/allica/backend/enums`
   - Example: `AccountType`, `TransactionType`

4. **Exceptions** – Handle custom errors.
   - Location: `src/main/java/com/allica/backend/exceptions`
   - Example: `AccountNotFoundException`, `InsufficientBalanceException`

5. **Mappers** – Convert DTOs to entities.
   - Location: `src/main/java/com/allica/backend/mappers`
   - Example: `AccountMapper`, `TransactionMapper`

6. **Repositories** – Manage database operations.
   - Location: `src/main/java/com/allica/backend/repositories`
   - Example: `AccountRepository`, `TransactionRepository`

7. **Services** – Implement business logic.
   - Location: `src/main/java/com/allica/backend/services`
   - Example: `AccountService`, `TransactionService`

---

## **Technologies Used**
- **Spring Boot** – Backend framework
- **JPA/Hibernate** – For database interaction
- **H2 Database** – In-memory database (for testing)
- **Swagger/OpenAPI** – API documentation
- **JUnit 5** – Unit, integration, and API testing

---

## **Author**
Developed by **Naman Sisodia**.

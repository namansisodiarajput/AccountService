# Account Service

This is a backend service for managing bank accounts and handling transactions. It is built using **Spring Boot** and follows RESTful API principles.

---

## APIs Supported

### **Account APIs**

- **Create a new account**  
  **POST** `/api/accounts`  
  **Request Body:**
  ```json
  {
    "accountHolderName": "John Doe",
    "accountNumber": "123456789012",
    "ifscCode": "ABC12345678",
    "accountType": "SAVINGS",
    "balance": 1000.00
  }

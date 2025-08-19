# 💅 Beauty Salon Management System

**Short Description:**
A Java-based **networked beauty salon management system** using **Hibernate** for database operations, created as an educational project. It manages clients, appointments, and services over sockets.

---

## 📖 Overview

This project simulates the operations of a beauty salon using **Java**.
It was developed as a course project and demonstrates **object-oriented programming**, **database integration with Hibernate**, and **network communication using sockets**.

---

## ✨ Features

* **Client Management:** add, update, and view client information
* **Appointments & Scheduling:** book, cancel, and list appointments
* **Service Management:** manage available salon services
* **Database Integration:** uses Hibernate ORM to persist data
* **Networking:** client-server architecture using sockets for remote interactions

---

## 🛠 Technologies

* **Language:** Java
* **Database:** Hibernate ORM (requires a relational database setup)
* **Networking:** Java Sockets
* **IDE Support:** IntelliJ IDEA
* **Build Tool:** Maven (via `pom.xml`)

---

## 📂 Project Structure

```
/.idea                   # IntelliJ IDEA project settings
/src                     # Java source code
    /client              # Client-side logic
    /server              # Server-side logic
.gitignore              
beautySalon.iml          # IntelliJ module file
pom.xml                  # Maven build configuration
```

---

## 🚀 Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/jasiuhavingfun/beautySalon.git
   cd beautySalon
   ```
2. Open the project in **IntelliJ IDEA** (or another Java IDE).
3. Configure the database for Hibernate (e.g., MySQL, H2) and update `hibernate.cfg.xml`.
4. Run the **server** main class first to start the backend.
5. Run the **client** main class to interact with the system over sockets.
6. Use the client application to manage clients, services, and appointments.

---

## 📜 License

This project was created for educational purposes.
Feel free to use or adapt it for learning or coursework.

---

## 👤 Author

Created by **[@jasiuhavingfun](https://github.com/jasiuhavingfun)**

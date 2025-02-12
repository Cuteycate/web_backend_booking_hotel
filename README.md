![image](https://github.com/user-attachments/assets/03b4a663-13af-451f-bdc9-4183d95795b4)# Booking Application

## Description
This Booking Application is a web platform that allows users to book hotels online. Built with **Spring Boot** for the backend and **React + Vite** for the frontend, the application enables users to search for hotels, make reservations, and manage bookings seamlessly.

### Motivation
The project was developed to provide a user-friendly and secure hotel booking system, enhancing the experience of both customers and rental providers.

### Why was this built?
This project was created to simplify the process of hotel booking by offering a modern user interface, robust authentication, and a seamless reservation experience.

### Problem it solves
The system addresses issues such as spam registrations, inefficient booking management, lack of real-time availability checks, and seamless online payment integration.

### What did I learn?
Through this project, I gained experience in:
- Implementing **JWT authentication** for secure API communication.
- Integrating **email confirmation** and **password recovery**.
- Utilizing **React + Vite** for a fast and optimized frontend.
- Managing real-time hotel availability and search suggestions.
- Implementing **online payment** for deposits and full payments.
- Handling **multi-role access control** for users, rentals, and admins.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Credits](#credits)
- [Features](#features)

## Installation

### Backend (Spring Boot)
#### Prerequisites
- Java 17+
- Maven
- PostgreSQL or MySQL database

#### Steps
1. Clone the repository from GitHub.
2. Configure the database in `application.properties`.
3. Run the backend using:
   ```sh
   mvn spring-boot:run
   ```

### Frontend (React + Vite)
The project consists of three separate frontends: **Admin, Rental, User**.

#### Prerequisites
- Node.js (v16 or higher)

#### Steps
1. Navigate to each frontend folder (`admin-frontend`, `rental-frontend`, `user-frontend`).
2. Install dependencies:
   ```sh
   npm install
   ```
3. Run the frontend:
   ```sh
   npm run dev
   ```

## Usage
Once both the backend and frontend are running, navigate to `http://localhost:3000` in your browser.

Below are some screenshots showcasing key features:

### Homepage:

![Homepage](https://github.com/user-attachments/assets/ff93ee46-9a0c-49c0-af9f-55e107c51b9e)
)

### Hotel Search:

![Hotel Search](#)

### Booking Process:

![Booking](#)

### Payment Integration (Online Deposit/Full Payment):

![Payment](#)

### Email Confirmation:

![Email Confirmation](#)

### Admin Dashboard:

![Admin Dashboard](#)

### Rental Management:

![Rental Management](#)

## Credits
- **[Your Name]** - Fullstack Developer

## Features
- **JWT Authentication** for secure API requests.
- **User Registration/Login** with email confirmation.
- **Hotel Search & Filtering** based on location and availability.
- **Dynamic Hotel & Room Display** with real-time availability.
- **Booking System** with date selection and room limit.
- **Payment Integration** for deposits and full payments.
- **Admin Panel** for user and rental management.
- **Rental Management** for hotel owners to add/manage properties.
- **Email Notifications** for booking confirmation and check-in codes.
- **Responsive Design** for mobile and desktop users.

---

_Add screenshots and more details as needed._

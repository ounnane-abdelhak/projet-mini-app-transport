# ESI-RUN Transport Management System

The **ESI-RUN Transport Management System** is a sophisticated desktop application developed in Java, specifically designed to streamline the operations of a modern transport network. By leveraging the **JavaFX** framework, the application provides a responsive and intuitive graphical user interface that caters to both administrative staff and end-users. The system serves as a centralized hub for managing the complex lifecycle of transport services, from user registration to real-time fare validation and customer feedback loops.

## Core Functionalities

The application is built around several key modules that ensure comprehensive management of transport operations. At its heart, the **User Management** system distinguishes between various roles, such as employees and passengers, allowing for tailored access and functionality. The **Ticketing and Fare** module handles the logic for diverse transport titles, including standard tickets and personalized cards, while enforcing strict validation rules to prevent fraud and ensure service integrity. Furthermore, the integrated **Complaint Management** service provides a structured way for users to voice concerns, which are then tracked and managed by the system to improve service quality.

| Feature | Description |
| :--- | :--- |
| **User Roles** | Supports distinct profiles for regular passengers and transport employees. |
| **Fare Validation** | Automated logic for checking the validity of tickets and personal transport cards. |
| **Data Persistence** | Robust system for loading and saving application state, including users and complaints. |
| **Interactive UI** | Multi-screen interface built with FXML for a seamless user experience. |

## Architectural Overview

The project adheres to a clean, modular architecture that separates concerns across different packages. The `transport.core` package serves as the engine of the application, housing the essential business logic, data models, and custom exception handling. Within this layer, the `DataManager` class orchestrates data input and output operations, while the `ServiceReclamation` class manages the lifecycle of customer complaints. The visual layer, located in `transport.ui`, utilizes FXML to define layouts such as the `WelcomeScreen` and `UserManagementScreen`, ensuring that the presentation remains decoupled from the underlying logic.

## Getting Started

To deploy the **ESI-RUN Transport Management System**, developers should ensure they have a **Java Development Kit (JDK)** of version 11 or higher installed, along with the **JavaFX SDK**. After cloning the repository from [GitHub](https://github.com/ounnane-abdelhak/projet-mini-app-transport), the project can be imported into any standard Integrated Development Environment (IDE). It is crucial to correctly configure the JavaFX library paths within the IDE settings before launching the `MainApp.java` class, which serves as the primary entry point for the entire application.

> **Note**: This project was developed as a mini-application for educational purposes, demonstrating the practical application of object-oriented programming and GUI design in Java.

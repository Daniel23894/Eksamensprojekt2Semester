# Eksamensprojekt2Semester

This tool is designed for project managers and scrum masters to assist in breaking down projects into smaller, manageable tasks while predicting the time required, assigning responsibilities, and understanding how everything is interconnected. It aims to simplify the process of project planning, improve time management, and potentially enhance resource allocation.
   
## How to Try Out the Project Locally
1. **Clone the repository:**
Clone this repository from GitHub and open it in your preferred Java IDE (e.g., IntelliJ IDEA or Eclipse).

2. **Build and run the project:**
Make sure you have Java 21+ and Maven installed. Then build and run the project using your IDE’s run configuration or terminal commands.

3. **Access the application:**
Once the application is running, open your browser and go to:
[http://localhost:8080/login](http://localhost:8080/login). You’ll now see the Thymeleaf-powered web interface.

## Technical Overview
This project is built with **Java** and **Spring Boot**, following a standard layered architecture:

-- **Backend**  
  - `Model` classes representing entities and data structures.  
  - `Repository` using Spring Data JDBC for database access.  
  - **Service layer:**  
    - A `ProjectService` interface defines the contract for some services, implemented by service classes containing business logic.  
    - Other service classes are implemented directly without interfaces.  
  - **DTOs (Data Transfer Objects):** `ProjectDTO`, `SubprojectDTO`, and `ResponseDTO` transfer data between layers and to/from clients, decoupling internal models from external representations.  
  - **Custom exception handling:** application-specific exceptions signal business errors clearly.  
  - **Global error handling:** a `@ControllerAdvice` class manages exceptions centrally, providing consistent error responses and custom error pages.  
  - **Custom converters:** e.g., `StringToStateStatusConverter` converts string request parameters to enums, easing controller input binding.

-- **Frontend**  
  - HTML files using **Thymeleaf** for dynamic content rendering.  
  - Static **CSS** files for styling.  
  - Minimal JavaScript (e.g., `confirm()` dialogs) for user interaction enhancements such as delete confirmations.

- **Project Start Page**  
  - The application is deployed on Azure and accessible at [https://calculation-tool-engve3hmfyh7afhf.francecentral-01.azurewebsites.net/login](https://calculation-tool-engve3hmfyh7afhf.francecentral-01.azurewebsites.net/login).

- **Database**  
  - Connected to a MySQL database hosted on Azure (`the-2nd-term-exam.mysql.database.azure.com`).  
  - Database credentials (`MYSQL_USERNAME`, `MYSQL_PASSWORD`) are securely injected via environment variables to avoid hardcoding secrets.  
  - SQL scripts (`schema.sql`) are included for initial schema setup.  
  - The project uses Spring Data JDBC for database access and is configured with the official MySQL JDBC driver.


-- **CI/CD**  
  - Basic pipeline setup for continuous integration and automated testing (`mvn clean install`) and deployment.

  

## Support  
If you encounter any issues with the project or would like to provide suggestions, feel free to contact me via email: [daso0005@stud.kea.dk](mailto:daso0005@stud.kea.dk).


## Contributing
If you'd like to contribute to the project, please refer to the [CONTRIBUTING.md](CONTRIBUTING.md) file for guidelines.


## License 
See the [LICENSE](LICENSE.md) file.

## Project Status  
Just getting started ;) 



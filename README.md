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
This project is built with **Java** and **Spring Boot**, following a standard layered architecture **based on the MVC (Model-View-Controller) pattern**.

Project follows a standard layered architecture:  

--  **Handling external libraries**
   - Maven ensuring smooth integration of external libraries.

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

-- **Database**  
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
📝 Status
The application is in a solid working state and includes essential core features. Users can log in with different roles (admin and team members), each with access to specific functionality. Admins can manage users and view all projects, while team members have limited permissions. This role-based access is handled securely and provides a good foundation for further development.

Full CRUD operations are implemented for projects, including the ability to create, edit, delete, and view them. The project overview includes search and filter options, making it easier to navigate larger data sets. The frontend uses consistent design patterns with reusable CSS components to ensure a clean and uniform user experience.

On the backend, the project follows a layered architecture. Business logic is separated into service methods, including calculations like project completion percentage. We’ve used DTOs to control which data is exposed to the user, and custom exceptions for clearer error handling. The repository layer uses JDBC with dependency injection, including RowMappers for clean and reusable database interactions.

CI/CD is configured through GitHub Actions. On each push to the main branch, the application is built, unit tests are run, and – if successful – the app is deployed automatically to Azure. Environment variables and connection strings are handled via the Azure App Service configuration and secrets. The database is set up using schema.sql and data.sql files, which define structure and default data.



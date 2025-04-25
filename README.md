# Eksamensprojekt2Semester

This tool is designed for project managers and scrum masters to assist in breaking down projects into smaller, manageable tasks while predicting the time required, assigning responsibilities, and understanding how everything is interconnected. It aims to simplify the process of project planning, improve time management, and potentially enhance resource allocation.
   
## How to Try Out the Project Locally
1. **Clone the repository:**
Clone this repository from GitHub and open it in your preferred Java IDE (e.g., IntelliJ IDEA or Eclipse).

2. **Build and run the project:**
Make sure you have Java 21+ and Maven installed. Then build and run the project using your IDE’s run configuration or terminal commands.

3. **Access the application:**
Once the application is running, open your browser and go to:
http://localhost:8080. You’ll now see the Thymeleaf-powered web interface.

## Technical Overview  
This project is built with **Java** and **Spring Boot**, following a standard layered architecture:  
- **Backend**
  - `Model` classes for representing entities and data structures.
  - `Repository` interfaces using Spring Data JPA for database access.
  - `Service` classes to contain business logic.
  - `Controller` classes to handle HTTP requests and connect to frontend.

- **Frontend**
  - HTML files with **Thymeleaf** annotations for dynamic content rendering.
  - Static **CSS** files for styling.

- **Database**
  - SQL scripts included for schema setup and test data.
  - Configured connection to a relational database (e.g., PostgreSQL/MySQL).

- **CI/CD**
  - Basic pipeline setup for continuous integration
  

## Support  
If you encounter any issues with the project or would like to provide suggestions, feel free to contact me via email: [daso0005@stud.kea.dk](mailto:daso0005@stud.kea.dk).


## Contributing
If you'd like to contribute to the project, please refer to the [CONTRIBUTING.md](CONTRIBUTING.md) file for guidelines.


## License 
See the [LICENSE](LICENSE.md) file.

## Project Status  
Just getting started ;) 



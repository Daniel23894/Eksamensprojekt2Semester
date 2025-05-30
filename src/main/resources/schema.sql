CREATE DATABASE IF NOT EXISTS alphasolutions;
USE alphasolutions;

CREATE TABLE stateStatus (
                             statusId INTEGER PRIMARY KEY,
                             statusName VARCHAR(255) NOT NULL
);

CREATE TABLE role (
                      roleId INTEGER PRIMARY KEY,
                      roleName VARCHAR(100) NOT NULL
);

CREATE TABLE project (
                         projectId INTEGER PRIMARY KEY AUTO_INCREMENT,
                         projectName VARCHAR(255) NOT NULL,
                         description VARCHAR(255),
                         startDate DATE,
                         endDate DATE,
                         actualStartDate DATE,
                         actualEndDate DATE,
                         budget DECIMAL(19, 0),
                         completionPercentage INTEGER,
                         statusId INTEGER NOT NULL, -- Project must have a status
                         FOREIGN KEY (statusId) REFERENCES stateStatus(statusId)
);


CREATE TABLE teamMember (
                            memberId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,     -- unique for login
                            password VARCHAR(255) NOT NULL,         -- store hashed password
                            role INTEGER,                           -- e.g., 'admin', 'user', 'project_manager'
                            hoursPerDay DECIMAL(19, 0),
                            projectId INTEGER NULL, -- NULL to allow projectId  to be empty or unassigned
                            FOREIGN KEY (role) REFERENCES role(roleId),
                            FOREIGN KEY (projectId) REFERENCES project(projectId) ON DELETE SET NULL -- ON DELETE SET NULL to avoid errors and keep team members existing if the project is deleted.
);


CREATE TABLE subProject (
                            subProjectId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            subProjectName VARCHAR(255) NOT NULL,
                            completionPercentage INTEGER NOT NULL,
                            statusId INTEGER,
                            projectId INTEGER,
                            FOREIGN KEY (statusId) REFERENCES stateStatus(statusId),
                            FOREIGN KEY (projectId) REFERENCES project(projectId) ON DELETE CASCADE
);

CREATE TABLE task (
                      taskId INTEGER PRIMARY KEY AUTO_INCREMENT,
                      taskName VARCHAR(255) NOT NULL,
                      deadline DATE,
                      estimatedHours DECIMAL(19, 0),
                      usedHours DECIMAL(19, 0),
                      completionPercentage INTEGER,
                      statusId INTEGER,
                      subProjectId INTEGER,
                      FOREIGN KEY (statusId) REFERENCES stateStatus(statusId),
                      FOREIGN KEY (subProjectId) REFERENCES subProject(subProjectId) ON DELETE CASCADE
);

CREATE TABLE subTask (
                         subTaskId INTEGER PRIMARY KEY AUTO_INCREMENT,
                         subTaskName VARCHAR(255) NOT NULL,
                         deadline DATE,
                         estimatedHours DECIMAL(19, 0),
                         usedHours DECIMAL(19, 0),
                         completionPercentage INTEGER,
                         statusId INTEGER,
                         taskId INTEGER,
                         FOREIGN KEY (statusId) REFERENCES stateStatus(statusId),
                         FOREIGN KEY (taskId) REFERENCES task(taskId) ON DELETE CASCADE
);

CREATE TABLE taskAssignment (
                                taskId INTEGER,
                                memberId INTEGER,
                                assignedHours DECIMAL(19, 0),
                                actualHours DECIMAL(19, 0),
                                PRIMARY KEY (taskId, memberId),
                                FOREIGN KEY (taskId) REFERENCES task(taskId) ON DELETE CASCADE,
                                FOREIGN KEY (memberId) REFERENCES teamMember(memberId) ON DELETE CASCADE
);

CREATE TABLE taskDependency (
                                sourceTaskId INTEGER,
                                targetTaskId INTEGER,
                                dependencyType INTEGER,
                                PRIMARY KEY (sourceTaskId, targetTaskId),
                                FOREIGN KEY (sourceTaskId) REFERENCES task(taskId) ON DELETE CASCADE,
                                FOREIGN KEY (targetTaskId) REFERENCES task(taskId) ON DELETE CASCADE
);

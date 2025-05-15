CREATE DATABASE IF NOT EXISTS alphasolutions;
USE alphasolutions;

CREATE TABLE stateStatus (
                             statusId INTEGER PRIMARY KEY AUTO_INCREMENT,
                             statusName VARCHAR(255) NOT NULL
);

CREATE TABLE teamMember (
                            memberId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,     -- unique for login
                            password VARCHAR(255) NOT NULL,          -- store hashed password
                            role INTEGER(255),                      -- e.g., 'admin', 'user', 'project_manager'
                            hoursPerDay DECIMAL(19, 0)
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
                         statusId INTEGER,
                         FOREIGN KEY (statusId) REFERENCES stateStatus(statusId)
);

CREATE TABLE subProject (
                            subProjectId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            subProjectName VARCHAR(255) NOT NULL,
                            completionPercentage INTEGER NOT NULL,
                            statusId INTEGER,
                            projectId INTEGER,
                            FOREIGN KEY (statusId) REFERENCES stateStatus(statusId),
                            FOREIGN KEY (projectId) REFERENCES project(projectId)
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
                      FOREIGN KEY (subProjectId) REFERENCES subProject(subProjectId)
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
                         FOREIGN KEY (taskId) REFERENCES task(taskId)
);

CREATE TABLE taskAssignment (
                                taskId INTEGER,
                                memberId INTEGER,
                                assignedHours DECIMAL(19, 0),
                                actualHours DECIMAL(19, 0),
                                PRIMARY KEY (taskId, memberId),
                                FOREIGN KEY (taskId) REFERENCES task(taskId),
                                FOREIGN KEY (memberId) REFERENCES teamMember(memberId)
);

CREATE TABLE taskDependency (
                                sourceTaskId INTEGER,
                                targetTaskId INTEGER,
                                dependencyType INTEGER,
                                PRIMARY KEY (sourceTaskId, targetTaskId),
                                FOREIGN KEY (sourceTaskId) REFERENCES task(taskId),
                                FOREIGN KEY (targetTaskId) REFERENCES task(taskId)
);

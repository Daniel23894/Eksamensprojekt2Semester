-- Opret database
CREATE DATABASE IF NOT EXISTS alphasolutions;
USE alphasolutions;

-- Opret StateStatus table
CREATE TABLE StateStatus (
                             statusId INTEGER PRIMARY KEY AUTO_INCREMENT,
                             statusName VARCHAR(255) NOT NULL
);

-- Opret TeamMember table
CREATE TABLE TeamMember (
                            memberId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL,
                            role VARCHAR(255),
                            hoursPerDay DECIMAL(19, 0)
);

-- Opret Role table
CREATE TABLE Role (
                      roleId INTEGER PRIMARY KEY,
                      roleName VARCHAR(100) NOT NULL
);

-- Opret Project table
CREATE TABLE Project (
                         projectId INTEGER PRIMARY KEY AUTO_INCREMENT,
                         projectName VARCHAR(255) NOT NULL,
                         startDate DATE,
                         endDate DATE,
                         actualStartDate DATE,
                         actualEndDate DATE,
                         budget DECIMAL(19, 0),
                         completionPercentage INTEGER,
                         statusId INTEGER,
                         FOREIGN KEY (statusId) REFERENCES StateStatus(statusId)
);

-- Opret Subproject table
CREATE TABLE Subproject (
                            subprojectId INTEGER PRIMARY KEY AUTO_INCREMENT,
                            subprojectName VARCHAR(255) NOT NULL,
                            completionPercentage INTEGER NOT NULL,
                            statusId INTEGER,
                            projectId INTEGER,
                            FOREIGN KEY (statusId) REFERENCES StateStatus(statusId),
                            FOREIGN KEY (projectId) REFERENCES Project(projectId)
);

-- Opret Task table
CREATE TABLE Task (
                      taskId INTEGER PRIMARY KEY AUTO_INCREMENT,
                      taskName VARCHAR(255) NOT NULL,
                      deadline DATE,
                      estimatedHours DECIMAL(19, 0),
                      usedHours DECIMAL(19, 0),
                      completionPercentage INTEGER,
                      statusId INTEGER,
                      subprojectId INTEGER,
                      FOREIGN KEY (statusId) REFERENCES StateStatus(statusId),
                      FOREIGN KEY (subprojectId) REFERENCES Subproject(subprojectId)
);

-- Opret Subtask table
CREATE TABLE Subtask (
                         subtaskId INTEGER PRIMARY KEY AUTO_INCREMENT,
                         subtaskName INTEGER NOT NULL,
                         deadline DATE,
                         estimatedHours DECIMAL(19, 0),
                         usedHours DECIMAL(19, 0),
                         completionPercentage INTEGER,
                         statusId INTEGER,
                         taskId INTEGER,
                         FOREIGN KEY (statusId) REFERENCES StateStatus(statusId),
                         FOREIGN KEY (taskId) REFERENCES Task(taskId)
);

-- Opret TaskAssignment table
CREATE TABLE TaskAssignment (
                                taskId INTEGER,
                                memberId INTEGER,
                                assignedHours DECIMAL(19, 0),
                                actualHours DECIMAL(19, 0),
                                PRIMARY KEY (taskId, memberId),
                                FOREIGN KEY (taskId) REFERENCES Task(taskId),
                                FOREIGN KEY (memberId) REFERENCES TeamMember(memberId)
);

-- Opret TaskDependency table
CREATE TABLE TaskDependency (
                                sourceTaskId INTEGER,
                                targetTaskId INTEGER,
                                dependencyType INTEGER,
                                PRIMARY KEY (sourceTaskId, targetTaskId),
                                FOREIGN KEY (sourceTaskId) REFERENCES Task(taskId),
                                FOREIGN KEY (targetTaskId) REFERENCES Task(taskId)
);

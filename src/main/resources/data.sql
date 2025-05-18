-- Indsæt i Role
INSERT INTO Role (roleId, roleName) VALUES
(0, 'Administrator'),
(1, 'Developer'),
(2, 'Product Owner');

-- Indsæt i StateStatus
INSERT INTO StateStatus (statusName) VALUES
('Ikke startet'),
('I gang'),
('Afsluttet'),
('Pauset'),
('Annulleret');

-- Indsæt i TeamMember
INSERT INTO TeamMember (name, email, password, role, hoursPerDay) VALUES
('Alice Hansen', 'alice@example.com', 'someHashedPassword1', 0, 8),
('Bob Madsen', 'bob@example.com', 'someHashedPassword2', 1, 7),
('Clara Nielsen', 'clara@example.com', 'someHashedPassword3', 2, 6),
('System Admin', 'admin@alphasolutions.dk', 'admin123', 0, 8);

-- Indsæt i Project
INSERT INTO Project (projectName, startDate, endDate, actualStartDate, actualEndDate, budget, completionPercentage, statusId) VALUES
('Alpha Platform', '2025-01-01', '2025-06-30', '2025-01-02', NULL, 500000, 40, 2);

-- Indsæt i Subproject
INSERT INTO Subproject (subprojectName, completionPercentage, statusId, projectId) VALUES
('Frontend', 50, 2, 1),
('Backend', 30, 2, 1);

-- Indsæt i Task
INSERT INTO Task (taskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, subprojectId) VALUES
('Design UI', '2025-03-01', 120, 80, 70, 2, 1),
('API Development', '2025-04-01', 150, 60, 40, 2, 2);

-- Indsæt i Subtask
INSERT INTO Subtask (subtaskName, deadline, estimatedHours, usedHours, completionPercentage, statusId, taskId) VALUES
(1, '2025-02-01', 40, 30, 75, 2, 1),
(2, '2025-03-15', 50, 20, 40, 1, 2);

-- Indsæt i TaskAssignment
INSERT INTO TaskAssignment (taskId, memberId, assignedHours, actualHours) VALUES
(1, 2, 60, 45),
(2, 3, 90, 30);

-- Indsæt i TaskDependency
INSERT INTO TaskDependency (sourceTaskId, targetTaskId, dependencyType) VALUES
(1, 2, 1);

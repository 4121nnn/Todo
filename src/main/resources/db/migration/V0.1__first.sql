CREATE SCHEMA IF NOT EXISTS todo;

CREATE TABLE IF NOT EXISTS todo.tasks (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP NOT NULL,
    completed_date TIMESTAMP
);
---- Insert initial tasks
--INSERT INTO todo.tasks (task, completed, created_date, completed_date) VALUES
--('Task 1', FALSE, NOW(), NULL),
--('Task 2', FALSE, NOW(), NULL),
--('Task 3', TRUE, NOW(), NOW());
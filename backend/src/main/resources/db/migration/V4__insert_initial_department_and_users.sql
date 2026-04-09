-- Insert department (without user FK manager)
INSERT INTO department (name)
VALUES ('ARCHITECTURE')
    ON CONFLICT (name) DO NOTHING;

-- Insert users (bcrypt hashes required)
WITH dept AS (
    SELECT id FROM department WHERE name = 'ARCHITECTURE'
)
INSERT INTO asset_user (
    username,
    password,
    name,
    surname,
    email,
    status,
    department_id,
    role,
    notes,
    benefit,
    manager_email
)
SELECT
    v.username,
    v.password,
    v.name,
    v.surname,
    v.email,
    v.status,
    d.id,
    v.role,
    v.notes,
    v.benefit,
    v.manager_email
FROM (
         VALUES
             (
                 'user_admin',
                 '$2a$12$9qse.vAVHdnMYkiWeK156.pWo3LCkZjVK6pfBXG4z0Rm3tiD5NVHu', -- bcrypt of "admin123"
                 'John',
                 'Doe',
                 'john.doe@example.com',
                 'ACTIVE',
                 'ADMIN',
                 'This is a dummy admin user',
                 'ALL',
                 'manager.doe@example.com'
             ),
             (
                 'user_employee',
                 '$2a$12$GuQwFZyOKSOttzf.hsqnEuDRbX2fB7XudiQsmOqWwYKLzzDrQO9Uq', -- bcrypt of "employee123"
                 'Jane',
                 'Smith',
                 'jane.smith@example.com',
                 'ACTIVE',
                 'EMPLOYEE',
                 'This is a dummy employee user',
                 'ALL',
                 'manager.doe@example.com'
             ),
             (
                 'user_manager',
                 '$2a$12$SlU1fXn97HS1ozbdV8mNy.UBbG.bK3fpEigx2//27.4eFPD3bWCNy', -- bcrypt of "manager123"
                 'Mark',
                 'Jones',
                 'mark.jones@example.com',
                 'ACTIVE',
                 'MANAGER',
                 'This is a dummy manager user',
                 'ALL',
                 'manager.3@example.com'
             )
     ) AS v(
            username,
            password,
            name,
            surname,
            email,
            status,
            role,
            notes,
            benefit,
            manager_email
    )
         CROSS JOIN dept d
    ON CONFLICT (username) DO NOTHING;

-- Assign manager (after users exist)
UPDATE department d
SET manager_id = u.id
    FROM asset_user u
WHERE d.name = 'ARCHITECTURE'
  AND u.username = 'user_manager'
  AND d.manager_id IS NULL;
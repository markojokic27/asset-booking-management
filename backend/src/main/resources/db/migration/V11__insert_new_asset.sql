INSERT INTO asset (
    name,
    category_id,
    status,
    description,
    code,
    location
)
SELECT
    v.name,
    ac.id,
    v.status,
    v.description,
    v.code,
    v.location
FROM (
         VALUES
             (
                 'MacBook Pro 16',
                 'Laptop',
                 'ACTIVE',
                 'Apple laptop for developers',
                 'LAP-001',
                 'Office 3'
             ),
             (
                 'Dell XPS 13',
                 'Laptop',
                 'INACTIVE',
                 'Ultrabook for staff',
                 'LAP-002',
                 'Office 7'
             ),
             (
                 'Lenovo ThinkPad',
                 'Laptop',
                 'ACTIVE',
                 'Business laptop',
                 'LAP-003',
                 'Office 2'
             ),

             (
                 'Parking Spot 5',
                 'Parking',
                 'ACTIVE',
                 'Underground parking',
                 'PARK-001',
                 'Floor plan 0'
             ),
             (
                 'Parking Spot 10',
                 'Parking',
                 'DAMAGED',
                 'Reserved parking',
                 'PARK-002',
                 'Floor plan 1'
             ),
             (
                 'Parking Spot 22',
                 'Parking',
                 'ACTIVE',
                 'Outdoor parking',
                 'PARK-003',
                 'Floor plan 2'
             ),

             (
                 'Clean Code',
                 'Book',
                 'ACTIVE',
                 'Programming book',
                 'BOOK-001',
                 'Library'
             ),
             (
                 'Design Patterns',
                 'Book',
                 'INACTIVE',
                 'Software design book',
                 'BOOK-002',
                 'Library'
             ),
             (
                 'Refactoring',
                 'Book',
                 'ACTIVE',
                 'Code improvement book',
                 'BOOK-003',
                 'Library'
             ),

             (
                 'Desk A1',
                 'Desk',
                 'ACTIVE',
                 'Standing desk',
                 'DESK-001',
                 'Floor 1'
             ),
             (
                 'Desk A2',
                 'Desk',
                 'INACTIVE',
                 'Standard desk',
                 'DESK-002',
                 'Floor 1'
             ),
             (
                 'Desk B1',
                 'Desk',
                 'ACTIVE',
                 'Corner desk',
                 'DESK-003',
                 'Floor 2'
             ),

             (
                 'Meeting Room 12',
                 'Meeting room',
                 'ACTIVE',
                 'Small meeting room',
                 'MR-001',
                 'Floor 2'
             ),
             (
                 'Meeting Room 18',
                 'Meeting room',
                 'INACTIVE',
                 'Medium meeting room',
                 'MR-002',
                 'Floor 2'
             ),
             (
                 'Meeting Room 26',
                 'Meeting room',
                 'ACTIVE',
                 'Large meeting room',
                 'MR-003',
                 'Floor 3'
             ),

             (
                 'Projector Epson',
                 'IT equipment',
                 'ACTIVE',
                 'HD projector',
                 'IT-001',
                 'Room 7'
             ),
             (
                 'Switch Cisco 24-port',
                 'IT equipment',
                 'INACTIVE',
                 'Network switch',
                 'IT-002',
                 'Server room'
             ),
             (
                 'Router Mikrotik',
                 'IT equipment',
                 'ACTIVE',
                 'Office router',
                 'IT-003',
                 'Server room'
             ),

             (
                 'MacBook Air M2',
                 'Laptop',
                 'ACTIVE',
                 'Lightweight laptop',
                 'LAP-004',
                 'Office 10'
             ),
             (
                 'Parking Spot 17',
                 'Parking',
                 'DELETED',
                 'VIP parking',
                 'PARK-004',
                 'Floor plan 2'
             )
     ) AS v(
            name,
            category_name,
            status,
            description,
            code,
            location
    )
         JOIN asset_category ac
              ON ac.name = v.category_name
    ON CONFLICT (code) DO NOTHING;

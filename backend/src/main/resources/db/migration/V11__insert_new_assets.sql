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
    v.category_id,
    v.status,
    v.description,
    v.code,
    v.location
FROM (
         VALUES
             (
                 'MacBook Pro 16',
                 14,
                 'ACTIVE',
                 'Apple laptop for developers',
                 'LAP-001',
                 'Office 3'
             ),
             (
                 'Dell XPS 13',
                 14,
                 'INACTIVE',
                 'Ultrabook for staff',
                 'LAP-002',
                 'Office 7'
             ),
             (
                 'Lenovo ThinkPad',
                 14,
                 'ACTIVE',
                 'Business laptop',
                 'LAP-003',
                 'Office 2'
             ),

             (
                 'Parking Spot 5',
                 15,
                 'ACTIVE',
                 'Underground parking',
                 'PARK-001',
                 'Floor plan 0'
             ),
             (
                 'Parking Spot 10',
                 15,
                 'DAMAGED',
                 'Reserved parking',
                 'PARK-002',
                 'Floor plan 1'
             ),
             (
                 'Parking Spot 22',
                 15,
                 'ACTIVE',
                 'Outdoor parking',
                 'PARK-003',
                 'Floor plan 2'
             ),

             (
                 'Clean Code',
                 16,
                 'ACTIVE',
                 'Programming book',
                 'BOOK-001',
                 'Library'
             ),
             (
                 'Design Patterns',
                 16,
                 'INACTIVE',
                 'Software design book',
                 'BOOK-002',
                 'Library'
             ),
             (
                 'Refactoring',
                 16,
                 'ACTIVE',
                 'Code improvement book',
                 'BOOK-003',
                 'Library'
             ),

             (
                 'Desk A1',
                 17,
                 'ACTIVE',
                 'Standing desk',
                 'DESK-001',
                 'Floor 1'
             ),
             (
                 'Desk A2',
                 17,
                 'INACTIVE',
                 'Standard desk',
                 'DESK-002',
                 'Floor 1'
             ),
             (
                 'Desk B1',
                 17,
                 'ACTIVE',
                 'Corner desk',
                 'DESK-003',
                 'Floor 2'
             ),

             (
                 'Meeting Room 12',
                 18,
                 'ACTIVE',
                 'Small meeting room',
                 'MR-001',
                 'Floor 2'
             ),
             (
                 'Meeting Room 18',
                 18,
                 'INACTIVE',
                 'Medium meeting room',
                 'MR-002',
                 'Floor 2'
             ),
             (
                 'Meeting Room 26',
                 18,
                 'ACTIVE',
                 'Large meeting room',
                 'MR-003',
                 'Floor 3'
             ),

             (
                 'Projector Epson',
                 19,
                 'ACTIVE',
                 'HD projector',
                 'IT-001',
                 'Room 7'
             ),
             (
                 'Switch Cisco 24-port',
                 19,
                 'INACTIVE',
                 'Network switch',
                 'IT-002',
                 'Server room'
             ),
             (
                 'Router Mikrotik',
                 19,
                 'ACTIVE',
                 'Office router',
                 'IT-003',
                 'Server room'
             ),

             (
                 'MacBook Air M2',
                 14,
                 'ACTIVE',
                 'Lightweight laptop',
                 'LAP-004',
                 'Office 10'
             ),
             (
                 'Parking Spot 17',
                 15,
                 'DELETED',
                 'VIP parking',
                 'PARK-004',
                 'Floor plan 2'
             )
     ) AS v(
            name,
            category_id,
            status,
            description,
            code,
            location
    )
    ON CONFLICT (code) DO NOTHING;
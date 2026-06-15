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
        ('Parking Spot 66',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-066', 'Level -1'),
        ('Parking Spot 67',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-067', 'Level -1'),
        ('Parking Spot 68',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-068', 'Level -1'),
        ('Parking Spot 108', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-108', 'Level -1'),
        ('Parking Spot 109', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-109', 'Level -1'),
        ('Parking Spot 112', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-112', 'Level -1'),
        ('Parking Spot 113', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-113', 'Level -1'),
        ('Parking Spot 114', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-114', 'Level -1'),
        ('Parking Spot 117', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-117', 'Level -1'),
        ('Parking Spot 118', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-118', 'Level -1'),
        ('Parking Spot 119', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-119', 'Level -1'),
        ('Parking Spot 120', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-120', 'Level -1'),
        ('Parking Spot 121', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-121', 'Level -1'),
        ('Parking Spot 122', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-122', 'Level -1'),
        ('Parking Spot 123', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-123', 'Level -1'),
        ('Parking Spot 124', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-124', 'Level -1'),
        ('Parking Spot 125', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-125', 'Level -1'),
        ('Parking Spot 126', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-126', 'Level -1'),
        ('Parking Spot 127', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-127', 'Level -1'),
        ('Parking Spot 128', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-128', 'Level -1'),
        ('Parking Spot 1',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-001', 'Level -2'),
        ('Parking Spot 2',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-002', 'Level -2'),
        ('Parking Spot 3',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-003', 'Level -2'),
        ('Parking Spot 4',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-004', 'Level -2'),
        ('Parking Spot 6',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-006', 'Level -2'),
        ('Parking Spot 7',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-007', 'Level -2'),
        ('Parking Spot 8',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-008', 'Level -2'),
        ('Parking Spot 9',  'Parking', 'ACTIVE', 'Garage parking', 'PARK-009', 'Level -2'),
        ('Parking Spot 11', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-011', 'Level -2'),
        ('Parking Spot 12', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-012', 'Level -2'),
        ('Parking Spot 13', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-013', 'Level -2'),
        ('Parking Spot 14', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-014', 'Level -2'),
        ('Parking Spot 15', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-015', 'Level -2'),
        ('Parking Spot 16', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-016', 'Level -2'),
        ('Parking Spot 17', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-017', 'Level -2'),
        ('Parking Spot 19', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-019', 'Level -2'),
        ('Parking Spot 20', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-020', 'Level -2'),
        ('Parking Spot 21', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-021', 'Level -2'),
        ('Parking Spot 48', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-048', 'Level -2'),
        ('Parking Spot 49', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-049', 'Level -2'),
        ('Parking Spot 53', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-053', 'Level -2'),
        ('Parking Spot 54', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-054', 'Level -2'),
        ('Parking Spot 55', 'Parking', 'ACTIVE', 'Garage parking', 'PARK-055', 'Level -2')
    
) AS v(name, category_name, status, description, code, location)
JOIN asset_category ac ON ac.name = v.category_name
ON CONFLICT (code) DO NOTHING;


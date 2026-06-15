UPDATE asset
SET 
    location = 'Level -2',
    description = 'Garage parking'
WHERE code IN ('PARK-001', 'PARK-002', 'PARK-004');
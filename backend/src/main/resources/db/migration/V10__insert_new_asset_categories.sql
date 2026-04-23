INSERT INTO asset_category (
    name,
    description,
    booking_period,
    approval
)
SELECT
    v.name,
    v.description,
    v.booking_period,
    v.approval
FROM (
         VALUES
             (
                 'Laptop',
                 'All company laptops',
                 'DAY',
                 false
             ),
             (
                 'Parking',
                 'All company parkings',
                 'MONTH',
                 false
             ),
             (
                 'Book',
                 'All company books',
                 'DAY',
                 false
             ),
             (
                 'Desk',
                 'All company desks',
                 'HOUR',
                 false
             ),
             (
                 'Meeting room',
                 'All company meeting rooms',
                 'HOUR',
                 false
             ),
             (
                 'IT equipment',
                 'All company IT equipments',
                 'MONTH',
                 false
             )
     ) AS v(
            name,
            description,
            booking_period,
            approval
    )
    ON CONFLICT (name) DO NOTHING;
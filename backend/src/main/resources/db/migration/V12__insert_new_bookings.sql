INSERT INTO booking (
    user_id,
    asset_id,
    booking_start,
    booking_end,
    status,
    notes
)
SELECT
    v.user_id,
    a.id,
    v.booking_start,
    v.booking_end,
    v.status,
    v.notes
FROM (
         VALUES
             (
                 1,
                 'MacBook Pro 16',
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-25 09:00:00+00'::timestamptz,
                 'APPROVED',
                 'MacBook Pro booking for dev work'
             ),
             (
                 2,
                 'Dell XPS 13',
                 '2026-04-24 13:00:00+00'::timestamptz,
                 '2026-04-25 13:00:00+00'::timestamptz,
                 'PENDING',
                 'Waiting approval for Dell XPS'
             ),
             (
                 3,
                 'Lenovo ThinkPad',
                 '2026-04-25 08:00:00+00'::timestamptz,
                 '2026-04-26 08:00:00+00'::timestamptz,
                 'CANCELLED',
                 'ThinkPad booking cancelled'
             ),
             (
                 1,
                 'Parking Spot 5',
                 '2026-04-24 10:00:00+00'::timestamptz,
                 '2026-05-24 10:00:00+00'::timestamptz,
                 'APPROVED',
                 'Parking approved'
             ),
             (
                 2,
                 'Parking Spot 10',
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-05-24 09:00:00+00'::timestamptz,
                 'REJECTED',
                 'Parking request rejected'
             ),
             (
                 3,
                 'Parking Spot 22',
                 '2026-04-24 11:00:00+00'::timestamptz,
                 '2026-05-24 11:00:00+00'::timestamptz,
                 'APPROVED',
                 'Outdoor parking use'
             ),
             (
                 1,
                 'Clean Code',
                 '2026-04-24 11:00:00+00'::timestamptz,
                 '2026-04-25 11:00:00+00'::timestamptz,
                 'COMPLETED',
                 'Book returned'
             ),
             (
                 2,
                 'Design Patterns',
                 '2026-04-24 14:00:00+00'::timestamptz,
                 '2026-04-25 14:00:00+00'::timestamptz,
                 'APPROVED',
                 'Design Patterns reading session'
             ),
             (
                 3,
                 'Refactoring',
                 '2026-04-24 16:00:00+00'::timestamptz,
                 '2026-04-25 16:00:00+00'::timestamptz,
                 'PENDING',
                 'Waiting approval for refactoring book'
             ),
             (
                 1,
                 'Desk A1',
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 'APPROVED',
                 'Desk A1 full day booking'
             ),
             (
                 2,
                 'Desk A2',
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 'CANCELLED',
                 'Desk cancelled'
             ),
             (
                 3,
                 'Desk B1',
                 '2026-04-24 13:00:00+00'::timestamptz,
                 '2026-04-24 14:00:00+00'::timestamptz,
                 'APPROVED',
                 'Desk approved booking'
             ),
             (
                 1,
                 'Meeting Room 12',
                 '2026-04-25 09:00:00+00'::timestamptz,
                 '2026-04-25 10:00:00+00'::timestamptz,
                 'APPROVED',
                 'Meeting Room booking'
             ),
             (
                 2,
                 'Meeting Room 18',
                 '2026-04-25 12:00:00+00'::timestamptz,
                 '2026-04-25 13:00:00+00'::timestamptz,
                 'REJECTED',
                 'Meeting room rejected'
             ),
             (
                 3,
                 'Meeting Room 26',
                 '2026-04-25 15:00:00+00'::timestamptz,
                 '2026-04-25 16:00:00+00'::timestamptz,
                 'APPROVED',
                 'Large meeting room approved'
             ),
             (
                 1,
                 'Projector Epson',
                 '2026-04-24 08:00:00+00'::timestamptz,
                 '2026-05-24 08:00:00+00'::timestamptz,
                 'APPROVED',
                 'Projector use'
             ),
             (
                 2,
                 'Switch Cisco 24-port',
                 '2026-04-24 10:00:00+00'::timestamptz,
                 '2026-05-24 10:00:00+00'::timestamptz,
                 'COMPLETED',
                 'Switch maintenance done'
             ),
             (
                 3,
                 'Router Mikrotik',
                 '2026-04-24 12:00:00+00'::timestamptz,
                 '2026-05-24 12:00:00+00'::timestamptz,
                 'APPROVED',
                 'Router setup'
             ),
             (
                 1,
                 'MacBook Air M2',
                 '2026-04-24 14:00:00+00'::timestamptz,
                 '2026-04-25 14:00:00+00'::timestamptz,
                 'APPROVED',
                 'MacBook Air approved'
             ),
             (
                 2,
                 'Parking Spot 17',
                 '2026-04-24 16:00:00+00'::timestamptz,
                 '2026-05-24 16:00:00+00'::timestamptz,
                 'PENDING',
                 'Parking removed'
             )
     ) AS v(
            user_id,
            asset_name,
            booking_start,
            booking_end,
            status,
            notes
    )
         JOIN asset a
              ON a.name = v.asset_name;
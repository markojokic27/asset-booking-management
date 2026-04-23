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
    v.asset_id,
    v.booking_start,
    v.booking_end,
    v.status,
    v.notes
FROM (
         VALUES
             (
                 1,
                 39,
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-25 09:00:00+00'::timestamptz,
                 'ACTIVE',
                 'MacBook Pro booking for dev work'
             ),
             (
                 2,
                 40,
                 '2026-04-24 13:00:00+00'::timestamptz,
                 '2026-04-25 13:00:00+00'::timestamptz,
                 'PENDING',
                 'Waiting approval for Dell XPS'
             ),
             (
                 3,
                 41,
                 '2026-04-25 08:00:00+00'::timestamptz,
                 '2026-04-26 08:00:00+00'::timestamptz,
                 'CANCELLED',
                 'ThinkPad booking cancelled'
             ),
             (
                 1,
                 42,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 '2026-05-24 10:00:00+00'::timestamptz,
                 'APPROVED',
                 'Parking approved'
             ),
             (
                 2,
                 43,
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-05-24 09:00:00+00'::timestamptz,
                 'REJECTED',
                 'Parking request rejected'
             ),
             (
                 3,
                 44,
                 '2026-04-24 11:00:00+00'::timestamptz,
                 '2026-05-24 11:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Outdoor parking use'
             ),
             (
                 1,
                 45,
                 '2026-04-24 11:00:00+00'::timestamptz,
                 '2026-04-25 11:00:00+00'::timestamptz,
                 'COMPLETED',
                 'Book returned'
             ),
             (
                 2,
                 46,
                 '2026-04-24 14:00:00+00'::timestamptz,
                 '2026-04-25 14:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Design Patterns reading session'
             ),
             (
                 3,
                 47,
                 '2026-04-24 16:00:00+00'::timestamptz,
                 '2026-04-25 16:00:00+00'::timestamptz,
                 'PENDING',
                 'Waiting approval for refactoring book'
             ),
             (
                 1,
                 48,
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Desk A1 full day booking'
             ),
             (
                 2,
                 49,
                 '2026-04-24 09:00:00+00'::timestamptz,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 'CANCELLED',
                 'Desk cancelled'
             ),
             (
                 3,
                 50,
                 '2026-04-24 13:00:00+00'::timestamptz,
                 '2026-04-24 14:00:00+00'::timestamptz,
                 'APPROVED',
                 'Desk approved booking'
             ),
             (
                 1,
                 51,
                 '2026-04-25 09:00:00+00'::timestamptz,
                 '2026-04-25 10:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Meeting Room booking'
             ),
             (
                 2,
                 52,
                 '2026-04-25 12:00:00+00'::timestamptz,
                 '2026-04-25 13:00:00+00'::timestamptz,
                 'REJECTED',
                 'Meeting room rejected'
             ),
             (
                 3,
                 53,
                 '2026-04-25 15:00:00+00'::timestamptz,
                 '2026-04-25 16:00:00+00'::timestamptz,
                 'APPROVED',
                 'Large meeting room approved'
             ),
             (
                 1,
                 54,
                 '2026-04-24 08:00:00+00'::timestamptz,
                 '2026-05-24 08:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Projector use'
             ),
             (
                 2,
                 55,
                 '2026-04-24 10:00:00+00'::timestamptz,
                 '2026-05-24 10:00:00+00'::timestamptz,
                 'COMPLETED',
                 'Switch maintenance done'
             ),
             (
                 3,
                 56,
                 '2026-04-24 12:00:00+00'::timestamptz,
                 '2026-05-24 12:00:00+00'::timestamptz,
                 'ACTIVE',
                 'Router setup'
             ),
             (
                 1,
                 57,
                 '2026-04-24 14:00:00+00'::timestamptz,
                 '2026-04-25 14:00:00+00'::timestamptz,
                 'APPROVED',
                 'MacBook Air approved'
             ),
             (
                 2,
                 58,
                 '2026-04-24 16:00:00+00'::timestamptz,
                 '2026-05-24 16:00:00+00'::timestamptz,
                 'DELETED',
                 'Parking removed'
             )
     ) AS v(
            user_id,
            asset_id,
            booking_start,
            booking_end,
            status,
            notes
    );
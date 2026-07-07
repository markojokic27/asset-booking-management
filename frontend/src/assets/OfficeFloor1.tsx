import * as React from 'react';
import { useTranslation } from 'react-i18next';

export const OfficeFloor1: React.FC = () => {
  const { t } = useTranslation();
  const [showLegend, setShowLegend] = React.useState(false);

  const office           = t('bookings.floorMap.rooms.office');
  const meetingRoom      = t('bookings.floorMap.rooms.meetingRoom');
  const meetingRoomShort = t('bookings.floorMap.rooms.meetingRoomShort');
  const kitchenWc        = t('bookings.floorMap.rooms.kitchenWc');
  const stairs           = t('bookings.floorMap.rooms.stairs');
  const corridor         = t('bookings.floorMap.rooms.corridor');
  const showLegendLabel  = t('bookings.officeMap.showLegend');
  const hideLegendLabel  = t('bookings.officeMap.hideLegend');

  const C = {
    office:       { fill: '#2563EB', stroke: '#1D4ED8', text: '#FFFFFF' },
    meeting:      { fill: '#6366F1', stroke: '#4F46E5', text: '#FFFFFF' },
    kitchen:      { fill: '#6d6d6d', stroke: '#4b4b4b', text: '#FFFFFF' },
    stairs:       { fill: '#949494', stroke: '#5f5f5f', text: '#FFFFFF' },
    corridor:     { fill: '#F3F4F6', stroke: '#D1D5DB', text: '#6B7280' },
    wall:         '#374151',
    legendBg:     '#F9FAFB',
    legendBorder: '#E5E7EB',
    legendText:   '#6B7280',
  } as const;

  const room = (
    label: string,
    color: { fill: string; stroke: string; text: string },
    x: number, y: number, w: number, h: number
  ) => (
    <g key={`${x}-${y}`}>
      <rect x={x} y={y} width={w} height={h} fill={color.fill} stroke={color.stroke} strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="12" fill={color.text} x={x + w / 2} y={y + h / 2 + 4} textAnchor="middle">{label}</text>
    </g>
  );

  return (
    <div className="relative">
      {/* Legend toggle */}
      <div className="mb-2 flex justify-end">
        <button
          onClick={() => setShowLegend((v) => !v)}
          className="rounded-lg border border-gray-200 bg-gray-50 px-3 py-1.5 text-xs font-medium text-gray-600 transition-colors hover:bg-gray-100"
        >
          {showLegend ? hideLegendLabel : showLegendLabel}
        </button>
      </div>

      {/* Legend panel */}
      {showLegend && (
        <div className="absolute right-0 top-10 z-10 rounded-xl border border-gray-200 bg-white p-4 shadow-lg">
          <p className="mb-3 text-xs font-semibold uppercase tracking-widest text-gray-400">Legenda</p>
          <div className="flex flex-col gap-2">
            {[
              { color: C.office.fill,   label: office },
              { color: C.meeting.fill,  label: meetingRoom },
              { color: C.kitchen.fill,  label: kitchenWc },
              { color: C.stairs.fill,   label: stairs },
              { color: C.corridor.fill, label: corridor },
            ].map(({ color, label }) => (
              <div key={label} className="flex items-center gap-2">
                <div className="h-3 w-3 flex-shrink-0 rounded" style={{ backgroundColor: color, border: '1px solid rgba(0,0,0,0.1)' }}/>
                <span className="text-xs text-gray-700">{label}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <svg width="100%" viewBox="0 0 680 560" role="img" aria-label={t('bookings.officeMap.title')}>
        <rect x="0" y="0" width="680" height="560" fill="#FFFFFF"/>

        {/* Outer walls */}
        <rect x="30" y="30" width="620" height="280" fill="none" stroke={C.wall} strokeWidth="2"/>
        <rect x="30" y="310" width="440" height="180" fill="none" stroke={C.wall} strokeWidth="2"/>

        {/* Left column: 3 offices top */}
        {room(office, C.office, 30, 30,  130, 93)}
        {room(office, C.office, 30, 123, 130, 93)}
        {room(office, C.office, 30, 216, 130, 94)}

        {/* Left column: 1 office bottom */}
        {room(office, C.office, 30, 310, 130, 180)}

        {/* Top row: 5 offices */}
        {room(office, C.office, 160, 30, 80,  70)}
        {room(office, C.office, 240, 30, 110, 70)}
        {room(office, C.office, 350, 30, 80,  70)}
        {room(office, C.office, 430, 30, 80,  70)}
        {room(office, C.office, 510, 30, 140, 70)}

        {/* Corridors */}
        <rect x="160" y="100" width="400" height="40" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0"/>
        <rect x="160" y="140" width="40"  height="170" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0"/>
        <rect x="530" y="100" width="40"  height="210" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0"/>

        {/* Central block */}
        {room(kitchenWc, C.kitchen, 200, 140, 250, 170)}

        {/* Large meeting room */}
        {room(meetingRoomShort, C.meeting, 450, 140, 80, 170)}

        {/* 2 small meeting rooms */}
        {room(meetingRoomShort, C.meeting, 570, 100, 80, 105)}
        {room(meetingRoomShort, C.meeting, 570, 205, 80, 105)}

        {/* Bottom corridor */}
        <rect x="160" y="310" width="310" height="40" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0"/>
        <rect x="160" y="350" width="310" height="60" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0"/>
        {/* Stairs */}
        <rect x="185" y="350" width="100" height="60" fill={C.stairs.fill} stroke={C.stairs.stroke} strokeWidth="1"/>
        <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.stairs.text} x="235" y="383" textAnchor="middle">{stairs}</text>
        {[362, 374, 386, 398].map((y) => (
          <line key={y} x1="160" y1={y} x2="310" y2={y} stroke="#FFFFFF" strokeWidth="0.5" opacity="0.4"/>
        ))}

        {/* Bottom offices */}
        {room(office,           C.office,  160, 410, 75,  80)}
        {room(meetingRoomShort, C.meeting, 235, 410, 75,  80)}
        {room(office,           C.office,  310, 350, 70,  140)}
        {room(office,           C.office,  380, 350, 55,  140)}
        {room(office,           C.office,  435, 310, 95,  180)}
      </svg>
    </div>
  );
};

export default OfficeFloor1;
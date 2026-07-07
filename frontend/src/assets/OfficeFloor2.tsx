import * as React from 'react';
import { useTranslation } from 'react-i18next';

export const OfficeFloor2: React.FC = () => {
  const { t } = useTranslation();
  const [showLegend, setShowLegend] = React.useState(false);

  const stairs          = t('bookings.floorMap.rooms.stairs');
  const corridor        = t('bookings.floorMap.rooms.corridor');
  const commonArea      = t('bookings.floorMap.rooms.commonArea');
  const showLegendLabel = t('bookings.officeMap.showLegend');
  const hideLegendLabel = t('bookings.officeMap.hideLegend');

  const C = {
    AT:      { fill: '#2563EB', stroke: '#1D4ED8', text: '#FFFFFF', sub: '#BFDBFE' },
    MR:      { fill: '#6366F1', stroke: '#4F46E5', text: '#FFFFFF', sub: '#C7D2FE' },
    MS:      { fill: '#8B5CF6', stroke: '#7C3AED', text: '#FFFFFF', sub: '#DDD6FE' },
    ST:      { fill: '#0EA5E9', stroke: '#0284C7', text: '#FFFFFF', sub: '#BAE6FD' },
    D:       { fill: '#1E40AF', stroke: '#1E3A8A', text: '#FFFFFF', sub: '#BFDBFE' },
    A:       { fill: '#7C3AED', stroke: '#6D28D9', text: '#FFFFFF', sub: '#DDD6FE' },
    stairs:  { fill: '#949494', stroke: '#5f5f5f', text: '#FFFFFF' },
    corridor:{ fill: '#F3F4F6', stroke: '#D1D5DB', text: '#6B7280' },
    common:  { fill: '#6d6d6d', stroke: '#4b4b4b', text: '#FFFFFF' },
    wall:    '#374151',
  } as const;

  const room = (
    num: string,
    dept: string,
    color: { fill: string; stroke: string; text: string; sub: string },
    x: number, y: number, w: number, h: number
  ) => (
    <g key={num}>
      <rect x={x} y={y} width={w} height={h} fill={color.fill} stroke={color.stroke} strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="11" fontWeight="600" fill={color.text} x={x+w/2} y={y+h/2-6} textAnchor="middle">{num}</text>
      <text fontFamily="system-ui,sans-serif" fontSize="9" fill={color.sub} x={x+w/2} y={y+h/2+8} textAnchor="middle">{dept}</text>
    </g>
  );

  const deptLabels = [
    { color: C.AT.fill,       label: `AT — ${t('bookings.floorMap.depts.AT')}` },
    { color: C.MR.fill,       label: `MR — ${t('bookings.floorMap.depts.MR')}` },
    { color: C.MS.fill,       label: `M&S — ${t('bookings.floorMap.depts.MS')}` },
    { color: C.ST.fill,       label: `ST — ${t('bookings.floorMap.depts.ST')}` },
    { color: C.D.fill,        label: `D — ${t('bookings.floorMap.depts.D')}` },
    { color: C.A.fill,        label: `A — ${t('bookings.floorMap.depts.A')}` },
    { color: C.stairs.fill,   label: stairs },
    { color: C.corridor.fill, label: corridor },
  ];

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
          <p className="mb-3 text-xs font-semibold uppercase tracking-widest text-gray-400">
            {t('bookings.officeMap.legend')}
          </p>
          <div className="flex flex-col gap-2">
            {deptLabels.map(({ color, label }) => (
              <div key={label} className="flex items-center gap-2">
                <div className="h-3 w-3 flex-shrink-0 rounded" style={{ backgroundColor: color, border: '1px solid rgba(0,0,0,0.1)' }}/>
                <span className="text-xs text-gray-700">{label}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <svg width="100%" viewBox="0 0 720 520" role="img" aria-label={t('bookings.officeMap.title')}>
        <rect x="0" y="0" width="720" height="520" fill="#FFFFFF"/>

        {/* Outer wall */}
        <rect x="20" y="20" width="680" height="480" fill="none" stroke={C.wall} strokeWidth="2"/>

        {/* Corridor background */}
        <rect x="150" y="120" width="410" height="280" fill={C.corridor.fill} stroke="none"/>

        {/* TOP ROW */}
        {room('210', 'AT',  C.AT, 20,  20, 150, 100)}
        {room('211', 'MR',  C.MR, 170, 20, 70,  100)}
        {room('212', 'M&S', C.MS, 240, 20, 70,  100)}
        {room('213', 'M&S', C.MS, 310, 20, 80,  100)}
        {room('214', 'M&S', C.MS, 390, 20, 80,  100)}
        {room('215', 'ST',  C.ST, 470, 20, 90,  100)}

        {/* RIGHT COLUMN */}
        {room('216', 'ST', C.ST, 560, 20,  140, 160)}
        {room('217', 'ST', C.ST, 560, 180, 140, 160)}
        {room('218', 'ST', C.ST, 560, 340, 140, 160)}

        {/* LEFT COLUMN middle */}
        {room('209', 'AT', C.AT, 20, 120, 130, 70)}
        {room('208', 'AT', C.AT, 20, 190, 130, 70)}
        {room('207', 'MR', C.MR, 20, 260, 130, 70)}
        {room('206', 'AT', C.AT, 20, 330, 130, 70)}

        {/* BOTTOM ROW */}
        {room('205', 'AT', C.AT, 20,  400, 150, 100)}
        {room('204', 'AT', C.AT, 170, 400, 70,  100)}
        {room('203', 'A',  C.A,  240, 400, 70,  100)}
        {room('202', 'D',  C.D,  310, 400, 80,  100)}
        {room('201', 'ST', C.ST, 390, 400, 80,  100)}
        {room('219', 'ST', C.ST, 470, 400, 90,  100)}

        {/* ISLAND: common area */}
        <rect x="210" y="165" width="200" height="170" fill={C.common.fill} stroke={C.common.stroke} strokeWidth="1"/>
        <text fontFamily="system-ui,sans-serif" fontSize="11" fontWeight="500" fill={C.common.text} x="310" y="242" textAnchor="middle">{commonArea}</text>

        {/* Stairs */}
        <rect x="410" y="165" width="90" height="170" fill={C.stairs.fill} stroke={C.stairs.stroke} strokeWidth="1"/>
        <text fontFamily="system-ui,sans-serif" fontSize="11" fontWeight="600" fill={C.stairs.text} x="455" y="255" textAnchor="middle">{stairs}</text>
        {[178,191,204,217,230,243,256,269,282,295,308,321].map((y) => (
          <line key={y} x1="410" y1={y} x2="500" y2={y} stroke="#FFFFFF" strokeWidth="0.8" opacity="0.4"/>
        ))}
      </svg>
    </div>
  );
};

export default OfficeFloor2;
import * as React from 'react';
import { useTranslation } from 'react-i18next';

type Props = {
  takenSpots?: number[];
  onSpotClick?: (spotNumber: number) => void;
};

const AVAILABLE = '#2563EB';
const TAKEN = '#F97316';
const TAKEN_DIVIDER = '#EA6800';
const AVAILABLE_DIVIDER = '#1D4ED8';
const NO_PARK_FILL = '#FFFFFF';
const NO_PARK_X = '#9CA3AF';

export const FloorMinus1: React.FC<Props> = ({ takenSpots = [], onSpotClick }) => {
  const { t } = useTranslation();

  const sc = (num: number) => takenSpots.includes(num) ? TAKEN : AVAILABLE;
  const divider = (num: number) => takenSpots.includes(num) ? TAKEN_DIVIDER : AVAILABLE_DIVIDER;
  const spotProps = (num: number) => ({
    onClick: () => onSpotClick?.(num),
    style: { cursor: onSpotClick ? 'pointer' : 'default' },
     'data-testid': `parking-spot-${num}`
  });

  return (
    <svg width="100%" height="100%" viewBox="0 0 680 740" preserveAspectRatio="xMidYMid meet" role="img" aria-label={t('bookings.parkingMap.title')}>
      <defs>
        <marker id="arr" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="5" markerHeight="5" orient="auto-start-reverse">
          <path d="M2 1L8 5L2 9" fill="none" stroke="context-stroke" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
        </marker>
      </defs>

      <rect x="0" y="0" width="680" height="740" fill="#F3F4F6"/>
      <rect x="18" y="18" width="644" height="704" rx="10" fill="#FFFFFF" stroke="#E5E7EB" strokeWidth="1"/>

      {/* Legend */}
      <rect x="230" y="24" width="100" height="22" rx="5" fill={AVAILABLE}/>
      <text fontFamily="system-ui,sans-serif" fontWeight="600" fontSize="11" fill="#FFFFFF" x="280" y="39" textAnchor="middle">{t('bookings.floorMap.legend.maurerSpot')}</text>
      <rect x="342" y="24" width="100" height="22" rx="5" fill={TAKEN}/>
      <text fontFamily="system-ui,sans-serif" fontWeight="600" fontSize="11" fill="#FFFFFF" x="392" y="39" textAnchor="middle">{t('bookings.floorMap.legend.taken')}</text>
      <rect x="454" y="24" width="22" height="22" rx="5" fill={NO_PARK_FILL} stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="458" y1="28" x2="472" y2="42" stroke={NO_PARK_X} strokeWidth="1.8"/>
      <line x1="472" y1="28" x2="458" y2="42" stroke={NO_PARK_X} strokeWidth="1.8"/>
      <text fontFamily="system-ui,sans-serif" fontSize="11" fill="#374151" x="480" y="39">{t('bookings.floorMap.legend.noParking')}</text>
      <rect x="554" y="24" width="22" height="22" rx="5" fill="#F9FAFB" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="11" fill="#374151" x="580" y="39">{t('bookings.floorMap.legend.stairs')}</text>

      {/* Left ramp */}
      <rect x="30" y="70" width="74" height="528" rx="6" fill="#F9FAFB" stroke="#E5E7EB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fill="#6B7280" x="67" y="295" textAnchor="middle">{t('bookings.floorMap.level')}</text>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fill="#6B7280" x="67" y="308" textAnchor="middle">-2</text>
      <line x1="67" y1="440" x2="67" y2="180" stroke="#9CA3AF" strokeWidth="1.5" markerEnd="url(#arr)"/>

      {/* Left column 117–128 */}
      <rect x="108" y="70" width="118" height="528" rx="6" fill="#FFFFFF" stroke="#D1D5DB" strokeWidth="1"/>
      {([128,127,126,125,124,123,122,121,120,119,118,117] as number[]).map((num, i) => {
        const y = 71 + i * 44;
        return (
          <g key={num} {...spotProps(num)}>
            <rect x="109" y={y} width="116" height="43" rx="5" fill={sc(num)}/>
            {i < 11 && <line x1="108" y1={y+43} x2="226" y2={y+43} stroke={divider(num)} strokeWidth="0.8"/>}
            <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="167" y={y+26} textAnchor="middle">{num}</text>
          </g>
        );
      })}

      {/* Road gap */}
      <rect x="108" y="598" width="554" height="44" fill="#F3F4F6"/>
      <line x1="108" y1="620" x2="662" y2="620" stroke="#D1D5DB" strokeWidth="1" strokeDasharray="10 6"/>
      <line x1="130" y1="620" x2="310" y2="620" stroke="#9CA3AF" strokeWidth="1.3" markerEnd="url(#arr)" opacity="0.7"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fill="#9CA3AF" x="220" y="614" textAnchor="middle">{t('bookings.floorMap.directionOfTravel')}</text>
      <line x1="67" y1="700" x2="67" y2="650" stroke="#9CA3AF" strokeWidth="2" markerEnd="url(#arr)"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fill="#9CA3AF" x="67" y="716" textAnchor="middle">{t('bookings.floorMap.entrance')}</text>

      {/* Bottom row 66/67/68 */}
      <rect x="108" y="642" width="554" height="70" rx="6" fill="#FFFFFF" stroke="#D1D5DB" strokeWidth="1"/>
      {([{x:109,n:66},{x:169,n:67},{x:229,n:68}] as {x:number,n:number}[]).map(({x,n},i)=>(
        <g key={n} {...spotProps(n)}>
          <rect x={x} y="643" width="59" height="68" rx="5" fill={sc(n)}/>
          <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x={x+29} y="681" textAnchor="middle">{n}</text>
          {i < 2 && <line x1={x+59} y1="643" x2={x+59} y2="711" stroke={divider(n)} strokeWidth="0.8"/>}
        </g>
      ))}

      {/* No parking bottom row */}
      {([{x:289},{x:349},{x:409},{x:469},{x:529},{x:589}] as {x:number}[]).map(({x},i)=>(
        <g key={x}>
          <rect x={x} y="643" width={i===5?72:59} height="68" rx="5" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1={x+9} y1="653" x2={x+(i===5?63:50)} y2="702" stroke={NO_PARK_X} strokeWidth="2"/>
          <line x1={x+(i===5?63:50)} y1="653" x2={x+9} y2="702" stroke={NO_PARK_X} strokeWidth="2"/>
          {i < 5 && <line x1={x+59} y1="643" x2={x+59} y2="711" stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}

      {/* Center pill */}
      <rect x="300" y="136" width="170" height="414" rx="50" fill="#F9FAFB" stroke="#D1D5DB" strokeWidth="1.5"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="385" y="174" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="300" y1="186" x2="470" y2="186" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="385" y1="186" x2="385" y2="288" stroke="#D1D5DB" strokeWidth="0.8"/>

      {([187,221,255] as number[]).map((y,i)=>(
        <g key={y}>
          <rect x="304" y={y} width="78" height="32" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="311" y1={y+5} x2="379" y2={y+27} stroke={NO_PARK_X} strokeWidth="1.8"/>
          <line x1="379" y1={y+5} x2="311" y2={y+27} stroke={NO_PARK_X} strokeWidth="1.8"/>
          <rect x="388" y={y} width="78" height="32" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="395" y1={y+5} x2="463" y2={y+27} stroke={NO_PARK_X} strokeWidth="1.8"/>
          <line x1="463" y1={y+5} x2="395" y2={y+27} stroke={NO_PARK_X} strokeWidth="1.8"/>
          {i < 2 && <line x1="300" y1={y+33} x2="470" y2={y+33} stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}

      <line x1="300" y1="288" x2="470" y2="288" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="385" y="318" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="300" y1="330" x2="470" y2="330" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="385" y1="330" x2="385" y2="458" stroke="#D1D5DB" strokeWidth="0.8"/>

      {/* 114 | no-park */}
      <g {...spotProps(114)}>
        <rect x="304" y="331" width="78" height="40" rx="3" fill={sc(114)}/>
        <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="343" y="356" textAnchor="middle">114</text>
      </g>
      <rect x="388" y="331" width="78" height="40" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
      <line x1="395" y1="336" x2="463" y2="367" stroke={NO_PARK_X} strokeWidth="1.8"/>
      <line x1="463" y1="336" x2="395" y2="367" stroke={NO_PARK_X} strokeWidth="1.8"/>
      <line x1="300" y1="372" x2="470" y2="372" stroke="#E5E7EB" strokeWidth="0.8"/>

      {/* 113 | 109 */}
      <g {...spotProps(113)}>
        <rect x="304" y="373" width="78" height="40" rx="3" fill={sc(113)}/>
        <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="343" y="398" textAnchor="middle">113</text>
      </g>
      <g {...spotProps(109)}>
        <rect x="388" y="373" width="78" height="40" rx="3" fill={sc(109)}/>
        <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="427" y="398" textAnchor="middle">109</text>
      </g>
      <line x1="300" y1="414" x2="470" y2="414" stroke="#E5E7EB" strokeWidth="0.8"/>

      {/* 112 | 108 */}
      <g {...spotProps(112)}>
        <rect x="304" y="415" width="78" height="40" rx="3" fill={sc(112)}/>
        <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="343" y="440" textAnchor="middle">112</text>
      </g>
      <g {...spotProps(108)}>
        <rect x="388" y="415" width="78" height="40" rx="3" fill={sc(108)}/>
        <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="427" y="440" textAnchor="middle">108</text>
      </g>
      <line x1="300" y1="456" x2="470" y2="456" stroke="#D1D5DB" strokeWidth="1"/>

      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="385" y="492" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
    </svg>
  );
};

export default FloorMinus1;
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

export const FloorMinus2: React.FC<Props> = ({ takenSpots = [], onSpotClick }) => {
  const { t } = useTranslation();

  const sc = (num: number) => takenSpots.includes(num) ? TAKEN : AVAILABLE;
  const divider = (num: number) => takenSpots.includes(num) ? TAKEN_DIVIDER : AVAILABLE_DIVIDER;
  const spotProps = (num: number) => ({
    onClick: () => onSpotClick?.(num),
    style: { cursor: onSpotClick ? 'pointer' : 'default' },
  });

  return (
    <svg width="100%" viewBox="0 0 680 700" role="img" aria-label={t('bookings.parkingMap.title')}>
      <defs>
        <marker id="arr2" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="5" markerHeight="5" orient="auto-start-reverse">
          <path d="M2 1L8 5L2 9" fill="none" stroke="context-stroke" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
        </marker>
      </defs>

      <rect x="0" y="0" width="680" height="700" fill="#F3F4F6"/>
      <rect x="18" y="18" width="644" height="664" rx="10" fill="#FFFFFF" stroke="#E5E7EB" strokeWidth="1"/>

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

      {/* Outer wall */}
      <rect x="30" y="62" width="620" height="590" rx="6" fill="none" stroke="#D1D5DB" strokeWidth="1.5"/>

      {/* Entrance */}
      <rect x="30" y="62" width="80" height="480" rx="6" fill="#F9FAFB" stroke="#E5E7EB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="70" y="295" textAnchor="middle">{t('bookings.floorMap.entrance')}</text>
      <line x1="70" y1="410" x2="70" y2="160" stroke="#9CA3AF" strokeWidth="2" markerEnd="url(#arr2)"/>


      {/* Road gap */}
      <rect x="30" y="542" width="620" height="44" fill="#F3F4F6"/>
      <line x1="30" y1="564" x2="650" y2="564" stroke="#D1D5DB" strokeWidth="1" strokeDasharray="10 6"/>
      <line x1="120" y1="564" x2="300" y2="564" stroke="#9CA3AF" strokeWidth="1.3" markerEnd="url(#arr2)" opacity="0.6"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fill="#9CA3AF" x="210" y="558" textAnchor="middle">{t('bookings.floorMap.directionOfTravel')}</text>

      {/* Left pill */}
      <rect x="160" y="128" width="155" height="324" rx="46" fill="#F9FAFB" stroke="#D1D5DB" strokeWidth="1.5"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="237" y="159" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="160" y1="180" x2="315" y2="180" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="237" y1="180" x2="237" y2="264" stroke="#D1D5DB" strokeWidth="0.8"/>

      {([181,209,237] as number[]).map((y,i)=>(
        <g key={y}>
          <rect x="163" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="169" y1={y+4} x2="231" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="231" y1={y+4} x2="169" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <rect x="240" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="246" y1={y+4} x2="308" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="308" y1={y+4} x2="246" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          {i < 2 && <line x1="160" y1={y+27} x2="315" y2={y+27} stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}

      <line x1="160" y1="264" x2="315" y2="264" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="237" y="295" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="160" y1="316" x2="315" y2="316" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="237" y1="316" x2="237" y2="400" stroke="#D1D5DB" strokeWidth="0.8"/>

      {([
        {y:317, l:55, r:null},
        {y:345, l:54, r:49},
        {y:373, l:53, r:48},
      ] as {y:number,l:number,r:number|null}[]).map(({y,l,r},i)=>(
        <g key={y}>
          <g {...spotProps(l)}>
            <rect x="163" y={y} width="71" height="26" rx="3" fill={sc(l)}/>
            <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="199" y={y+18} textAnchor="middle">{l}</text>
          </g>
          {r !== null ? (
            <g {...spotProps(r)}>
              <rect x="240" y={y} width="71" height="26" rx="3" fill={sc(r)}/>
              <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="13" fill="#FFFFFF" x="276" y={y+18} textAnchor="middle">{r}</text>
            </g>
          ) : (
            <g>
              <rect x="240" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
              <line x1="246" y1={y+4} x2="308" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
              <line x1="308" y1={y+4} x2="246" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
            </g>
          )}
          {i < 2 && <line x1="160" y1={y+27} x2="315" y2={y+27} stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}

      <line x1="160" y1="400" x2="315" y2="400" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="237" y="431" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>

      {/* Right pill — all no-parking */}
      <rect x="365" y="128" width="155" height="324" rx="46" fill="#F9FAFB" stroke="#D1D5DB" strokeWidth="1.5"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="442" y="159" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="365" y1="180" x2="520" y2="180" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="442" y1="180" x2="442" y2="264" stroke="#D1D5DB" strokeWidth="0.8"/>
      {([181,209,237] as number[]).map((y,i)=>(
        <g key={y}>
          <rect x="368" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="374" y1={y+4} x2="436" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="436" y1={y+4} x2="374" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <rect x="445" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="451" y1={y+4} x2="513" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="513" y1={y+4} x2="451" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          {i < 2 && <line x1="365" y1={y+27} x2="520" y2={y+27} stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}
      <line x1="365" y1="264" x2="520" y2="264" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="442" y="295" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>
      <line x1="365" y1="316" x2="520" y2="316" stroke="#D1D5DB" strokeWidth="1"/>
      <line x1="442" y1="316" x2="442" y2="400" stroke="#D1D5DB" strokeWidth="0.8"/>
      {([317,345,373] as number[]).map((y,i)=>(
        <g key={y}>
          <rect x="368" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="374" y1={y+4} x2="436" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="436" y1={y+4} x2="374" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <rect x="445" y={y} width="71" height="26" rx="3" fill={NO_PARK_FILL} stroke="#E5E7EB" strokeWidth="1"/>
          <line x1="451" y1={y+4} x2="513" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          <line x1="513" y1={y+4} x2="451" y2={y+22} stroke={NO_PARK_X} strokeWidth="1.6"/>
          {i < 2 && <line x1="365" y1={y+27} x2="520" y2={y+27} stroke="#E5E7EB" strokeWidth="0.8"/>}
        </g>
      ))}
      <line x1="365" y1="400" x2="520" y2="400" stroke="#D1D5DB" strokeWidth="1"/>
      <text fontFamily="system-ui,sans-serif" fontSize="10" fontWeight="600" fill="#6B7280" x="442" y="431" textAnchor="middle">{t('bookings.floorMap.stairs')}</text>

      {/* Bottom row 1–21 */}
      <rect x="30" y="586" width="620" height="66" rx="6" fill="#FFFFFF" stroke="#D1D5DB" strokeWidth="1"/>
      {Array.from({length: 21}, (_, i) => {
        const x = 31 + i * 29;
        const w = i === 20 ? 38 : 28;
        const num = i + 1;
        return (
          <g key={num} {...spotProps(num)}>
            <rect x={x} y="587" width={w} height="64" rx="3" fill={sc(num)}/>
            <text fontFamily="system-ui,sans-serif" fontWeight="700" fontSize="11" fill="#FFFFFF" x={x + w/2} y="624" textAnchor="middle">{num}</text>
            {i < 20 && <line x1={x+w} y1="587" x2={x+w} y2="651" stroke={divider(num)} strokeWidth="0.5"/>}
          </g>
        );
      })}
    </svg>
  );
};

export default FloorMinus2;
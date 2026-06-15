import * as React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '../../../components/ui/Button';

export const OfficeMap: React.FC = () => {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = React.useState(false);

  const openModal  = () => setIsOpen(true);
  const closeModal = () => setIsOpen(false);

  React.useEffect(() => {
    if (!isOpen) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') closeModal();
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [isOpen]);

  React.useEffect(() => {
    document.body.style.overflow = isOpen ? 'hidden' : '';
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  const office      = t('bookings.floorMap.rooms.office');
  const meetingRoom = t('bookings.floorMap.rooms.meetingRoom');
  const kitchenWc   = t('bookings.floorMap.rooms.kitchenWc');
  const stairs      = t('bookings.floorMap.rooms.stairs');
  const corridor    = t('bookings.floorMap.rooms.corridor');

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

  return (
    <>
      <Button variant="outline" onClick={openModal}>
        {t('bookings.officeMap.title')}
      </Button>

      {isOpen && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
          onClick={(e) => { if (e.target === e.currentTarget) closeModal(); }}
          role="dialog"
          aria-modal="true"
          aria-label={t('bookings.officeMap.title')}
        >
          <div className="flex max-h-[90vh] w-full max-w-3xl flex-col overflow-hidden rounded-xl bg-white shadow-2xl">

            <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
              <h2 className="text-lg font-bold tracking-wide text-gray-900">
                {t('bookings.officeMap.title')}
              </h2>
              <button
                onClick={closeModal}
                className="flex h-8 w-8 items-center justify-center rounded-lg text-gray-400 transition-colors hover:bg-gray-100 hover:text-gray-600"
                aria-label={t('bookings.officeMap.closeAria')}
              >
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M2 2L14 14M14 2L2 14" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                </svg>
              </button>
            </div>

            <div className="overflow-y-auto p-4">
              <svg width="100%" viewBox="0 0 680 560" role="img" aria-label={t('bookings.officeMap.title')}>
                <rect x="0" y="0" width="680" height="560" fill="#FFFFFF"/>

                <rect x="30" y="30" width="620" height="280" fill="none" stroke={C.wall} strokeWidth="2"/>
                <rect x="30" y="310" width="440" height="220" fill="none" stroke={C.wall} strokeWidth="2"/>

                <rect x="30" y="30"  width="130" height="93" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="95" y="79" textAnchor="middle">{office}</text>
                <rect x="30" y="123" width="130" height="93" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="95" y="172" textAnchor="middle">{office}</text>
                <rect x="30" y="216" width="130" height="94" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="95" y="265" textAnchor="middle">{office}</text>

                <rect x="30" y="310" width="130" height="220" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="95" y="423" textAnchor="middle">{office}</text>

                <rect x="160" y="30" width="80"  height="70" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="200" y="68" textAnchor="middle">{office}</text>
                <rect x="240" y="30" width="110" height="70" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="295" y="68" textAnchor="middle">{office}</text>
                <rect x="350" y="30" width="80"  height="70" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="390" y="68" textAnchor="middle">{office}</text>
                <rect x="430" y="30" width="80"  height="70" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="470" y="68" textAnchor="middle">{office}</text>
                <rect x="510" y="30" width="140" height="70" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="580" y="68" textAnchor="middle">{office}</text>

                <rect x="160" y="100" width="400" height="40" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.corridor.text} x="360" y="124" textAnchor="middle">{corridor}</text>

                <rect x="160" y="140" width="40" height="170" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="1"/>

                <rect x="560" y="100" width="40" height="210" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="1"/>

                <rect x="200" y="140" width="280" height="170" fill={C.kitchen.fill} stroke={C.kitchen.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="13" fontWeight="500" fill={C.kitchen.text} x="340" y="225" textAnchor="middle">{kitchenWc}</text>

                <rect x="480" y="140" width="80" height="170" fill={C.meeting.fill} stroke={C.meeting.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.meeting.text} x="520" y="228" textAnchor="middle">{meetingRoom}</text>

                <rect x="600" y="100" width="50" height="105" fill={C.meeting.fill} stroke={C.meeting.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.meeting.text} x="625" y="155" textAnchor="middle">{meetingRoom}</text>
                <rect x="600" y="205" width="50" height="105" fill={C.meeting.fill} stroke={C.meeting.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.meeting.text} x="625" y="260" textAnchor="middle">{meetingRoom}</text>

                <rect x="160" y="310" width="310" height="40" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.corridor.text} x="315" y="334" textAnchor="middle">{corridor}</text>

                <rect x="160" y="350" width="150" height="60" fill={C.stairs.fill} stroke={C.stairs.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.stairs.text} x="235" y="383" textAnchor="middle">{stairs}</text>
                {[362, 374, 386, 398].map((y) => (
                  <line key={y} x1="160" y1={y} x2="310" y2={y} stroke="#FFFFFF" strokeWidth="0.5" opacity="0.4"/>
                ))}

                <rect x="160" y="410" width="75" height="120" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="197" y="473" textAnchor="middle">{office}</text>

                <rect x="235" y="410" width="75" height="120" fill={C.meeting.fill} stroke={C.meeting.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.meeting.text} x="272" y="473" textAnchor="middle">{meetingRoom}</text>

                <rect x="310" y="350" width="70" height="180" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="345" y="443" textAnchor="middle">{office}</text>

                <rect x="380" y="350" width="55" height="180" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="407" y="443" textAnchor="middle">{office}</text>

                <rect x="435" y="310" width="55" height="220" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="1"/>
                <text fontFamily="system-ui,sans-serif" fontSize="12" fill={C.office.text} x="462" y="423" textAnchor="middle">{office}</text>

                <rect x="500" y="365" width="165" height="145" rx="6" fill={C.legendBg} stroke={C.legendBorder} strokeWidth="0.5"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="574" y="382" textAnchor="middle">Legenda</text>
                <rect x="510" y="392" width="10" height="10" rx="2" fill={C.office.fill} stroke={C.office.stroke} strokeWidth="0.8"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="522" y="402">{office}</text>
                <rect x="510" y="410" width="10" height="10" rx="2" fill={C.meeting.fill} stroke={C.meeting.stroke} strokeWidth="0.8"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="522" y="420">{meetingRoom}</text>
                <rect x="510" y="428" width="10" height="10" rx="2" fill={C.kitchen.fill} stroke={C.kitchen.stroke} strokeWidth="0.8"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="522" y="438">{kitchenWc}</text>
                <rect x="510" y="446" width="10" height="10" rx="2" fill={C.stairs.fill} stroke={C.stairs.stroke} strokeWidth="0.8"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="522" y="456">{stairs}</text>
                <rect x="510" y="464" width="10" height="10" rx="2" fill={C.corridor.fill} stroke={C.corridor.stroke} strokeWidth="0.8"/>
                <text fontFamily="system-ui,sans-serif" fontSize="11" fill={C.legendText} x="522" y="474">{corridor}</text>
              </svg>
            </div>

          </div>
        </div>
      )}
    </>
  );
};

export default OfficeMap;
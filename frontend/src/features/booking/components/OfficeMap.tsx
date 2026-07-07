import * as React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '../../../components/ui/Button';
import { OfficeFloor1 } from '../../../assets/OfficeFloor1';
import { OfficeFloor2 } from '../../../assets/OfficeFloor2';

type FloorLevel = '1' | '2';

export const OfficeMap: React.FC = () => {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = React.useState(false);
  const [activeFloor, setActiveFloor] = React.useState<FloorLevel>('1');

  const open = t('bookings.officeMap.title');

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

  return (
    <>
      <Button variant="outline" onClick={openModal}>
        {open}
      </Button>

      {isOpen && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center p-4"
          role="dialog"
          aria-modal="true"
          aria-label={t('bookings.officeMap.title')}
        >
          <button
            type="button"
            className="fixed inset-0 cursor-default bg-black/50"
            aria-label={t('bookings.officeMap.closeAria')}
            onClick={closeModal}
          />
          <div className="relative z-10 flex max-h-[90vh] w-full max-w-3xl flex-col overflow-hidden rounded-xl bg-white shadow-2xl">

            {/* Header */}
            <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
              <h2 className="text-lg font-bold tracking-wide text-gray-900">
                {t('bookings.officeMap.title')}
              </h2>

              {/* Floor tabs */}
              <div className="flex gap-1 rounded-lg border border-gray-200 bg-gray-100 p-1">
                {(['1', '2'] as FloorLevel[]).map((level) => (
                  <button
                    key={level}
                    onClick={() => setActiveFloor(level)}
                    className={[
                      'rounded-md px-4 py-1.5 text-sm font-semibold transition-colors',
                      activeFloor === level
                        ? 'bg-white text-gray-900 shadow-sm'
                        : 'text-gray-500 hover:text-gray-700',
                    ].join(' ')}
                  >
                    {t('bookings.officeMap.floorTab', { floor: level })}
                  </button>
                ))}
              </div>

              {/* Close */}
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

            {/* Map */}
            <div className="overflow-y-auto p-4">
              {activeFloor === '1' ? <OfficeFloor1 /> : <OfficeFloor2 />}
            </div>

          </div>
        </div>
      )}
    </>
  );
};

export default OfficeMap;
import { useState } from "react";
import type { Filters } from "../types";
import { Button } from "../../../components/ui/Button";
import { Tab } from "../../../components/ui/Tab";
import { FiltersBar } from "../../../components/ui/FilterBar";
import type { AssetDto } from "../../asset/types";


type BookingModalProps = {
    open: boolean;
    onClose: () => void;
    asset: AssetDto | null;
    filters: Filters;
    setFilters: React.Dispatch<React.SetStateAction<Filters>>;
};

export function BookingModal({ open, onClose, asset, filters, setFilters }: BookingModalProps) {
    const [activeTab, setActiveTab] = useState<'date' | 'availability'>('date');
    const selectedFrom = `${filters.fromDate || '-'} ${filters.fromHour || ''}`;
    const selectedTo = `${filters.toDate || '-'} ${filters.toHour || ''}`;

    if (!open || !asset) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30">
            <div className="w-full max-w-150 bg-(--color-bg) p-8 shadow-lg">
                <h2 className="mb-4 text-xl font-semibold">
                    Book {asset.name}
                </h2>

                <div className="mb-5 text-sm">
                    <p className="font-semibold">{asset.name}</p>
                    <p>Model: {asset.name ?? '-'}</p>
                    <p>Location: {asset.location ?? '-'}</p>
                </div>

                <div className="mb-6 border-b border-gray-200">
                    <Tab
                        value={activeTab}
                        onChange={setActiveTab}
                        tabs={[
                            { label: 'Choose a date', value: 'date' },
                            { label: 'Show availability', value: 'availability' },
                        ]}
                    />

                </div>
                {activeTab === 'date' && (
                    <div className="space-y-6">
                        <div className="w-full">
                            <FiltersBar filters={filters} setFilters={setFilters} showSearch={false}  className="grid-cols-1 sm:grid-cols-2 lg:grid-cols-2"/>
                        </div>

                        {/* Implement checking asset status, if available user can book asset, else button book is disabled  */}
                        <div className="flex items-start gap-3 text-sm">
                            <span className="flex h-5 w-5 items-center justify-center bg---color-status-active-bg text-(--color-status-active-text)">
                                ✓
                            </span>

                            <p>
                                {asset.name} is{' '}
                                <span className="rounded bg-(--color-status-active-bg) px-2 py-0.5 text-(--color-status-active-text)">
                                    available
                                </span>{' '}
                                from {selectedFrom} to {selectedTo}
                            </p>
                        </div>
                    </div>
                )}

                {activeTab === 'availability' && (
                    <div className="text-sm">
                        Availability list will be shown here.
                    </div>
                )}

                <div className="mt-10 flex justify-end gap-4">
                    <Button
                        variant="outline"
                        size="md"
                        onClick={onClose}
                    >
                        Cancel
                    </Button>

                    <Button
                        variant="solid"
                        size="md"
                        onClick={() => {
                            console.log('BOOK', {
                                asset,
                                from: filters.fromDate,
                                to: filters.toDate,
                            });
                        }}
                    >
                        Book now
                    </Button>
                </div>
            </div>
        </div>
    );
}
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { useState } from 'react';
import Select from 'react-select';

type Props = {
    open: boolean;
    onClose: () => void;
};

export const AddCategoryModal: React.FC<Props> = ({ open, onClose }) => {
    if (!open) return null;
    const [approvalValue, setApprovalValue] = useState(false);
    const assetOptions = [
        { value: "MacBookPro", label: "MacBook Pro" },
        { value: "HPElitebook", label: "HP Elitebook" }
    ];
    const bookigPerionOptions = [
        { value: "hour", label: "Hour" },
        { value: "day", label: "Day" },
        { value: "week", label: "Week" },
        { value: "month", label: "Month" }
    ]
    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
            role="dialog"
            aria-modal="true"
            aria-label="Asset details"
            onMouseDown={(e) => {
                if (e.target === e.currentTarget) onClose();
            }}
        >
            <div className="w-full max-w-[800px] overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
                <div className="relative flex items-center justify-center px-8 pt-6 pb-4">
                    <div className="font-bold text-xl text-center">
                        Add New Category
                    </div>

                    <button
                        type="button"
                        onClick={onClose}
                        aria-label="Close"
                        className="absolute right-8 inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
                    >
                        <CloseIcon className="pointer-events-none" />
                    </button>
                </div>
                <div className="mx-8 h-px bg-(--color-table-border) m-4"></div>
                <form className="flex flex-col gap-3">
                    <label htmlFor="name" className='mt-5 ml-5 font-bold'> Name</label>
                    <input
                        type="text"
                        id="name"
                        placeholder="Name"
                        className="p-2 ml-5 mr-5  border border-(--color-table-border) rounded focus:border-(--color-select-border) focus:border-2 outline-none"
                    />
                    <label htmlFor="description" className='mt-5 ml-5 font-bold'> Description</label>
                    <input
                        type="text"
                        id='description'
                        placeholder="Description"
                        className="p-2 ml-5 mr-5 border border-(--color-table-border) rounded focus:border-(--color-select-border) focus:border-2 outline-none"
                    />
                    <label htmlFor="bookingPeriod" className='mt-5 ml-5 font-bold'> Booking Period</label>
                    <Select
                        id='bookingPeriod'
                        options={bookigPerionOptions}
                        className="p-2 ml-3 mr-5 outline-none"
                    >
                    </Select>

                    <label htmlFor="picture" className='mt-5 ml-5 font-bold'> Add category picture</label>
                    <input
                        type="file"
                        accept="image/*"
                        id="picture"
                        className="p-2 border border-(--color-table-border) rounded ml-5 mr-5 focus:border-(--color-select-border) focus:border-2 outline-none"
                    />

                    <label htmlFor="assets" className='mt-5 ml-5 font-bold'> Assets</label>
                    { /* TODO: fetch all assets so we can assign them to categoru */}
                    <Select
                        isMulti
                        options={assetOptions}
                        id='assets'
                        className="p-2 ml-3 mr-5 focus:border-black outline-none"
                    >
                    </Select>

                    <div className='mt-5 ml-5 font-bold'>

                        <input
                            type="checkbox"
                            checked={approvalValue}
                            onChange={(e) => setApprovalValue(e.target.checked)}
                            className='mr-2' />
                        All Assets must be approved by Manager
                    </div>

                    <div className="mx-8 h-px bg-(--color-table-border) mt-5"></div>

                    {/*TODO: Connect with BE and DB so new category can be added in database */}
                    <div className="flex justify-center gap-2 mt-5">
                        <Button
                            type="submit"
                            className="px-6 py-4 w-70 h-10 mb-5 mr-5 font-bold"
                        >
                            Add
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
};
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
    { value: 'MacBookPro', label: 'MacBook Pro' },
    { value: 'HPElitebook', label: 'HP Elitebook' },
  ];
  const bookigPerionOptions = [
    { value: 'hour', label: 'Hour' },
    { value: 'day', label: 'Day' },
    { value: 'week', label: 'Week' },
    { value: 'month', label: 'Month' },
  ];
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
      <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="relative flex items-center justify-center px-8 pt-6 pb-4">
          <div className="text-center text-xl font-bold">Add New Category</div>

          <button
            type="button"
            onClick={onClose}
            aria-label="Close"
            className="absolute right-8 inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
          >
            <CloseIcon className="pointer-events-none" />
          </button>
        </div>
        <div className="m-4 mx-8 h-px bg-(--color-table-border)"></div>
        <form className="flex flex-col gap-3">
          <label htmlFor="name" className="mt-5 ml-5 font-bold">
            Name
          </label>
          <input
            type="text"
            id="name"
            placeholder="Name"
            className="mr-5 ml-5 rounded border border-(--color-table-border) p-2 outline-none focus:border-2 focus:border-(--color-select-border)"
          />
          <label htmlFor="description" className="mt-5 ml-5 font-bold">
            Description
          </label>
          <input
            type="text"
            id="description"
            placeholder="Description"
            className="mr-5 ml-5 rounded border border-(--color-table-border) p-2 outline-none focus:border-2 focus:border-(--color-select-border)"
          />
          <label htmlFor="bookingPeriod" className="mt-5 ml-5 font-bold">
            Booking Period
          </label>
          <Select
            id="bookingPeriod"
            options={bookigPerionOptions}
            className="mr-5 ml-3 p-2 outline-none"
          ></Select>

          <label htmlFor="picture" className="mt-5 ml-5 font-bold">
            Add category picture
          </label>
          <input
            type="file"
            accept="image/*"
            id="picture"
            className="mr-5 ml-5 rounded border border-(--color-table-border) p-2 outline-none focus:border-2 focus:border-(--color-select-border)"
          />

          <label htmlFor="assets" className="mt-5 ml-5 font-bold">
            Assets
          </label>
          {/* TODO: fetch all assets so we can assign them to categoru */}
          <Select
            isMulti
            options={assetOptions}
            id="assets"
            className="mr-5 ml-3 p-2 outline-none focus:border-black"
          ></Select>

          <div className="mt-5 ml-5 font-bold">
            <input
              type="checkbox"
              checked={approvalValue}
              onChange={(e) => setApprovalValue(e.target.checked)}
              className="mr-2"
            />
            All Assets must be approved by Manager
          </div>

          <div className="mx-8 mt-5 h-px bg-(--color-table-border)"></div>

          {/*TODO: Connect with BE and DB so new category can be added in database */}
          <div className="mt-5 flex justify-center gap-2">
            <Button
              type="submit"
              className="mr-5 mb-5 h-10 w-70 px-6 py-4 font-bold"
            >
              Add
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

import { useEffect, useState } from 'react';

export function useEditFormChanges(isEditMode: boolean, resetKey: string) {
  // track if the form has changes
  const [hasChanges, setHasChanges] = useState(false);

  // reset the changes flag when the reset key changes
  useEffect(() => {
    setHasChanges(false);
  }, [resetKey]);

  // function which is called when the form changes
  const onFormChange = () => {
    if (isEditMode) setHasChanges(true);
  };

  return {
    onFormChange,
    isSaveDisabled: isEditMode && !hasChanges,
  };
}

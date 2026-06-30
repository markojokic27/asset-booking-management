import { useTranslation } from 'react-i18next';

import type { AssetStatus } from '../types';

const statusClassNameConfig: Record<AssetStatus, string> = {
  ACTIVE:
    'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  INACTIVE:
    'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
  DAMAGED:
    'bg-(--color-status-damaged-bg) text-(--color-status-damaged-text)',
  DELETED:
    'bg-(--color-status-deleted-bg) text-(--color-status-deleted-text)',
};

type AssetStatusBadgeProps = {
  status: AssetStatus;
};

export function AssetStatusBadge({ status }: Readonly<AssetStatusBadgeProps>) {
  const { t } = useTranslation();

  const label = t(`assets.status.${status}`);
  const statusClassName = statusClassNameConfig[status];

  return (
    <span
      className={[
        'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
        statusClassName,
      ].join(' ')}
    >
      {label}
    </span>
  );
}

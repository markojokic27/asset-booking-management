import * as React from 'react';
import { twMerge } from 'tailwind-merge';

export type TableColumn<T> = {
  key: React.Key;
  header: React.ReactNode;
  accessor?: keyof T;
  render?: (row: T, index: number) => React.ReactNode;
  headerClassName?: string;
  cellClassName?: string;
};


export type TableProps<T> = {
  data: T[];
  columns: TableColumn<T>[];
  getRowKey: (row: T, index: number) => React.Key;
  className?: string;
  rowClassName?: string | ((row: T, index: number) => string | undefined);
  emptyMessage?: React.ReactNode;
};

const tableContainerClassName =
  'overflow-hidden rounded-lg border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--color-table-shadow)';

const tableClassName = 'min-w-full border-collapse text-left text-sm';

const tableHeadClassName =
  'border-b border-(--color-table-border) bg-(--color-table-head) text-(--color-table-head-text) text-xs uppercase tracking-[0.2em]';

const tableHeaderCellClassName = 'px-6 py-4 font-semibold';

const defaultTableRowClassName =
  'border-b border-(--color-table-row-border) transition-colors hover:bg-(--color-table-row-hover)';

const tableCellClassName = 'px-6 py-4 text-(--color-table-text)';

const getCellContent = <T,>(column: TableColumn<T>, row: T, index: number) => {
  if (column.render) {
    return column.render(row, index);
  }

  if (column.accessor) {
    return row[column.accessor] as React.ReactNode;
  }

  return null;
};

export function Table<T>({
  data,
  columns,
  getRowKey,
  className,
  rowClassName,
  emptyMessage = 'No data available.',
}: TableProps<T>) {
  return (
    <div className={twMerge(tableContainerClassName, className)}>
      <div className="overflow-x-auto">
        <table className={tableClassName}>
          <thead className={tableHeadClassName}>
            <tr>
              {columns.map((column) => (
                <th
                  key={column.key}
                  scope="col"
                  className={twMerge(
                    tableHeaderCellClassName,
                    column.headerClassName
                  )}
                >
                  {column.header}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {data.length > 0 ? (
              data.map((row, index) => (
                <tr
                  key={getRowKey(row, index)}
                  className={twMerge(
                    defaultTableRowClassName,
                    typeof rowClassName === 'function'
                      ? rowClassName(row, index)
                      : rowClassName
                  )}
                >
                  {columns.map((column) => (
                    <td
                      key={column.key}
                      className={twMerge(tableCellClassName, column.cellClassName)}
                    >
                      {getCellContent(column, row, index)}
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={columns.length}
                  className={twMerge(
                    tableCellClassName,
                    'py-8 text-center text-(--color-table-head-text)'
                  )}
                >
                  {emptyMessage}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

import * as React from 'react'
import * as Checkbox from '@radix-ui/react-checkbox'
import { Controller } from 'react-hook-form'
import { twMerge } from 'tailwind-merge'
import { useTranslation } from 'react-i18next'

export type MultiSelectOption = {
  value: string
  label: string
}

type Props = {
  name: string
  control: any
  label?: string
  options: readonly MultiSelectOption[]
  onOpen?: () => void
  loading?: boolean
  error?: string
}

const fieldLabelClassName =
  'mb-2 block text-[10px] font-semibold uppercase tracking-[0.22em] text-(--color-table-head-text) opacity-60'

const fieldClassName =
  'w-full rounded-lg border border-(--color-table-border) bg-(--color-table-surface) px-2 py-3 text-sm font-medium text-(--color-text) shadow-(--shadow-card) outline-none transition'

export const MultiSelect = ({
  name,
  control,
  label,
  options,
  onOpen,
  loading,
  error
}: Props) => {
  const { t } = useTranslation()
  const [open, setOpen] = React.useState(false)
  const [search, setSearch] = React.useState('')

  const handleToggleOpen = () => {
    if (!open && onOpen) {
      onOpen()
    }
    setOpen(o => !o)
  }

  return (
    <Controller
      name={name}
      control={control}
      defaultValue={[]}
      render={({ field }) => {
        const value: string[] = field.value || []

        const toggle = (val: string) => {
          if (value.includes(val)) {
            field.onChange(value.filter(v => v !== val))
          } else {
            field.onChange([...value, val])
          }
        }

        const selectedOptions = options.filter(o =>
          value.includes(o.value)
        )

        const filteredOptions = options.filter(o =>
          o.label.toLowerCase().includes(search.toLowerCase())
        )

        return (
          <div className="w-full relative">

            {/* LABEL */}
            {label && (
              <label className={fieldLabelClassName}>
                {label}
              </label>
            )}

            {/* SELECT BOX */}
            <button
              type="button"
              className={twMerge(
                fieldClassName,
                'cursor-pointer'
              )}
              onClick={handleToggleOpen}
            >

              {/* SELECTED CHIPS */}
              <div className="flex flex-wrap gap-2">
                {selectedOptions.length === 0 && (
                  <span className="text-gray-400">
                    {t('ui.multiSelect.selectAssets')}
                  </span>
                )}

                {selectedOptions.map(opt => (
                  <span
                    key={opt.value}
                    className="flex items-center gap-1 rounded bg-(--color-primaryblue) px-2 py-1 text-xs text-white"
                  >
                    {opt.label}
                    <button
                      type="button"
                      onClick={(e) => {
                        e.stopPropagation()
                        toggle(opt.value)
                      }}
                    >
                      ×
                    </button>
                  </span>
                ))}
              </div>
            </button>

            {/* DROPDOWN */}
            {
              open && (
                <div className="absolute z-50 mt-2 w-full rounded-lg border border-(--color-table-border) bg-(--color-table-surface) shadow-lg">

                  {/* SEARCH */}
                  <input
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder={t('ui.multiSelect.searchAssetsPlaceholder')}
                    className="w-full border-b border-(--color-table-border) bg-transparent px-3 py-2 text-sm outline-none"
                    onClick={(e) => e.stopPropagation()}
                  />

                  {/* OPTIONS */}
                  <div className="max-h-60 overflow-y-auto">

                    {loading && (
                      <div className="px-3 py-3 text-sm text-gray-500">
                        {t('ui.multiSelect.loadingAssets')}
                      </div>
                    )}

                    {!loading && error && (
                      <div className="px-3 py-3 text-sm text-red-500">
                        {t('ui.multiSelect.failedToLoadAssets')}
                      </div>
                    )}

                    {!loading && !error && filteredOptions.map(opt => (
                      <label
                        key={opt.value}
                        className="flex cursor-pointer items-center gap-2 px-3 py-2 text-sm hover:bg-(--color-table-row-hover)"
                      >
                        <Checkbox.Root
                          checked={value.includes(opt.value)}
                          onCheckedChange={() => toggle(opt.value)}
                          className="h-4 w-4 rounded border border-(--color-table-border)"
                        />
                        {opt.label}
                      </label>
                    ))}

                  </div>
                </div>
              )
            }
          </div >
        )
      }}
    />
  )
}
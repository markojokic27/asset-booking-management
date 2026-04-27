import { twMerge } from 'tailwind-merge';
import { FooterLogo } from '../icons/FooterLogo';
import {
  GlobeIcon,
  MobileIcon,
  EnvelopeClosedIcon,
} from '@radix-ui/react-icons';
import { useTranslation } from 'react-i18next';

type FooterProps = {
  className?: string;
};

export const Footer: React.FC<FooterProps> = ({ className }) => {
  const { t } = useTranslation();
  return (
    <footer
      className={twMerge(
        'z-10 border-t border-(--color-border) bg-(--color-surface) py-8 shadow-md md:ml-[25%] md:py-4',
        className
      )}
    >
      <div className="container mx-auto flex flex-col gap-4 px-4 sm:flex-row sm:items-center sm:justify-between md:px-6">
        <div className="flex min-w-0 flex-col items-center gap-2 sm:items-baseline">
          <a
            href="https://www.bundesdruckerei.de/en/careers/maurer-electronics-split"
            target="_blank"
            rel="noreferrer"
            className="inline-flex items-center gap-2 text-sm font-medium tracking-wide wrap-break-word text-(--color-table-text) underline-offset-4 transition-colors hover:text-(--color-primaryblue) hover:underline"
          >
            <GlobeIcon className="h-4 w-4" />
            {t('layout.footer.websiteLinkLabel')}
          </a>

          <div className="flex flex-col gap-1 text-sm text-(--color-table-text)">
            <div className="inline-flex w-fit items-center gap-2">
              <MobileIcon className="h-4 w-4" />
              +385 21279 130
            </div>
            <div className="inline-flex w-fit max-w-full items-center gap-2 wrap-break-word">
              <EnvelopeClosedIcon className="h-4 w-4" />
              info@maurer-electronics.hr
            </div>
          </div>
        </div>

        <div className="flex min-w-0 flex-col items-center gap-2 sm:items-end">
          <div className="text-sm text-(--color-table-text) md:mb-2 md:-translate-y-1">
            {t('layout.footer.copyright')}
          </div>
          <div className="flex items-center gap-3">
            <div className="min-w-0 text-left text-(--color-table-text)">
              <div className="text-[11px] leading-tight font-semibold tracking-[0.12em] text-(--color-table-text)/70 uppercase">
                {t('layout.footer.partOfThe')}
              </div>
              <div className="text-sm leading-tight font-medium">
                {t('layout.footer.groupName')}
              </div>
            </div>
            <FooterLogo className="h-8 w-auto shrink-0" aria-hidden="true" />
          </div>
        </div>
      </div>
    </footer>
  );
};

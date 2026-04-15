import { twMerge } from 'tailwind-merge';
import { FooterLogo } from '../icons/FooterLogo';
import { GlobeIcon, MobileIcon, EnvelopeClosedIcon } from '@radix-ui/react-icons';

type FooterProps = {
  className?: string;
};

export const Footer: React.FC<FooterProps> = ({ className }) => {
  return (
    <footer
      className={twMerge(
        'fixed bottom-0 right-0 left-0 z-10 py-4 md:left-[25%]',
        className
      )}
    >
      <div className="px-4 md:px-6">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div className="flex flex-col gap-2">
            <a
              href="https://www.bundesdruckerei.de/en/careers/maurer-electronics-split"
              target="_blank"
              rel="noreferrer"
              className="inline-flex w-fit items-center gap-2 text-sm font-medium tracking-wide text-(--color-table-text) underline-offset-4 transition-colors hover:text-(--color-primaryblue) hover:underline"
            >
              <GlobeIcon className="h-4 w-4" />
              Maurer Electronics Website
            </a>

            <div className="flex flex-col gap-1 text-sm text-(--color-table-text)">
              <div className="inline-flex w-fit items-center gap-2">
                <MobileIcon className="h-4 w-4" />
                +385 21279 130
              </div>
              <div className="inline-flex w-fit items-center gap-2">
                <EnvelopeClosedIcon className="h-4 w-4" />
                info@maurer-electronics.hr
              </div>
            </div>
          </div>

          <div className="flex flex-col items-start gap-2 md:items-end">
            <div className="text-sm text-(--color-table-text) md:-translate-y-1 md:mb-2">
              © 2026 | Bundesdruckerei GmbH
            </div>
            <div className="flex items-center gap-3">
              <div className="min-w-42.5 text-left text-(--color-table-text)">
                <div className="text-[11px] leading-tight font-semibold tracking-[0.12em] text-(--color-table-text)/70 uppercase">
                  Part of the
                </div>
                <div className="text-sm leading-tight font-medium">
                  Bundesdruckerei Group
                </div>
              </div>
              <FooterLogo className="h-8 w-auto shrink-0" aria-hidden="true" />
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

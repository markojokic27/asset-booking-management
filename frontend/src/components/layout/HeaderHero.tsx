import { useTranslation } from 'react-i18next';

export const HeaderHero: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...rest
}) => {
  const { t } = useTranslation();

  return (
    <div className={className} {...rest}>
      <h1 className="text-5xl font-bold tracking-[0.2em] xl:text-6xl">
        {t('layout.headerHero.title')}
      </h1>
      <div className="mt-10 flex w-full">
        <div className="h-2 w-1/2 bg-[#00D097]" />
        <div className="h-2 w-1/2 bg-[#030043]" />
      </div>
      <p className="mt-10 w-full text-2xl xl:text-3xl">
        {t('layout.headerHero.descriptionLine1')}
        <br /> {t('layout.headerHero.descriptionLine2')}
        <br /> {t('layout.headerHero.descriptionLine3')}
      </p>
    </div>
  );
};

export const HeaderHero: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...rest
}) => (
  <div className={className} {...rest}>
    <h1 className="text-6xl font-bold tracking-[0.2em]">
      Asset Booking Management
    </h1>
    <div className="mt-10 flex w-full">
      <div className="h-2 w-1/3 bg-black" />
      <div className="h-2 w-1/3 bg-[#e60037]" />
      <div className="h-2 w-1/3 bg-[#ffc300]" />
    </div>
    <p className="mt-10 w-full text-3xl">
      Simple powerful web and mobile software
      <br /> for asset booking management of
      <br /> Maurer workplace assets
    </p>
  </div>
);

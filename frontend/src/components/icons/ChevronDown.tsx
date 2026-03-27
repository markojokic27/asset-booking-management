export const ChevronDown: React.FC<React.ComponentPropsWithoutRef<'svg'>> = ({
  ...rest
}) => (
  <svg
    {...rest}
    viewBox="0 0 24 24"
    fill="currentColor"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fillRule="evenodd"
      clipRule="evenodd"
      d="m5.47 9.265 1.06-1.06 5.47 5.47 5.47-5.47 1.06 1.06-6.53 6.53-6.53-6.53Z"
      fill="currentColor"
    />
  </svg>
);

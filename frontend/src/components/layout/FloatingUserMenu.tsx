import UserMenu from '../ui/UserMenu';

export default function FloatingUserMenu() {
  return (
    <div className="fixed top-20 right-6 z-50 hidden rounded-xs bg-(--color-surface) p-2 pr-6 shadow-md md:right-0 md:block">
      <UserMenu />
    </div>
  );
}

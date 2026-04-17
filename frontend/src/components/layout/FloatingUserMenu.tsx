import UserMenu from "../ui/UserMenu";

export default function FloatingUserMenu() {
  return (
    <div className="fixed top-20 right-6 z-50 rounded-xs bg-(--color-surface) p-2 shadow-md md:right-0 pr-6">
      <UserMenu />
    </div>
  );
}
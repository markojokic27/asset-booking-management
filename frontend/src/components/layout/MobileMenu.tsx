import * as Dialog from '@radix-ui/react-dialog';
import { Cross2Icon, HamburgerMenuIcon } from '@radix-ui/react-icons';

export default function MobileMenu() {
  //TD: linkovi, animacija zatvaranja, theme switcher, language switcher
  return (
    <Dialog.Root>
      <Dialog.Trigger asChild>
        <button className="p-2 hover:cursor-pointer md:hidden">
          <HamburgerMenuIcon width={24} height={24} />
        </button>
      </Dialog.Trigger>

      <Dialog.Portal>
        <Dialog.Overlay className="fixed inset-0 z-40 bg-(--bg-card)" />

        <Dialog.Content className="animate-slideIn fixed top-0 left-0 z-50 h-full w-64 bg-white p-6 shadow-lg">
          <Dialog.Close asChild>
            <button className="absolute top-4 right-4 hover:cursor-pointer">
              <Cross2Icon />
            </button>
          </Dialog.Close>

          <nav className="mt-8 flex flex-col gap-4">
            <a href="#">Home</a>
            <a href="#">About</a>
            <a href="#">Contact</a>
          </nav>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}

import * as DropdownMenu from '@radix-ui/react-dropdown-menu';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { useNavigate } from 'react-router-dom';
import { ChevronDown } from '../icons/ChevronDown';
import { useTranslation } from 'react-i18next';


function UserMenu() {
  const navigate = useNavigate();
  const { t } = useTranslation();

  return (
    <DropdownMenu.Root modal={false}>
      <DropdownMenu.Trigger asChild>
        <button
          type="button"
          aria-label={t('ui.userMenu.ariaLabel')}
          className="group flex items-center gap-1.5 text-gray-900 hover:cursor-pointer focus:outline-none dark:text-gray-100"
        >
          <AccountCircleOutlinedIcon sx={{ fontSize: 32}} />
          <ChevronDown className="h-5 w-5 shrink-0 transition-transform duration-300 ease-in-out group-data-[state=open]:rotate-180" />
        </button>
      </DropdownMenu.Trigger>

      <DropdownMenu.Content
        align="end"
        className="mt-1 min-w-48 rounded border border-gray-200 bg-white text-gray-900 shadow dark:border-gray-700 dark:bg-gray-900 dark:text-gray-100"
      >
        <DropdownMenu.Item
          onSelect={() => navigate('/account-info')}
          className="cursor-pointer px-4 py-2 hover:bg-gray-100 hover:outline-none dark:hover:bg-gray-800"
        >
          <span className="flex items-center gap-2">
            <VisibilityOutlinedIcon fontSize="small" />
            <span className="text-sm">{t('ui.userMenu.accountInfo')}</span>
          </span>
        </DropdownMenu.Item>

        <DropdownMenu.Separator className="h-px bg-gray-200 dark:bg-gray-700" />

        <DropdownMenu.Item
            onSelect={() => navigate('/login')}
            className="cursor-pointer px-4 py-2 text-red-600 hover:bg-gray-100 hover:outline-none dark:text-red-400 dark:hover:bg-gray-800"
        >
            <span className="flex items-center gap-2">
                <LogoutOutlinedIcon fontSize="small" />
                <span className="text-sm">{t('ui.userMenu.logout')}</span>
            </span>
        </DropdownMenu.Item>
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  );
}

export default UserMenu;
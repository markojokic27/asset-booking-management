// External packages
import { useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as Form from '@radix-ui/react-form';
import { useTranslation } from 'react-i18next';

// Components
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { createUserValidationSchema } from '../../user/validation';

// Context
import { useAuth } from '../context/AuthContext';

// API
import api from '../../../shared/api';

const LoginForm = () => {
  const { login } = useAuth();
  const [errors, setErrors] = useState({
    username: '',
    password: '',
  });
  const [serverError, setServerError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const { t } = useTranslation();
  const loginSchema = useMemo(
    () =>
      createUserValidationSchema(t).pick({
        username: true,
        password: true,
      }),
    [t]
  );

  const handleLogin = async (username: string, password: string) => {
    try {
      setLoading(true);
      setServerError('');

      const response = await api.post('/auth/login', {
        username,
        password,
      });

      const { accessToken, refreshToken } = response.data;

      await login(accessToken, refreshToken);

      navigate('/');
    } catch (error: any) {
      if (error.response) {
        setServerError(
          error.response.data?.message ??
            t('ui.login.errors.invalidCredentials')
        );
      } else {
        setServerError(t('ui.login.errors.serverError'));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (data: FormData) => {
    setErrors({ username: '', password: '' });
    setServerError('');

    const username = data.get('username') as string;
    const password = data.get('password') as string;

    const result = loginSchema.safeParse({ username, password });

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;

      setErrors({
        username: fieldErrors.username?.[0] || '',
        password: fieldErrors.password?.[0] || '',
      });
      return;
    }

    handleLogin(result.data.username, result.data.password);
  };

  return (
    <Form.Root
      onSubmit={(event) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        handleSubmit(formData);
      }}
      className="relative flex w-full flex-col overflow-hidden rounded-2xl bg-(--color-table-surface) px-5 py-10 shadow-(--shadow-card) sm:px-12 md:mt-0 md:px-12 lg:px-20"
    >
      <h1 className="mb-6 text-center text-6xl font-black text-gray-900 dark:text-gray-100">
        {t('ui.login.title')}
      </h1>
      <h2 className="mb-6 text-center font-bold">{t('ui.login.subtitle')}</h2>
      <p className="mb-2 tracking-[0.2em]">{t('ui.login.fields.username')}</p>
      <Form.Field name="username" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="username"
            type="text"
            placeholder={t('ui.login.placeholders.username')}
            className="w-full border p-3"
            error={!!errors.username}
            errorMessage={errors.username}
          />
        </Form.Control>
      </Form.Field>
      <p className="mb-2 tracking-[0.2em]">{t('ui.login.fields.password')}</p>
      <Form.Field name="password" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="password"
            type="password"
            placeholder={t('ui.login.placeholders.password')}
            className="w-full border p-3"
            error={!!errors.password}
            errorMessage={errors.password}
          />
        </Form.Control>
      </Form.Field>

      <Form.Submit asChild>
        <Button
          data-testid="login-button"
          type="submit"
          className="mt-2 mb-8 font-bold uppercase"
          disabled={loading}
        >
          {loading ? t('ui.login.loading') : t('ui.login.submit')}
        </Button>
      </Form.Submit>
      {serverError && (
        <p className="absolute bottom-8 self-center text-center font-semibold text-red-500">
          {serverError}
        </p>
      )}
      {/* <Button variant="link" onClick={() => navigate('/register')}>
        {t('ui.login.registerCta')}
      </Button> */}
    </Form.Root>
  );
};

export default LoginForm;

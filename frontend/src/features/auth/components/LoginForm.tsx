import { useState } from 'react';
import * as Form from '@radix-ui/react-form';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { userValidationSchema } from '../../user/validation';
import { useNavigate } from 'react-router-dom';
import api, { setAccessToken } from '../api/login';

const loginSchema = userValidationSchema.pick({
  username: true,
  password: true,
});

const LoginForm = () => {
  const [errors, setErrors] = useState({
    username: '',
    password: '',
  });
  const [serverError, setServerError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleLogin = async (username: string, password: string) => {
    try {
      setLoading(true);
      setServerError('');

      const response = await api.post('/auth/login', {
        username,
        password,
      });

      const { accessToken, refreshToken, username: user, role } = response.data;

      setAccessToken(accessToken);
      localStorage.setItem('refreshToken', refreshToken);

      localStorage.setItem('username', user);
      localStorage.setItem('role', role);

      navigate('/');
    } catch (error: any) {
      if (error.response) {
        setServerError(
          error.response.data?.message || 'Pogrešan username ili password'
        );
      } else {
        setServerError('Greška na serveru.');
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
      className="flex w-full flex-col bg-white px-5 py-10 shadow-(--shadow-card) sm:px-12 md:mt-0 md:px-12 lg:px-20 dark:bg-black"
    >
      <h1 className="mb-6 text-center text-6xl font-black text-gray-900 dark:text-gray-100">
        Login
      </h1>
      <h2 className="mb-6 text-center font-bold">
        Welcome to log in to your asset booking management
      </h2>
      <p className="mb-2 tracking-[0.2em]">Username</p>
      <Form.Field name="username" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="username"
            type="text"
            placeholder="Eneter your username"
            className="w-full border p-3"
            error={!!errors.username}
            errorMessage={errors.username}
          />
        </Form.Control>
      </Form.Field>
      <p className="mb-2 tracking-[0.2em]">Password</p>
      <Form.Field name="password" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="password"
            type="password"
            placeholder="Eneter your password"
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
          className="mt-6 mb-2 font-bold uppercase"
          disabled={loading}
        >
          {loading ? 'Loading...' : 'Login'}
        </Button>
      </Form.Submit>
      {serverError && (
        <p className="mb-4 text-center font-semibold text-red-500">
          {/* TODO uredit triba absolute da se ne minja vsina ili da je tu al ne vidljiv */}
          {serverError}
        </p>
      )}
      <Button variant="link" onClick={() => navigate('/register')}>
        Don't have an account? Register here.
      </Button>
    </Form.Root>
  );
};

export default LoginForm;

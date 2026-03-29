import { useState } from 'react';
import * as Form from '@radix-ui/react-form';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { userValidationSchema } from '../../user/validation';
import { useNavigate } from 'react-router-dom';

const loginSchema = userValidationSchema.pick({
  username: true,
  password: true,
});

const LoginForm = () => {
  const [errors, setErrors] = useState({
    username: '',
    password: '',
  });

  const navigate = useNavigate();

  /*   const handleLogin = async () => {
    await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      credentials: 'include',
    });

    navigate('/');
  };
 */

  const handleSubmit = (data: FormData) => {
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

    document.cookie = 'auth=true; path=/';
    navigate('/');
    console.log('Login uspješan', result.data);
  };

  return (
    <Form.Root
      onSubmit={(event) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        handleSubmit(formData);
      }}
      className="dark:gray-800 flex w-full flex-col gap-10"
    >
      <Form.Field name="username">
        <Form.Control asChild>
          <Input
            type="text"
            placeholder="Username"
            className="w-full rounded-lg border p-3"
            error={!!errors.username}
            errorMessage={errors.username}
          />
        </Form.Control>
      </Form.Field>

      <Form.Field name="password">
        <Form.Control asChild>
          <Input
            type="password"
            placeholder="Password"
            className="w-full rounded-lg border p-3"
            error={!!errors.password}
            errorMessage={errors.password}
          />
        </Form.Control>
      </Form.Field>

      <Form.Submit asChild>
        <Button type="submit">Login</Button>
      </Form.Submit>
    </Form.Root>
  );
};

export default LoginForm;

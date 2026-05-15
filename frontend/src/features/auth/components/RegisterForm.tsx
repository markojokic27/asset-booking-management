import * as Form from '@radix-ui/react-form';
import { useMemo, useState } from 'react';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { createUserValidationSchema } from '../../user/validation';
import { useNavigate } from 'react-router';
import { useTranslation } from 'react-i18next';

const RegisterForm = () => {
  const [errors, setErrors] = useState({
    username: '',
    password: '',
    name: '',
    surname: '',
  });

  const navigate = useNavigate();
  const { t } = useTranslation();
  const registerSchema = useMemo(
    () =>
      createUserValidationSchema(t).pick({
        username: true,
        password: true,
        name: true,
        surname: true,
      }),
    [t],
  );

  const handleSubmit = async (data: FormData) => {
    const formData = {
      username: data.get('username') as string,
      password: data.get('password') as string,
      name: data.get('name') as string,
      surname: data.get('surname') as string,
    };

    const result = registerSchema.safeParse(formData);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;

      setErrors({
        username: fieldErrors.username?.[0] || '',
        password: fieldErrors.password?.[0] || '',
        name: fieldErrors.name?.[0] || '',
        surname: fieldErrors.surname?.[0] || '',
      });

      return;
    }

    navigate('/login');

    /*try {
      setIsLoading(true);

      await registerUser(result.data);

      console.log('Register uspješan');

      navigate('/login');
    } catch (err: any) {
      console.error(err);

      setErrors((prev) => ({
        ...prev,
        username: err.message || 'Greška na serveru',
      }));
    } finally {
      setIsLoading(false);
    }*/
  };

  return (
    <Form.Root
      onSubmit={(event) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        handleSubmit(formData);
      }}
      className="flex w-full flex-col overflow-hidden rounded-2xl bg-(--color-table-surface) px-6 py-10 shadow-(--shadow-card) sm:px-12 md:mt-0 md:px-12 lg:px-20"
    >
      <h1 className="mb-6 text-center text-6xl font-black text-gray-900 dark:text-gray-100">
        Register
      </h1>
      <h2 className="mb-6 text-center font-bold">
        Welcome to asset booking management
      </h2>

      {/* NAME */}
      <p className="mb-2 tracking-[0.2em]">Name</p>
      <Form.Field name="name" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="name"
            placeholder="Enter your name"
            error={!!errors.name}
            errorMessage={errors.name}
          />
        </Form.Control>
      </Form.Field>

      {/* SURNAME */}
      <p className="mb-2 tracking-[0.2em]">Surname</p>
      <Form.Field name="surname" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="surname"
            placeholder="Enter your surname"
            error={!!errors.surname}
            errorMessage={errors.surname}
          />
        </Form.Control>
      </Form.Field>

      {/* USERNAME */}
      <p className="mb-2 tracking-[0.2em]">Username</p>
      <Form.Field name="username" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="username"
            placeholder="Enter username"
            error={!!errors.username}
            errorMessage={errors.username}
          />
        </Form.Control>
      </Form.Field>

      {/* PASSWORD */}
      <p className="mb-2 tracking-[0.2em]">Password</p>
      <Form.Field name="password" className="mb-10 w-full md:mb-12">
        <Form.Control asChild>
          <Input
            data-testid="password"
            type="password"
            placeholder="Enter password"
            error={!!errors.password}
            errorMessage={errors.password}
          />
        </Form.Control>
      </Form.Field>

      <Form.Submit asChild>
        <Button
          data-testid="register-button"
          type="submit"
          className="mt-6 mb-2 font-bold uppercase"
        >
          Register
        </Button>
      </Form.Submit>

      {/* LINK NA LOGIN */}
      <Button variant="link" onClick={() => navigate('/login')}></Button>
    </Form.Root>
  );
};

export default RegisterForm;

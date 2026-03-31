import * as Form from '@radix-ui/react-form';
import { useState } from 'react';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { userValidationSchema } from '../../user/validation';
import { useNavigate } from 'react-router-dom';
//import { registerUser } from '../api/authApi';

const registerSchema = userValidationSchema.pick({
  username: true,
  password: true,
  name: true,
  surname: true,
});

const RegisterForm = () => {
  const [errors, setErrors] = useState({
    username: '',
    password: '',
    name: '',
    surname: '',
  });

  const [isLoading] = useState(false);

  const navigate = useNavigate();

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
      className="flex w-full flex-col bg-white px-6 py-10 shadow-(--shadow-card) sm:px-12 md:mt-30 md:px-12 lg:px-20 dark:bg-black"
    >
      <h1 className="mb-6 text-center text-6xl font-black text-gray-900 dark:text-gray-100">
        Register
      </h1>

      {/* NAME */}
      <p className="mb-2">Name</p>
      <Form.Field name="name" className="mb-6">
        <Form.Control asChild>
          <Input
            placeholder="Enter your name"
            error={!!errors.name}
            errorMessage={errors.name}
          />
        </Form.Control>
      </Form.Field>

      {/* SURNAME */}
      <p className="mb-2">Surname</p>
      <Form.Field name="surname" className="mb-6">
        <Form.Control asChild>
          <Input
            placeholder="Enter your surname"
            error={!!errors.surname}
            errorMessage={errors.surname}
          />
        </Form.Control>
      </Form.Field>

      {/* USERNAME */}
      <p className="mb-2">Username</p>
      <Form.Field name="username" className="mb-6">
        <Form.Control asChild>
          <Input
            placeholder="Enter username"
            error={!!errors.username}
            errorMessage={errors.username}
          />
        </Form.Control>
      </Form.Field>

      {/* PASSWORD */}
      <p className="mb-2">Password</p>
      <Form.Field name="password" className="mb-6">
        <Form.Control asChild>
          <Input
            type="password"
            placeholder="Enter password"
            error={!!errors.password}
            errorMessage={errors.password}
          />
        </Form.Control>
      </Form.Field>

      <Form.Submit asChild>
        <Button
          type="submit"
          className="mt-6 font-bold uppercase"
          disabled={isLoading}
        >
          {isLoading ? 'Loading...' : 'Register'}
        </Button>
      </Form.Submit>

      {/* LINK NA LOGIN */}
      <Button
        variant="link"
        type="button"
        onClick={() => navigate('/login')}
        className="mt-4"
      >
        Već imaš račun? Login
      </Button>
    </Form.Root>
  );
};

export default RegisterForm;

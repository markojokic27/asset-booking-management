  import { useState } from 'react';
  import { Input } from '../../../components/ui/Input';
  import { Button } from '../../../components/ui/Button';
  import { userValidationSchema } from '../../user/validation';


  const loginSchema = userValidationSchema.pick({
    username: true,
    password: true,
  });

  const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({
      username: '',
      password: '',
    });

    const handleSubmit = (e: any) => {
  e.preventDefault();

  const result = loginSchema.safeParse({
    username,
    password,
  });

  if (!result.success) {
    const fieldErrors = result.error.flatten().fieldErrors;

    setErrors({
      username: fieldErrors.username?.[0] || "",
      password: fieldErrors.password?.[0] || "",
    });

    return;
  }

  console.log("Login uspješan", result.data);
};

    return (
      <form onSubmit={handleSubmit}>
        <Input
          name="username"
          placeholder="Username"
          value={username}
          onChange={(e) => {
            setUsername(e.target.value);
            setErrors((prev) => ({
              ...prev,
              username: '',
            }));
          }}
        />
        {errors.username && <p style={{ color: 'red' }}>{errors.username}</p>}

        <Input
          type="password"
          name="password"
          placeholder="Password"
          value={password}
          onChange={(e) => {
            setPassword(e.target.value);
            setErrors((prev) => ({
              ...prev,
              password: '',
            }));
          }}
        />
        {errors.password && <p style={{ color: 'red' }}>{errors.password}</p>}

        <Button type="submit">Login</Button>
      </form>
    );
  };

  export default LoginForm;

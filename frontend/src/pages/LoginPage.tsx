import { useState } from 'react';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';

const MAX_USERNAME_LENGTH = 20;
const MAX_PASSWORD_LENGTH = 50;

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const [errors, setErrors] = useState({
    username: '',
    password: '',
  });

  const handleSubmit = (e: any) => {
    e.preventDefault();

    const newErrors = {
      username: '',
      password: '',
    };

    let isValid = true;

    if (!username.trim()) {
      newErrors.username = 'Username je obavezan';
      isValid = false;
    } else if (username.length > MAX_USERNAME_LENGTH) {
      newErrors.username = `Username ne smije biti duži od ${MAX_USERNAME_LENGTH} znakova`;
      isValid = false;
    }

    if (!password.trim()) {
      newErrors.password = 'Password je obavezan';
      isValid = false;
    } else if (password.length < 6) {
      newErrors.password = 'Password mora imati barem 6 znakova';
      isValid = false;
    } else if (password.length > MAX_PASSWORD_LENGTH) {
      newErrors.password = `Password ne smije biti duži od ${MAX_PASSWORD_LENGTH} znakova`;
      isValid = false;
    }

    setErrors(newErrors);

    if (!isValid) return;

    console.log('Login uspješan', {
      username,
      password,
    });
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

export default LoginPage;

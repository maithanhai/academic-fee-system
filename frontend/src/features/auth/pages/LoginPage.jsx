import { Card } from "antd";
import useLogin from "../hooks/useLogin";
import LoginForm from "../components/LoginForm";

const LoginPage = () => {
  const { loading, submit } = useLogin();
  return (
    <Card title="Đăng nhập" style={{ maxWidth: 500, margin: "50px auto" }}>
      <LoginForm loading={loading} onSubmit={submit} />
    </Card>
  );
};

export default LoginPage;

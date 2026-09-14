import { Button, Form, Input } from 'antd';

const LoginForm = ({ loading, onSubmit }) => (
  <Form name="login" layout="vertical" onFinish={onSubmit} autoComplete="off">
    <Form.Item name="username" label="Tài khoản" rules={[{ required: true, message: 'Vui lòng nhập tài khoản!' }]}>
      <Input />
    </Form.Item>
    <Form.Item name="password" label="Mật khẩu" rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}>
      <Input.Password />
    </Form.Item>
    <Button type="primary" htmlType="submit" loading={loading} block>Đăng nhập</Button>
  </Form>
);

export default LoginForm;

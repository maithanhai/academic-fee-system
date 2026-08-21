import { useState } from 'react';
import { Button, Form, Input, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux'; 

import authApi from '../../api/authApi';
import { setCredentials } from '../../store/slices/authSlice';
import { ROLES } from '../../configs/roles';

const LoginPage = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await authApi.login({
        username: values.username,
        password: values.password,
      });

      const loginData = response.data.data

      dispatch(setCredentials(loginData));
      message.success('Đăng nhập thành công!');

      if (loginData.role === ROLES.ADMIN) navigate('/admin/dashboard');
      else if (loginData.role === ROLES.TEACHER) navigate('/teacher/dashboard');
      else if (loginData.role === ROLES.STUDENT) navigate('/student/notification');

    } catch (error) {
      console.error("Lỗi đăng nhập: ", error);
      message.error('Sai tài khoản hoặc mật khẩu!');
    } finally {
      setLoading(false);
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <Form
      name="basic"
      labelCol={{ span: 8 }}
      wrapperCol={{ span: 16 }}
      style={{ maxWidth: 600, margin: '50px auto' }}
      initialValues={{ remember: true }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      autoComplete="off"
    >
      <Form.Item
        label="Tài khoản"
        name="username"
        rules={[{ required: true, message: 'Vui lòng nhập tài khoản!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Mật khẩu"
        name="password"
        rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
      >
        <Input.Password />
      </Form.Item>

      <Form.Item label={null} wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="primary" htmlType="submit" loading={loading}>
          Đăng nhập
        </Button>
      </Form.Item>
    </Form>
  );
};

export default LoginPage;
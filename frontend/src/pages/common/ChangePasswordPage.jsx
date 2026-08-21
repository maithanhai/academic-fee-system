import { Form, Input, Button, Card, message } from 'antd';
import { useState } from 'react';
import userApi from '../../api/userApi';

const ChangePasswordPage = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      await userApi.password({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword
      });
      
      message.success('Đổi mật khẩu thành công!');
      
      form.resetFields(); 
    } catch (error) {
      console.error("Lỗi đổi mật khẩu:", error);
      const errorMsg = error.response?.data?.message || 'Lỗi khi đổi mật khẩu. Vui lòng thử lại!';
      message.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '40px' }}>
      <Card 
        title={<span style={{ fontSize: '20px' }}>Đổi Mật Khẩu</span>} 
        style={{ width: '100%', maxWidth: '500px' }} 
        className="shadow-sm"
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={onFinish}
        >
          <Form.Item
            name="oldPassword"
            label="Mật khẩu hiện tại"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu hiện tại!' }]}
          >
            <Input.Password placeholder="Nhập mật khẩu hiện tại" size="large" />
          </Form.Item>

          <Form.Item
            name="newPassword"
            label="Mật khẩu mới"
            rules={[
              { required: true, message: 'Vui lòng nhập mật khẩu mới!' },
              { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' } 
            ]}
          >
            <Input.Password placeholder="Nhập mật khẩu mới" size="large" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="Xác nhận mật khẩu mới"
            dependencies={['newPassword']} 
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu mới!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('newPassword') === value) {
                    return Promise.resolve(); 
                  }
                  return Promise.reject(new Error('Mật khẩu xác nhận không khớp!')); 
                },
              }),
            ]}
          >
            <Input.Password placeholder="Nhập lại mật khẩu mới" size="large" />
          </Form.Item>

          <Form.Item style={{ marginTop: 32, marginBottom: 0 }}>
            <Button type="primary" htmlType="submit" loading={loading} block size="large">
              Cập nhật mật khẩu
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default ChangePasswordPage;
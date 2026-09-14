import { Form, Input, Button } from 'antd';

const ChangePasswordForm = ({ form, loading, onSubmit }) => (
  <Form form={form} layout="vertical" onFinish={onSubmit}>
    <Form.Item name="oldPassword" label="Mật khẩu hiện tại" rules={[{ required: true }]}><Input.Password /></Form.Item>
    <Form.Item name="newPassword" label="Mật khẩu mới" rules={[{ required: true }, { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' }]}><Input.Password /></Form.Item>
    <Form.Item name="confirmPassword" label="Xác nhận mật khẩu mới" dependencies={['newPassword']} rules={[{ required: true }, ({ getFieldValue }) => ({ validator(_, value) { return !value || getFieldValue('newPassword') === value ? Promise.resolve() : Promise.reject(new Error('Mật khẩu xác nhận không khớp!')); } })]}><Input.Password /></Form.Item>
    <Button type="primary" htmlType="submit" loading={loading} block>Cập nhật mật khẩu</Button>
  </Form>
);

export default ChangePasswordForm;

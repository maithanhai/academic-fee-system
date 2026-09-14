import { Form, Input, Button } from 'antd';

const ProfileContactForm = ({ form, isStudent, updating, onSubmit }) => (
  <Form form={form} layout="vertical" onFinish={onSubmit}>
    <Form.Item name="email" label="Địa chỉ Email" rules={[{ required: true }, { type: 'email', message: 'Email không đúng định dạng!' }]}><Input placeholder="Ví dụ: hs11@edu.vn" /></Form.Item>
    <Form.Item name="phone" label="Số điện thoại cá nhân" rules={[{ required: true }]}><Input placeholder="Ví dụ: 0901xxx" /></Form.Item>
    {isStudent && <>
      <Form.Item name="address" label="Địa chỉ thường trú"><Input placeholder="Ví dụ: HCM" /></Form.Item>
      <Form.Item name="phoneParent" label="Số điện thoại Phụ huynh"><Input placeholder="SĐT người thân để liên hệ" /></Form.Item>
    </>}
    <Button type="primary" htmlType="submit" loading={updating} block>Lưu thay đổi</Button>
  </Form>
);

export default ProfileContactForm;

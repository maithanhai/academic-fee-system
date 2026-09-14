import { Form, Input, Button, Row, Col, Select, DatePicker, Space } from 'antd';

const TeacherRegistrationForm = ({ form, departments, subjects, creating, onReset, onSubmit }) => (
  <Form form={form} layout="vertical" onFinish={onSubmit}>
    <Row gutter={16}>
      <Col xs={24} md={12}><Form.Item name="fullName" label="Họ và tên" rules={[{ required: true }]}><Input /></Form.Item></Col>
      <Col xs={24} md={12}><Form.Item name="email" label="Địa chỉ Email" rules={[{ required: true }, { type: 'email' }]}><Input /></Form.Item></Col>
    </Row>
    <Row gutter={16}>
      <Col xs={24} md={12}><Form.Item name="phone" label="Số điện thoại"><Input /></Form.Item></Col>
      <Col xs={24} md={12}><Form.Item name="dateOfBirth" label="Ngày sinh" rules={[{ required: true }]}><DatePicker style={{ width: '100%' }} format="DD-MM-YYYY" /></Form.Item></Col>
    </Row>
    <Row gutter={16}>
      <Col xs={24} md={12}><Form.Item name="gender" label="Giới tính" rules={[{ required: true }]}><Select options={[{ value: 'MALE', label: 'Nam' }, { value: 'FEMALE', label: 'Nữ' }]} /></Form.Item></Col>
      <Col xs={24} md={12}><Form.Item name="departmentId" label="Tổ / Bộ môn" rules={[{ required: true }]}><Select options={departments.map((item) => ({ value: item.id, label: item.name }))} /></Form.Item></Col>
    </Row>
    <Form.Item name="subjectIds" label="Các môn giảng dạy" rules={[{ required: true }]}><Select mode="multiple" options={subjects.map((item) => ({ value: item.id, label: item.name }))} /></Form.Item>
    <Form.Item style={{ textAlign: 'right' }}><Space><Button onClick={onReset}>Làm mới</Button><Button type="primary" htmlType="submit" loading={creating}>Tạo tài khoản</Button></Space></Form.Item>
  </Form>
);

export default TeacherRegistrationForm;

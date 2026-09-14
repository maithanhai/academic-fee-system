import { Form, Input, Button, Row, Col, Select, DatePicker, Switch, Space } from 'antd';

const UserEditForm = ({ form, isTeacher, departments, subjects, updating, onFinish, onReset }) => (
  <Form form={form} layout="vertical" onFinish={onFinish}>
    <Row gutter={16}>
      <Col span={12}><Form.Item name="fullName" label="Họ và tên" rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}><Input placeholder="Nhập họ và tên" /></Form.Item></Col>
      <Col span={12}><Form.Item name="email" label="Địa chỉ Email" rules={[{ required: true, message: 'Vui lòng nhập email!' }, { type: 'email', message: 'Email không hợp lệ!' }]}><Input placeholder="Nhập email" /></Form.Item></Col>
    </Row>
    <Row gutter={16}>
      <Col span={12}><Form.Item name="phone" label="Số điện thoại"><Input placeholder="Nhập số điện thoại" /></Form.Item></Col>
      <Col span={12}><Form.Item name="dateOfBirth" label="Ngày sinh" rules={[{ required: true, message: 'Vui lòng chọn ngày sinh!' }]}><DatePicker style={{ width: '100%' }} format="DD-MM-YYYY" /></Form.Item></Col>
    </Row>
    <Row gutter={16}>
      <Col span={12}><Form.Item name="gender" label="Giới tính" rules={[{ required: true, message: 'Vui lòng chọn giới tính!' }]}><Select options={[{ value: 'MALE', label: 'Nam' }, { value: 'FEMALE', label: 'Nữ' }]} /></Form.Item></Col>
      <Col span={12}><Form.Item name="active" label="Trạng thái tài khoản" valuePropName="checked"><Switch checkedChildren="Hoạt động" unCheckedChildren="Vô hiệu hóa" /></Form.Item></Col>
    </Row>
    {!isTeacher && <>
      <Form.Item name="address" label="Địa chỉ thường trú"><Input placeholder="Nhập địa chỉ" /></Form.Item>
      <Form.Item name="phoneParent" label="Số điện thoại Phụ huynh"><Input placeholder="Nhập SĐT người thân" /></Form.Item>
    </>}
    {isTeacher && <Row gutter={16}>
      <Col span={12}><Form.Item name="departmentId" label="Tổ / Bộ môn" rules={[{ required: true, message: 'Vui lòng chọn bộ môn!' }]}><Select options={departments.map((department) => ({ value: department.id, label: department.name }))} /></Form.Item></Col>
      <Col span={12}><Form.Item name="subjectIds" label="Các môn giảng dạy"><Select mode="multiple" allowClear options={subjects.map((subject) => ({ value: subject.id, label: subject.name }))} /></Form.Item></Col>
    </Row>}
    <Form.Item style={{ marginTop: 24, marginBottom: 0, textAlign: 'right' }}>
      <Space><Button onClick={onReset}>Đặt lại</Button><Button type="primary" htmlType="submit" loading={updating}>Lưu thay đổi</Button></Space>
    </Form.Item>
  </Form>
);

export default UserEditForm;

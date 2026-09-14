import { Form, Input, Button, Row, Col, Select, DatePicker, Space } from "antd";

const StudentRegistrationForm = ({
  form,
  cohorts,
  creating,
  onReset,
  onSubmit,
}) => (
  <Form form={form} layout="vertical" onFinish={onSubmit}>
    <Row gutter={16}>
      <Col xs={24} md={12}>
        <Form.Item
          name="fullName"
          label="Họ và tên"
          rules={[{ required: true, message: "Vui lòng nhập họ tên!" }]}
        >
          <Input />
        </Form.Item>
      </Col>
      <Col xs={24} md={12}>
        <Form.Item
          name="dateOfBirth"
          label="Ngày sinh"
          rules={[{ required: true, message: "Vui lòng chọn ngày sinh!" }]}
        >
          <DatePicker style={{ width: "100%" }} format="DD-MM-YYYY" />
        </Form.Item>
      </Col>
    </Row>
    <Row gutter={16}>
      <Col xs={24} md={12}>
        <Form.Item name="gender" label="Giới tính" rules={[{ required: true }]}>
          <Select
            options={[
              { value: "MALE", label: "Nam" },
              { value: "FEMALE", label: "Nữ" },
            ]}
          />
        </Form.Item>
      </Col>
      <Col xs={24} md={12}>
        <Form.Item
          name="cohortId"
          label="Khóa học"
          rules={[{ required: true }]}
        >
          <Select
            options={cohorts.map((item) => ({
              value: item.id,
              label: item.name,
            }))}
          />
        </Form.Item>
      </Col>
    </Row>
    <Form.Item style={{ textAlign: "right" }}>
      <Space>
        <Button onClick={onReset}>Làm mới</Button>
        <Button type="primary" htmlType="submit" loading={creating}>
          Tạo tài khoản
        </Button>
      </Space>
    </Form.Item>
  </Form>
);

export default StudentRegistrationForm;

import {
  Modal,
  Form,
  Input,
  Button,
  DatePicker,
  InputNumber,
  Select,
  Switch,
} from "antd";

const FeeFormModal = ({
  open,
  form,
  editing,
  submitting,
  onCancel,
  onSubmit,
  academicYears = [],
}) => (
  <Modal
    title={editing ? "Sửa Khoản Phí" : "Tạo Khoản Phí Mới"}
    open={open}
    onCancel={onCancel}
    footer={null}
    destroyOnHidden
  >
    <Form form={form} layout="vertical" onFinish={onSubmit}>
      <Form.Item
        name="name"
        label="Tên khoản phí"
        rules={[{ required: true, message: "Vui lòng nhập tên!" }]}
      >
        <Input placeholder="Ví dụ: Học phí học kỳ 1" />
      </Form.Item>
      <Form.Item
        name="feeAmount"
        label="Số tiền (VNĐ)"
        rules={[{ required: true, message: "Vui lòng nhập số tiền!" }]}
      >
        <InputNumber style={{ width: "100%" }} min={0} />
      </Form.Item>
      <Form.Item
        name="dueDate"
        label="Hạn đóng"
        rules={[{ required: true, message: "Vui lòng chọn hạn đóng!" }]}
      >
        <DatePicker format="DD/MM/YYYY" style={{ width: "100%" }} />
      </Form.Item>
      <Form.Item
        name="academicYearId"
        label="Năm học"
        rules={[{ required: true, message: "Vui lòng chọn năm học!" }]}
      >
        <Select
          options={academicYears.map(year=>({
                value: year.id,
                label:year.name
              }))}
          placeholder="Chọn năm học"
        />
      </Form.Item>
      <Form.Item name="active" label="Trạng thái" valuePropName="checked">
        <Switch checkedChildren="Hoạt động" unCheckedChildren="Tắt" />
      </Form.Item>
      <div style={{ textAlign: "right" }}>
        <Button onClick={onCancel} style={{ marginRight: 8 }}>
          Hủy
        </Button>
        <Button type="primary" htmlType="submit" loading={submitting}>
          Lưu lại
        </Button>
      </div>
    </Form>
  </Modal>
);

export default FeeFormModal;

import { Form, Input, Button, Modal } from 'antd';

const { TextArea } = Input;

const UndoInvoiceModal = ({ open, submitting, onCancel, onSubmit }) => {
  const [form] = Form.useForm();

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  const handleFinish = async (values) => {
    await onSubmit(values);
    form.resetFields();
  };

  return (
    <Modal
      title="Hoàn tác hóa đơn"
      open={open}
      onCancel={handleCancel}
      footer={null}
      destroyOnHidden
    >
      <Form form={form} layout="vertical" onFinish={handleFinish}>
        <Form.Item
          name="reason"
          label="Lý do hoàn tác"
          rules={[{ required: true, message: 'Bắt buộc phải nhập lý do!' }]}
        >
          <TextArea rows={3} placeholder="Ví dụ: Thu nhầm tiền, Sinh viên xin rút..." />
        </Form.Item>
        <div style={{ textAlign: 'right' }}>
          <Button onClick={handleCancel} style={{ marginRight: 8 }}>Hủy</Button>
          <Button type="primary" danger htmlType="submit" loading={submitting}>
            Xác nhận Hoàn tác
          </Button>
        </div>
      </Form>
    </Modal>
  );
};

export default UndoInvoiceModal;

import { Form, Input, Button, Switch, Space } from 'antd';
import JoditEditor from 'jodit-react';

const NotificationForm = ({ form, editorRef, contentValue, setContentValue, config, saving, isCreateMode, onSubmit, onCancel }) => (
  <Form form={form} layout="vertical" initialValues={{ active: true, content: '' }} onFinish={onSubmit}>
    <Form.Item name="title" label="Tiêu đề thông báo" rules={[{ required: true, message: 'Vui lòng nhập tiêu đề!' }]}>
      <Input placeholder="Nhập tiêu đề..." size="large" />
    </Form.Item>
    <Form.Item name="content" label="Nội dung" rules={[{ required: true, message: 'Vui lòng nhập nội dung!' }]}>
      <JoditEditor
        ref={editorRef}
        value={contentValue}
        config={config}
        onBlur={(newContent) => {
          form.setFieldsValue({ content: newContent });
          setContentValue(newContent);
        }}
        onChange={(newContent) => form.setFieldsValue({ content: newContent })}
      />
    </Form.Item>
    <Form.Item name="active" label="Trạng thái hiển thị" valuePropName="checked">
      <Switch checkedChildren="Hoạt động" unCheckedChildren="Đã ẩn" />
    </Form.Item>
    <Form.Item style={{ marginTop: 32, marginBottom: 0, textAlign: 'right' }}>
      <Space>
        <Button onClick={onCancel}>Hủy</Button>
        <Button type="primary" htmlType="submit" loading={saving} size="large">
          {isCreateMode ? 'Tạo mới' : 'Lưu thay đổi'}
        </Button>
      </Space>
    </Form.Item>
  </Form>
);

export default NotificationForm;

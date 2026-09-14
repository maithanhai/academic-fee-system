import { Table, Button, Space, Typography, Modal, Form, Input, message, Tag, Switch } from 'antd';
import { PlusOutlined, EditOutlined } from '@ant-design/icons';
import { useState, useEffect } from 'react';
import subjectApi from '../api/subjectApi';

const { Text } = Typography;

const SubjectTab = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [editingId, setEditingId] = useState(null); 
  const [form] = Form.useForm();

  const fetchSubjects = async () => {
    setLoading(true);
    try {
      const res = await subjectApi.getSubjects();
      setData(res.data?.data || []);
    } catch {
      message.error('Lỗi khi tải danh sách môn học!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    Promise.resolve().then(() => fetchSubjects());
  }, []);

  const handleOpenCreate = () => {
    setEditingId(null);
    form.resetFields();
    form.setFieldsValue({ active: true });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (record) => {
    setEditingId(record.id);
    form.setFieldsValue({ 
      name: record.name,
      active: record.active 
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (values) => {
    setSubmitting(true);
    try {
      if (editingId) {
        await subjectApi.updateSubject(editingId, values);
        message.success('Cập nhật môn học thành công!');
      } else {
        await subjectApi.addSubject(values);
        message.success('Tạo môn học thành công!');
      }
      setIsModalOpen(false);
      form.resetFields();
      fetchSubjects(); 
    } catch (error) {
      message.error(error.response?.data?.message || 'Thao tác thất bại!');
    } finally {
      setSubmitting(false);
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80, align: 'center' },
    { 
      title: 'TÊN MÔN HỌC', 
      dataIndex: 'name', 
      render: (text) => <Text strong color="#1677ff">{text}</Text> 
    },
    {
      title: 'TRẠNG THÁI',
      dataIndex: 'active',
      width: 150,
      render: (active) => (
        <Tag color={active ? 'success' : 'default'}>
          {active ? 'Hoạt động' : 'Đã tắt'}
        </Tag>
      ),
    },
    {
      title: 'THAO TÁC',
      width: 120,
      align: 'center',
      render: (_, record) => (
        <Button 
          size="small" 
          icon={<EditOutlined />} 
          onClick={() => handleOpenEdit(record)}
        >
          Sửa
        </Button>
      ),
    },
  ];

  return (
    <>
      <Space orientation="vertical" size="middle" style={{ display: 'flex' }}>
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleOpenCreate}>
            Tạo Môn học
          </Button>
        </div>

        <Table
          columns={columns}
          dataSource={data}
          rowKey="id"
          loading={loading}
          bordered
          pagination={false}
        />
      </Space>

      <Modal
        title={editingId ? "Cập nhật Môn học" : "Thêm mới Môn học"}
        open={isModalOpen}
        onCancel={() => {
          setIsModalOpen(false);
          form.resetFields();
        }}
        onOk={() => form.submit()}
        confirmLoading={submitting}
        destroyOnHidden 
      >
        <Form 
          form={form} 
          layout="vertical" 
          onFinish={handleSubmit} 
          style={{ marginTop: 16 }}
          initialValues={{ active: true }} 
        >
          <Form.Item
            name="name"
            label="Tên môn học"
            rules={[{ required: true, message: 'Vui lòng nhập tên môn học!' }]}
          >
            <Input placeholder="VD: Toán, Ngữ Văn, Tiếng Anh..." />
          </Form.Item>

          <Form.Item
            name="active"
            label="Trạng thái"
            valuePropName="checked"
          >
            <Switch checkedChildren="Hoạt động" unCheckedChildren="Đã tắt" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default SubjectTab;
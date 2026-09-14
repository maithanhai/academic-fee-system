import { Table, Button, Space, Typography, Modal, Form, Input, message } from 'antd';
import { PlusOutlined, EditOutlined } from '@ant-design/icons';
import { useState, useEffect } from 'react';
import departmentApi from '../../people/api/departmentApi';

const { Text } = Typography;

const DepartmentTab = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form] = Form.useForm();

  const fetchDepartments = async () => {
    setLoading(true);
    try {
      const res = await departmentApi.getDepartments();
      setData(res.data?.data || []);
    } catch  {
      message.error('Lỗi khi tải danh sách tổ bộ môn!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    Promise.resolve().then(() => fetchDepartments());
  }, []);

  const handleOpenCreate = () => {
    setEditingId(null);
    form.resetFields();
    setIsModalOpen(true);
  };

  const handleOpenEdit = (record) => {
    setEditingId(record.id);
    form.setFieldsValue({ name: record.name });
    setIsModalOpen(true);
  };

  const handleSubmit = async (values) => {
    setSubmitting(true);
    try {
      if (editingId) {
        await departmentApi.updateDepartment(editingId, values);
        message.success('Cập nhật tổ bộ môn thành công!');
      } else {
        await departmentApi.addDepartment(values);
        message.success('Tạo tổ bộ môn thành công!');
      }
      setIsModalOpen(false);
      form.resetFields();
      fetchDepartments(); 
    } catch (error) {
      message.error(error.response?.data?.message || 'Thao tác thất bại!');
    } finally {
      setSubmitting(false);
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80, align: 'center' },
    { 
      title: 'TÊN TỔ BỘ MÔN', 
      dataIndex: 'name', 
      render: (text) => <Text strong>{text}</Text> 
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
            Tạo Tổ bộ môn
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
        title={editingId ? "Cập nhật Tổ bộ môn" : "Thêm mới Tổ bộ môn"}
        open={isModalOpen}
        onCancel={() => {
          setIsModalOpen(false);
          form.resetFields();
        }}
        onOk={() => form.submit()}
        confirmLoading={submitting}
        destroyOnHidden
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit} style={{ marginTop: 16 }}>
          <Form.Item
            name="name"
            label="Tên tổ bộ môn"
            rules={[{ required: true, message: 'Vui lòng nhập tên tổ bộ môn!' }]}
          >
            <Input placeholder="VD: Tổ Toán - Tin, Tổ Ngữ Văn..." />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default DepartmentTab;
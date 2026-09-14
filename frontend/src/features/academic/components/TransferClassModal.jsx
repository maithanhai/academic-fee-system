import { useState, useEffect, useCallback } from 'react';
import { Modal, Form, Select, message } from 'antd';
import academicYearApi from '../api/academicYearApi';
import classEnrollmentApi from '../api/classEnrollmentApi';

const TransferClassModal = ({ open, onClose, enrollmentId, academicYearId, onSuccess }) => {
  const [form] = Form.useForm();
  const [classesList, setClassesList] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchClasses = useCallback(async () => {
    if (!academicYearId) return;
    try {
      setLoading(true);
      const res = await academicYearApi.getClassesByAcademicYearId(academicYearId);
      setClassesList(res.data.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách lớp");
    } finally {
      setLoading(false);
    }
  }, [academicYearId]);

  useEffect(() => {
    if (open) {
      Promise.resolve().then(() =>fetchClasses());
    }
  }, [open, fetchClasses]);

  const handleFinish = async (values) => {
    try {
      await classEnrollmentApi.transferStudent({
        enrollmentId: enrollmentId,
        newClassId: values.newClassId,
      });
      message.success("Chuyển lớp thành công");
      form.resetFields();
      onSuccess();
      onClose();
    } catch {
      message.error("Lỗi khi chuyển lớp");
    }
  };

  return (
    <Modal
      title="Chuyển học sinh sang lớp khác"
      open={open}
      onCancel={() => { form.resetFields(); onClose(); }}
      onOk={() => form.submit()}
      destroyOnHidden
    >
      <Form form={form} layout="vertical" onFinish={handleFinish}>
        <Form.Item name="newClassId" label="Chọn lớp mới" rules={[{ required: true, message: 'Chọn lớp chuyển đến!' }]}>
          <Select 
            placeholder="Chọn lớp..."
            loading={loading}
            options={classesList.map(c => ({
              value: c.id,
              label: c.name
            }))}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default TransferClassModal;
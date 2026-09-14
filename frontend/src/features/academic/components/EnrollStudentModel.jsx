import { useState, useEffect, useCallback } from 'react';
import { Modal, Form, Select, message } from 'antd';
import studentApi from '../../people/api/studentApi';
import classEnrollmentApi from '../api/classEnrollmentApi';

const EnrollStudentModal = ({ open, onClose, classId, academicYearId, onSuccess }) => {
  const [form] = Form.useForm();
  const [studentsList, setStudentsList] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchUnenrolled = useCallback(async () => {
    if (!academicYearId) return;
    try {
      setLoading(true);
      const res = await studentApi.getUnenrolledStudents(academicYearId);
      setStudentsList(res.data.data || []);
    } catch  {
      message.error("Lỗi khi tải danh sách học sinh chưa có lớp");
    } finally {
      setLoading(false);
    }
  }, [academicYearId]);

  useEffect(() => {
    if (open) {
      Promise.resolve().then(() =>fetchUnenrolled());
    }
  }, [open, fetchUnenrolled]);

  const handleFinish = async (values) => {
    try {
      await classEnrollmentApi.createEnrollments({
        classId: parseInt(classId),
        studentIds: values.studentIds,
      });
      message.success("Thêm học sinh thành công");
      form.resetFields();
      onSuccess();
      onClose();
    } catch {
      message.error("Lỗi khi xếp lớp");
    }
  };

  return (
    <Modal
      title="Thêm học sinh vào lớp"
      open={open}
      onCancel={() => { form.resetFields(); onClose(); }}
      onOk={() => form.submit()}
      destroyOnHidden
    >
      <Form form={form} layout="vertical" onFinish={handleFinish}>
        <Form.Item name="studentIds" label="Chọn học sinh" rules={[{ required: true, message: 'Chọn ít nhất 1!' }]}>
          <Select 
            mode="multiple" 
            placeholder="Tìm học sinh chưa phân lớp..."
            loading={loading}
            options={studentsList.map(s => ({
              value: s.id,
              label: `${s.fullName} (${s.username})`
            }))}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EnrollStudentModal;
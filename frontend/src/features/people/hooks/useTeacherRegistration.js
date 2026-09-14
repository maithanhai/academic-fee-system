import { useEffect, useState } from 'react';
import { Form, message } from 'antd';
import adminUserApi from '../api/adminUserApi';
import departmentApi from '../api/departmentApi';
import subjectApi from '../../academic/api/subjectApi';
import { toApiDate } from '../../../shared/utils/dateUtils';

const useTeacherRegistration = () => {
  const [form] = Form.useForm();
  const [creating, setCreating] = useState(false);
  const [departments, setDepartments] = useState([]);
  const [subjects, setSubjects] = useState([]);

  useEffect(() => {
    Promise.all([departmentApi.getDepartments(), subjectApi.getSubjects()])
      .then(([departmentResponse, subjectResponse]) => {
        setDepartments(departmentResponse.data?.data || []);
        setSubjects(subjectResponse.data?.data || []);
      })
      .catch(() => message.error('Lỗi khi tải danh sách tổ bộ môn và môn học!'));
  }, []);

  const resetForm = () => form.resetFields();

  const submit = async (values) => {
    setCreating(true);
    try {
      await adminUserApi.createTeacher({
        ...values,
        dateOfBirth: toApiDate(values.dateOfBirth),
      });
      message.success('Tạo tài khoản Giáo viên thành công!');
      resetForm();
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi tạo tài khoản giáo viên!');
    } finally {
      setCreating(false);
    }
  };

  return { form, creating, departments, subjects, resetForm, submit };
};

export default useTeacherRegistration;

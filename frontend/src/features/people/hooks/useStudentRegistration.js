import { useEffect, useState } from 'react';
import { Form, message } from 'antd';
import adminUserApi from '../api/adminUserApi';
import cohortApi from '../../academic/api/cohortApi';
import classApi from '../../academic/api/classApi';
import { toApiDate } from '../../../shared/utils/dateUtils';

const useStudentRegistration = () => {
  const [form] = Form.useForm();
  const [creating, setCreating] = useState(false);
  const [cohorts, setCohorts] = useState([]);
  const [classes, setClasses] = useState([]);
  const [selectedGrade, setSelectedGrade] = useState(null);

  useEffect(() => {
    Promise.all([cohortApi.getCohorts(), classApi.getClasses()])
      .then(([cohortResponse, classResponse]) => {
        setCohorts(cohortResponse.data?.data || []);
        setClasses(classResponse.data?.data || []);
      })
      .catch(() => message.error('Lỗi khi tải danh sách khóa học và lớp học!'));
  }, []);

  const handleGradeChange = (grade) => {
    setSelectedGrade(grade);
    form.setFieldsValue({ schoolClassId: null });
  };

  const resetForm = () => {
    form.resetFields();
    setSelectedGrade(null);
  };

  const submit = async (values) => {
    setCreating(true);
    try {
      await adminUserApi.createStudent({
        ...values,
        dateOfBirth: toApiDate(values.dateOfBirth),
      });
      message.success('Tạo tài khoản Học sinh thành công!');
      resetForm();
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi tạo tài khoản học sinh!');
    } finally {
      setCreating(false);
    }
  };

  return { form, creating, cohorts, classes, selectedGrade, handleGradeChange, resetForm, submit };
};

export default useStudentRegistration;

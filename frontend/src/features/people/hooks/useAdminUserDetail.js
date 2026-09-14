import { useEffect, useState } from 'react';
import { Form, message } from 'antd';
import { useLocation, useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import adminUserApi from '../api/adminUserApi';
import departmentApi from '../api/departmentApi';
import subjectApi from '../../academic/api/subjectApi';
import teacherApi from '../api/teacherApi';
import studentApi from '../api/studentApi';
import { toApiDate } from '../../../shared/utils/dateUtils';

const useAdminUserDetail = () => {
  const [form] = Form.useForm();
  const { id } = useParams();
  const { pathname } = useLocation();
  const isTeacher = pathname.includes('/teachers/');
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [userData, setUserData] = useState(null);
  const [departments, setDepartments] = useState([]);
  const [subjects, setSubjects] = useState([]);

  const getFormValues = (data) => ({
    fullName: data.fullName,
    dateOfBirth: data.dateOfBirth ? dayjs(data.dateOfBirth) : null,
    phone: data.phone,
    email: data.email,
    gender: data.gender,
    active: data.active,
    address: data.address,
    phoneParent: data.phoneParent,
    departmentId: data.department?.id,
    subjectIds: data.subjects?.map((subject) => subject.id) || [],
  });

  useEffect(() => {
    const requests = [isTeacher ? adminUserApi.getTeacherById(id) : adminUserApi.getStudentById(id)];
    if (isTeacher) requests.push(departmentApi.getDepartments(), subjectApi.getSubjects());
    Promise.all(requests)
      .then(([userResponse, departmentResponse, subjectResponse]) => {
        setUserData(userResponse.data.data);
        if (isTeacher) {
          setDepartments(departmentResponse.data?.data || []);
          setSubjects(subjectResponse.data?.data || []);
        }
      })
      .catch((error) => { console.error('Lỗi lấy dữ liệu:', error); message.error('Không lấy được thông tin chi tiết!'); })
      .finally(() => setLoading(false));
  }, [id, isTeacher]);

  useEffect(() => {
    if (userData) form.setFieldsValue(getFormValues(userData));
  }, [userData, form]);

  const submit = async (values) => {
    setUpdating(true);
    try {
      const payload = { ...values, dateOfBirth: values.dateOfBirth ? toApiDate(values.dateOfBirth) : null };
      const response = isTeacher ? await teacherApi.updateTeacher(id, payload) : await studentApi.updateStudent(id, payload);
      setUserData(response.data?.data || userData);
      message.success('Cập nhật thông tin thành công!');
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi khi lưu thay đổi!');
    } finally { setUpdating(false); }
  };

  const reset = () => {
    if (userData) {
      form.setFieldsValue(getFormValues(userData));
      message.info('Đã đặt lại dữ liệu gốc!');
    }
  };

  return { form, id, isTeacher, loading, updating, userData, departments, subjects, submit, reset };
};

export default useAdminUserDetail;

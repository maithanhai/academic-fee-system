import { useEffect, useState } from 'react';
import { Form, message } from 'antd';
import { useSelector } from 'react-redux';
import userApi from '../api/userApi';
import { ROLES } from '../../../shared/constants/roles';

const useProfile = () => {
  const [form] = Form.useForm();
  const role = useSelector((state) => state.auth.role);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    userApi.profile()
      .then((response) => {
        const data = response.data.data;
        const profileData = data.teacherDetailResponse || data.studentDetailResponse || data;
        setProfile(profileData);
        form.setFieldsValue({
          email: profileData.email,
          phone: profileData.phone,
          address: profileData.address,
          phoneParent: profileData.phoneParent,
        });
      })
      .catch(() => message.error('Không lấy được thông tin cá nhân!'))
      .finally(() => setLoading(false));
  }, [form]);

  const updateProfile = async (values) => {
    setUpdating(true);
    try {
      await userApi.updateProfile(values);
      message.success('Cập nhật thông tin thành công!');
    } catch {
      message.error('Lỗi khi cập nhật thông tin!');
    } finally {
      setUpdating(false);
    }
  };

  return { form, role, isStudent: role === ROLES.STUDENT, isTeacher: role === ROLES.TEACHER, profile, loading, updating, updateProfile };
};

export default useProfile;

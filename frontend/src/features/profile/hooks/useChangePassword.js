import { useState } from 'react';
import { Form, message } from 'antd';
import userApi from '../api/userApi';

const useChangePassword = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const submit = async (values) => {
    setLoading(true);
    try {
      await userApi.password({ oldPassword: values.oldPassword, newPassword: values.newPassword });
      message.success('Đổi mật khẩu thành công!');
      form.resetFields();
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi khi đổi mật khẩu. Vui lòng thử lại!');
    } finally { setLoading(false); }
  };
  return { form, loading, submit };
};

export default useChangePassword;

import { Card } from 'antd';
import useChangePassword from '../hooks/useChangePassword';
import ChangePasswordForm from '../components/ChangePasswordForm';

const ChangePasswordPage = () => {
  const { form, loading, submit } = useChangePassword();
  return <Card title="Đổi Mật Khẩu" style={{ width: '100%', maxWidth: 500, margin: '40px auto' }}><ChangePasswordForm form={form} loading={loading} onSubmit={submit} /></Card>;
};

export default ChangePasswordPage;

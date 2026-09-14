import { Button, Card } from 'antd';
import { useNavigate } from 'react-router-dom';
import useTeacherRegistration from '../hooks/useTeacherRegistration';
import TeacherRegistrationForm from '../components/TeacherRegistrationForm';

const AdminRegisterTeacherPage = () => {
  const navigate = useNavigate();
  const registration = useTeacherRegistration();

  return (
    <div className="admin-register-teacher-page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2>Thêm mới tài khoản Giáo viên</h2>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>
      <Card variant={false} className="shadow-sm">
        <TeacherRegistrationForm
          form={registration.form}
          departments={registration.departments}
          subjects={registration.subjects}
          creating={registration.creating}
          onReset={registration.resetForm}
          onSubmit={registration.submit}
        />
      </Card>
    </div>
  );
};

export default AdminRegisterTeacherPage;

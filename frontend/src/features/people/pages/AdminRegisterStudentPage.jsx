import { Button, Card } from 'antd';
import { useNavigate } from 'react-router-dom';
import useStudentRegistration from '../hooks/useStudentRegistration';
import StudentRegistrationForm from '../components/StudentRegistrationForm';

const AdminRegisterStudentPage = () => {
  const navigate = useNavigate();
  const registration = useStudentRegistration();

  return (
    <div className="admin-register-student-page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2>Thêm mới tài khoản Học sinh</h2>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>
      <Card variant={false} className="shadow-sm">
        <StudentRegistrationForm
          form={registration.form}
          cohorts={registration.cohorts}
          classes={registration.classes}
          selectedGrade={registration.selectedGrade}
          creating={registration.creating}
          onGradeChange={registration.handleGradeChange}
          onReset={registration.resetForm}
          onSubmit={registration.submit}
        />
      </Card>
    </div>
  );
};

export default AdminRegisterStudentPage;

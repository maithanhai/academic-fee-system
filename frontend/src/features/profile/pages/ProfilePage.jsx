import { Skeleton, Row, Col, Card } from 'antd';
import { toApiDate } from '../../../shared/utils/dateUtils';
import useProfile from '../hooks/useProfile';
import ProfileContactForm from '../components/ProfileContactForm';
import ProfileSummary from '../components/ProfileSummary';
import EnrollmentHistory from '../components/EnrollmentHistory';

const ProfilePage = () => {
  const { form, role, isStudent, isTeacher, profile, loading, updating, updateProfile } = useProfile();

  const handleSubmit = (values) => updateProfile({ ...values, dateOfBirth: toApiDate(values.dateOfBirth) });

  if (loading) return <Skeleton active paragraph={{ rows: 10 }} />;

  return <div className="profile-page">
    <h2 style={{ marginBottom: 24 }}>Hồ sơ cá nhân</h2>
    <Row gutter={[24, 24]}>
      <Col xs={24} md={12} lg={10}><Card title="Cập nhật liên hệ" variant={false}><ProfileContactForm form={form} isStudent={isStudent} updating={updating} onSubmit={handleSubmit} /></Card></Col>
      <Col xs={24} md={12} lg={14}><ProfileSummary profile={profile} role={role} isStudent={isStudent} isTeacher={isTeacher} />{isStudent && <EnrollmentHistory enrollments={profile?.enrollments} />}</Col>
    </Row>
  </div>;
};

export default ProfilePage;

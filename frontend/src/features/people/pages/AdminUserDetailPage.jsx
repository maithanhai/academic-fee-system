import { Button, Card, Col, Row, Skeleton } from "antd";
import { useNavigate } from "react-router-dom";
import useAdminUserDetail from "../hooks/useAdminUserDetail";
import UserEditForm from "../components/UserEditForm";
import UserSystemInfo from "../components/UserSystemInfo";
import EnrollmentHistory from "../components/EnrollmentHistory";

const AdminUserDetailPage = () => {
  const navigate = useNavigate();
  const detail = useAdminUserDetail();

  if (detail.loading) return <Skeleton active paragraph={{ rows: 10 }} />;

  return (
    <div className="admin-user-detail-page">
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 24,
        }}
      >
        <h2>Chi tiết tài khoản: {detail.userData?.username}</h2>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>
      <Row gutter={[24, 24]}>
        <Col xs={24} md={14} lg={16}>
          <Card
            title="Chỉnh sửa thông tin"
            variant={false}
            className="shadow-sm"
          >
            <UserEditForm
              form={detail.form}
              isTeacher={detail.isTeacher}
              departments={detail.departments}
              subjects={detail.subjects}
              updating={detail.updating}
              onFinish={detail.submit}
              onReset={detail.reset}
            />
          </Card>
        </Col>
        <Col xs={24} md={10} lg={8}>
          <UserSystemInfo
            userData={detail.userData}
            isTeacher={detail.isTeacher}
          />
          {!detail.isTeacher && (
            <EnrollmentHistory enrollments={detail.userData?.enrollments} />
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AdminUserDetailPage;

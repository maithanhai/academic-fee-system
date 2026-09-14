import { Button, Card, Col, Row, Spin } from 'antd';
import NotificationForm from '../components/NotificationForm';
import useNotificationDetail from '../hooks/useNotificationDetail';

const AdminNotificationDetailPage = () => {
  const detail = useNotificationDetail();
  return (
    <div className="admin-notification-detail-page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2>{detail.isCreateMode ? 'Tạo thông báo mới' : `Chỉnh sửa thông báo ID: ${detail.id}`}</h2>
        <Button onClick={() => detail.navigate(-1)}>Quay lại</Button>
      </div>
      <Row justify="center">
        <Col xs={24} md={22} lg={18}>
          <Card variant={false} className="shadow-sm">
            <Spin spinning={detail.loading} description="Đang tải dữ liệu...">
              <NotificationForm
                form={detail.form}
                editorRef={detail.editorRef}
                contentValue={detail.contentValue}
                setContentValue={detail.setContentValue}
                config={detail.config}
                saving={detail.saving}
                isCreateMode={detail.isCreateMode}
                onSubmit={detail.submit}
                onCancel={() => detail.navigate(-1)}
              />
            </Spin>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AdminNotificationDetailPage;

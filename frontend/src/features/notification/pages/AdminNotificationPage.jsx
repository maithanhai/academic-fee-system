import { Card, message, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import useNotifications from "../hooks/useNotifications";
import NotificationTable from "../components/NotificationTable";
import notificationApi from "../api/notificationApi";

const { Title } = Typography;

const AdminNotificationPage = () => {
  const navigate = useNavigate();
  const {
    notifications,
    loading,
    pagination,
    filters,
    setFilters,
    fetchNotifications,
  } = useNotifications();
  const handleSendEmail = async (id) => {
    try {
      const res = await notificationApi.sendNotificationToStudents(id);
      message.success(
        res.data.message || "Đã đưa vào hàng đợi gửi email thành công!",
      );
    } catch {
      message.error("Gửi email thất bại.");
    }
  };
  return (
    <Card
      variant={false}
      title={
        <Title level={3} style={{ margin: 0 }}>
          Quản lý Thông báo
        </Title>
      }
    >
      <NotificationTable
        notifications={notifications}
        loading={loading}
        pagination={pagination}
        setFilters={setFilters}
        onPageChange={(page, pageSize) =>
          fetchNotifications(page, pageSize, filters)
        }
        onCreate={() => navigate("/admin/notifications/create")}
        onDetail={(id) => navigate(`/admin/notifications/${id}`)}
        onSendEmail={handleSendEmail}
      />
    </Card>
  );
};

export default AdminNotificationPage;

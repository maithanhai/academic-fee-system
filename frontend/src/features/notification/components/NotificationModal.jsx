import { useState, useEffect } from "react";
import { Modal, Typography, Spin, message, Divider } from "antd";
import notificationApi from "../api/notificationApi";

const { Title } = Typography;

const NotificationModal = ({ open, onClose, notificationId }) => {
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    const fetchNotificationDetail = async () => {
      setLoading(true);
      try {
        const res = await notificationApi.getNotificationByIdForStudent(notificationId);
        setNotification(res.data.data);
      } catch {
        message.error("Lỗi khi lấy nội dung chi tiết!");
        onClose();
      } finally {
        setLoading(false);
      }
    };
    if (open && notificationId) {
      fetchNotificationDetail();
    }
  }, [open, notificationId, onClose]);

  return (
    <Modal
      title="NỘI DUNG THÔNG BÁO"
      open={open}
      onCancel={onClose}
      footer={null} 
      width={700}  
      centered
    >
      {loading ? (
        <div style={{ textAlign: "center", padding: "40px 0" }}>
          <Spin size="large" />
        </div>
      ) : (
        notification && (
          <div>
            <Title level={4} style={{ color: "#1677ff", marginTop: 10 }}>
              {notification.title}
            </Title>
            <Divider style={{ margin: "12px 0" }} />
            
            <div
              style={{ fontSize: "15px", lineHeight: "1.6" }}
              dangerouslySetInnerHTML={{ __html: notification.content }}
            />
          </div>
        )
      )}
    </Modal>
  );
};

export default NotificationModal;
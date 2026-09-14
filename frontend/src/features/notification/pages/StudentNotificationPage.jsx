import { useState, useEffect } from "react";
import { Table, Input, Button, Space, Typography, message } from "antd";
import { SearchOutlined, BellOutlined } from "@ant-design/icons";
import notificationApi from "../api/notificationApi";
import NotificationModal from "../components/NotificationModal";
import { toDisplayDate } from "../../../shared/utils/dateUtils";

const { Text, Title } = Typography;

const StudentNotificationPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedId, setSelectedId] = useState(null);

  const fetchNotifications = async () => {
    setLoading(true);
    try {
      const res = await notificationApi.getNotificationsForStudent();
      setNotifications(res.data.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách thông báo!");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    Promise.resolve().then(() =>fetchNotifications());
  }, []);

  const handleDetail = (id) => {
    setSelectedId(id);
    setModalOpen(true);
  };

  const filteredData = notifications.filter((item) =>
    item.title?.toLowerCase().includes(searchText.toLowerCase()),
  );

  const columns = [
    {
      title: "TIÊU ĐỀ",
      dataIndex: "title",
      render: (text) => (
        <Space>
          <BellOutlined />
          <Text strong style={{ color: "#1677ff", fontSize: "15px" }}>
            {text}
          </Text>
        </Space>
      ),
    },
    {
      title: "NGÀY TẠO",
      dataIndex: "createdDate",
      key: "createdDate",
      align: "center",
      render: (date) => <Text>{toDisplayDate(date)}</Text>,
    },
    {
      title: "THAO TÁC",
      width: 150,
      render: (_, record) => (
        <Button size="small" onClick={() => handleDetail(record.id)}>
          Xem chi tiết
        </Button>
      ),
    },
  ];

  return (
    <Space orientation="vertical" size="middle" style={{ display: "flex" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          flexWrap: "wrap",
          gap: 16,
        }}
      >
        <Title level={4} style={{ margin: 0 }}>
          Thông báo
        </Title>
        <Space wrap>
          <Input
            prefix={<SearchOutlined />}
            placeholder="Tìm kiếm theo tiêu đề..."
            allowClear
            style={{ width: 300 }}
            onChange={(e) => setSearchText(e.target.value)}
          />
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={filteredData}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 10, showSizeChanger: false }}
        bordered
      />

      <NotificationModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        notificationId={selectedId}
      />
    </Space>
  );
};

export default StudentNotificationPage;

import { Table, Input, Select, Button, Space, Typography, Badge, Popconfirm } from "antd";
import { PlusOutlined, SearchOutlined, SendOutlined } from "@ant-design/icons";

const { Text } = Typography;

const NotificationTable = ({
  notifications,
  loading,
  pagination,
  setFilters,
  onPageChange,
  onCreate,
  onDetail,
  onSendEmail, 
}) => {
  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      width: 80,
      render: (id) => <Text strong>{id}</Text>,
    },
    {
      title: "TIÊU ĐỀ",
      dataIndex: "title",
      render: (text) => <Text style={{ color: "#1677ff" }}>{text}</Text>,
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "active",
      width: 150,
      render: (active) => (
        <Badge
          status={active ? "success" : "error"}
          text={active ? "Hoạt động" : "Đã ẩn"}
        />
      ),
    },
    {
      title: "THAO TÁC",
      width: 200, 
      render: (_, record) => (
        <Space>
          <Button size="small" onClick={() => onDetail(record.id)}>
            Chi tiết
          </Button>
          
          <Popconfirm
            title="Xác nhận gửi email hàng loạt?"
            description="Thông báo này sẽ được gửi tới tất cả học sinh thuộc 3 khóa gần nhất."
            onConfirm={() => onSendEmail(record.id)}
            okText="Gửi ngay"
            cancelText="Hủy"
            placement="topRight"
          >
            <Button 
              size="small" 
              type="primary" 
              icon={<SendOutlined />}
              disabled={!record.active} 
            >
              Gửi email
            </Button>
          </Popconfirm>
        </Space>
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
        <Space wrap>
          <Input
            prefix={<SearchOutlined />}
            placeholder="Tìm kiếm theo tiêu đề..."
            allowClear
            style={{ width: 300 }}
            onChange={(event) =>
              setFilters((previous) => ({
                ...previous,
                title: event.target.value,
              }))
            }
          />
          <Select
            placeholder="Tất cả trạng thái"
            allowClear
            style={{ width: 180 }}
            onChange={(value) =>
              setFilters((previous) => ({ ...previous, active: value }))
            }
            options={[
              { value: true, label: "Đang hoạt động" },
              { value: false, label: "Đã ẩn" },
            ]}
          />
        </Space>
        <Button type="primary" onClick={onCreate} icon={<PlusOutlined />}>
          Tạo thông báo
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={notifications}
        rowKey="id"
        loading={loading}
        pagination={{ ...pagination, showSizeChanger: true }}
        onChange={(page) => onPageChange(page.current, page.pageSize)}
        bordered
      />
    </Space>
  );
};

export default NotificationTable;
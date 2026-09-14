import { Table, Input, Select, Button, Space, Typography, Badge } from "antd";
import { PlusOutlined, SearchOutlined, UserOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { Avatar } from "antd";
import useTeachers from "../hooks/useTeachers";

const { Text } = Typography;

const TeacherTab = () => {
  const navigate = useNavigate();
  const {
    teachers,
    loading,
    pagination,
    filters,
    setFilters,
    departments,
    fetchTeachers,
  } = useTeachers();

  const columns = [
    { title: "ID", dataIndex: "id", width: 70 },
    {
      title: "GIÁO VIÊN",
      render: (_, record) => (
        <Space>
          <Avatar
            style={{ backgroundColor: "#1677ff" }}
            icon={<UserOutlined />}
          />
          <Space orientation="vertical" size={0}>
            <Text strong>{record.fullName}</Text>
            <Text type="secondary">{record.username}</Text>
          </Space>
        </Space>
      ),
    },
    {
      title: "TỔ BỘ MÔN",
      render: (_, record) => <Text>{record.department?.name || "---"}</Text>,
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "active",
      render: (active) => (
        <Badge
          status={active ? "success" : "error"}
          text={active ? "Hoạt động" : "Vô hiệu"}
        />
      ),
    },
    {
      title: "THAO TÁC",
      render: (_, record) => (
        <Button
          size="small"
          onClick={() => navigate(`/admin/teachers/${record.id}`)}
        >
          Chi tiết
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
        <Space wrap>
          <Input
            prefix={<SearchOutlined />}
            placeholder="Tìm theo tên, tài khoản..."
            allowClear
            style={{ width: 250 }}
            onChange={(event) =>
              setFilters((previous) => ({
                ...previous,
                keyword: event.target.value,
              }))
            }
          />
          <Select
            placeholder="Trạng thái"
            allowClear
            style={{ width: 150 }}
            onChange={(value) =>
              setFilters((previous) => ({ ...previous, active: value }))
            }
            options={[
              { value: true, label: "Hoạt động" },
              { value: false, label: "Vô hiệu" },
            ]}
          />
          <Select
            placeholder="Tất cả tổ bộ môn"
            allowClear
            style={{ width: 180 }}
            onChange={(value) =>
              setFilters((previous) => ({ ...previous, departmentId: value }))
            }
            options={departments.map((department) => ({
              value: department.id,
              label: department.name,
            }))}
          />
        </Space>
        <Button
          type="primary"
          onClick={() => navigate("/admin/teachers/create")}
          icon={<PlusOutlined />}
        >
          Tạo tài khoản
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={teachers}
        rowKey="id"
        loading={loading}
        pagination={{ ...pagination, showSizeChanger: true }}
        onChange={(page) => fetchTeachers(page.current, page.pageSize, filters)}
        bordered
      />
    </Space>
  );
};

export default TeacherTab;

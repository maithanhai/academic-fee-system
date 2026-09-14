import {
  Table,
  Button,
  Space,
  Typography,
  Popconfirm,
  message,
  Switch,
} from "antd";
import { PlusOutlined, QuestionCircleOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import academicYearApi from "../api/academicYearApi";

const { Text } = Typography;

const AcademicYearTab = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [creating, setCreating] = useState(false);

  const [togglingId, setTogglingId] = useState(null);

  const fetchAcademicYears = async () => {
    setLoading(true);
    try {
      const res = await academicYearApi.getAcademicYears();
      setData(res.data?.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách năm học!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    Promise.resolve().then(() => fetchAcademicYears());
  }, []);

  const handleCreate = async () => {
    setCreating(true);
    try {
      await academicYearApi.addAcademicYear();
      message.success("Tạo năm học mới thành công!");
      fetchAcademicYears();
    } catch (error) {
      message.error(error.response?.data?.message || "Tạo năm học thất bại!");
    } finally {
      setCreating(false);
    }
  };

  const handleToggleActive = async (id, currentStatus) => {
    setTogglingId(id);
    try {
      await academicYearApi.updateActiveAcademicYear(id, { active: !currentStatus });
      message.success(
        `Đã ${!currentStatus ? "mở" : "khóa"} năm học thành công!`,
      );
      fetchAcademicYears(); 
    } catch (error) {
      message.error(
        error.response?.data?.message || "Lỗi khi cập nhật trạng thái!",
      );
    } finally {
      setTogglingId(null);
    }
  };

  const columns = [
    { title: "ID", dataIndex: "id", width: 80, align: "center" },
    {
      title: "TÊN NĂM HỌC",
      dataIndex: "name",
      render: (text) => (
        <Text strong color="#1677ff">
          {text}
        </Text>
      ),
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "active",
      width: 150,
      align: "center",
      render: (active, record) => (
        <Popconfirm
          title={active ? "Xác nhận khóa năm học?" : "Xác nhận mở năm học?"}
          onConfirm={() => handleToggleActive(record.id, active)}
          okText="Đồng ý"
          cancelText="Hủy"
          placement="left"
        >
          <Switch
            checked={active}
            loading={togglingId === record.id} 
            checkedChildren="Đang Mở"
            unCheckedChildren="Đã kết thúc"
            style={{ backgroundColor: active ? "#52c41a" : "gray" }}
          />
        </Popconfirm>
      ),
    },
  ];

  return (
    <Space orientation="vertical" size="middle" style={{ display: "flex" }}>
      <div style={{ display: "flex", justifyContent: "flex-end" }}>
        <Popconfirm
          title="Tự động tạo Năm học mới?"
          description="Hệ thống sẽ tự động tính toán và tạo năm học tiếp theo. Bạn có chắc chắn?"
          icon={<QuestionCircleOutlined style={{ color: "blue" }} />}
          onConfirm={handleCreate}
          okText="Đồng ý tạo"
          cancelText="Hủy"
          placement="left"
        >
          <Button type="primary" icon={<PlusOutlined />} loading={creating}>
            Tạo Năm học
          </Button>
        </Popconfirm>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        bordered
        pagination={false}
      />
    </Space>
  );
};

export default AcademicYearTab;

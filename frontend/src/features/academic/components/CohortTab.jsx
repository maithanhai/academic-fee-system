import { Table, Button, Space, Typography, Popconfirm, message } from "antd";
import { PlusOutlined, QuestionCircleOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import cohortApi from "../api/cohortApi";

const { Text } = Typography;

const CohortTab = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [creating, setCreating] = useState(false);

  const fetchCohorts = async () => {
    setLoading(true);
    try {
      const res = await cohortApi.getCohorts();
      setData(res.data?.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách khóa học!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    Promise.resolve().then(() => fetchCohorts());
  }, []);

  const handleCreate = async () => {
    setCreating(true);
    try {
      await cohortApi.addCohort();
      message.success("Tạo khóa học mới thành công!");
      fetchCohorts(); 
    } catch (error) {
      message.error(error.response?.data?.message || "Tạo khóa học thất bại!");
    } finally {
      setCreating(false);
    }
  };

  const columns = [
    { title: "ID", dataIndex: "id", width: 80, align: "center" },
    {
      title: "TÊN KHÓA HỌC",
      dataIndex: "name",
      render: (text) => <Text strong>{text}</Text>,
    },
    {
      title: "NĂM NHẬP HỌC",
      dataIndex: "admissionYear",
      align: "center",
    },
  ];

  return (
    <Space orientation="vertical" size="middle" style={{ display: "flex" }}>
      <div style={{ display: "flex", justifyContent: "flex-end" }}>
        <Popconfirm
          title="Tự động tạo Khóa học mới?"
          description="Hệ thống sẽ tự tạo khóa học tiếp theo dựa trên khóa gần nhất. Bạn có chắc chắn?"
          icon={<QuestionCircleOutlined style={{ color: "blue" }} />}
          onConfirm={handleCreate}
          okText="Đồng ý tạo"
          cancelText="Hủy"
          placement="left"
        >
          <Button type="primary" icon={<PlusOutlined />} loading={creating}>
            Tạo Khóa học
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

export default CohortTab;

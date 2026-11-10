import { useState, useEffect, useCallback } from "react";
import {
  Card,
  Typography,
  Table,
  Select,
  Space,
  Button,
  message,
  Modal,
  Form,
  Input,
  InputNumber,
  Alert,
} from "antd";
import { LockOutlined } from "@ant-design/icons";
import classApi from "../api/classApi";
import academicYearApi from "../api/academicYearApi";
import { useNavigate } from "react-router-dom";
const { Title } = Typography;

const GRADE_OPTIONS = [
  { value: "ALL", label: "Tất cả khối" },
  { value: 10, label: "Khối 10" },
  { value: 11, label: "Khối 11" },
  { value: 12, label: "Khối 12" },
];

const AdminClassListPage = () => {
  const navigate = useNavigate();
  const [academicYears, setAcademicYears] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [selectedGrade, setSelectedGrade] = useState("ALL");
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [promotingClassId, setPromotingClassId] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [formAdd] = Form.useForm();
  const fetchAcademicYears = useCallback(async () => {
    try {
      const res = await academicYearApi.getAcademicYears();
      const years = res.data.data || [];
      setAcademicYears(years);
      if (years.length > 0) {
        setSelectedYearId(years[0].id);
      }
    } catch (error) {
      message.error(error.response?.data?.message || "Thao tác thất bại");
    }
  }, []);

  useEffect(() => {
    Promise.resolve().then(() => fetchAcademicYears());
  }, [fetchAcademicYears]);

  const fetchClasses = useCallback(async () => {
    if (!selectedYearId) return;
    try {
      setLoading(true);
      const res = await classApi.getClassesByAcademicYearId(selectedYearId);
      setClasses(res.data.data || []);
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi tải danh sách lớp học");
    } finally {
      setLoading(false);
    }
  }, [selectedYearId]);

  useEffect(() => {
    Promise.resolve().then(() => fetchClasses());
  }, [fetchClasses]);

  const handleAddClass = async (values) => {
    try {
      await classApi.createClass({
        className: values.className,
        gradeLevel: values.gradeLevel,
        academicYearId: selectedYearId,
      });
      message.success("Thêm lớp học thành công");
      setIsAddModalOpen(false);
      formAdd.resetFields();
      fetchClasses();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi tạo lớp học");
    }
  };

  const handleAutoPromote = (record) => {
    Modal.confirm({
      title: `Lên lớp tự động cho ${record.name}?`,
      content:
        "Hệ thống sẽ chuyển học sinh sang lớp đích đã được tạo trong năm học kế tiếp.",
      okText: "Lên lớp",
      cancelText: "Hủy",
      onOk: async () => {
        try {
          setPromotingClassId(record.id);
          await classApi.autoPromote(record.id);
          message.success(`Lên lớp cho ${record.name} thành công`);
          await fetchClasses();
        } catch (error) {
          message.error(
            error.response?.data?.message ||
              `Không thể lên lớp cho ${record.name}`,
          );
        } finally {
          setPromotingClassId(null);
        }
      },
    });
  };

  const displayClasses =
    selectedGrade === "ALL"
      ? classes
      : classes.filter((c) => c.gradeLevel === selectedGrade);

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const columns = [
    { title: "Tên lớp", dataIndex: "name", key: "name" },
    { title: "Khối", dataIndex: "gradeLevel", key: "gradeLevel" },
    { title: "Sĩ số", dataIndex: "totalStudents", key: "totalStudents" },
    {
      title: "Thao tác",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button
            size="small"
            onClick={() => {
              navigate(`/admin/classes/${record.id}/students`, {
                state: { academicYearId: selectedYearId },
              });
            }}
          >
            Chi tiết
          </Button>
          {isYearLocked && [10, 11].includes(record.gradeLevel) && (
            <Button
              size="small"
              type="primary"
              loading={promotingClassId === record.id}
              disabled={promotingClassId !== null}
              onClick={() => handleAutoPromote(record)}
            >
              Lên lớp tự động
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <Card
      variant={false}
      title={
        <Title level={3} style={{ margin: 0 }}>
          Quản lý lớp học
        </Title>
      }
    >
      <Space
        orientation="vertical"
        size="middle"
        style={{ display: "flex", width: "100%" }}
      >
        {isYearLocked && (
          <Alert
            title={`Năm học ${currentYearObj?.name} đã đóng`}
            type="warning"
            showIcon
            icon={<LockOutlined />}
          />
        )}

        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            flexWrap: "wrap",
            gap: 16,
          }}
        >
          <Space wrap>
            <Select
              value={selectedYearId}
              onChange={setSelectedYearId}
              style={{ width: 150 }}
              options={academicYears.map((year) => ({
                value: year.id,
                label: year.name,
              }))}
            />
            <Select
              value={selectedGrade}
              onChange={setSelectedGrade}
              style={{ width: 150 }}
              options={GRADE_OPTIONS}
            />
          </Space>

          {!isYearLocked && (
            <Space wrap>
              <Button
                type="primary"
                onClick={() => setIsAddModalOpen(true)}
                disabled={!selectedYearId}
              >
                Thêm lớp học
              </Button>
            </Space>
          )}
        </div>

        <Table
          columns={columns}
          dataSource={displayClasses}
          rowKey="id"
          loading={loading}
          pagination={false}
        />
      </Space>

      <Modal
        title="Thêm lớp học mới"
        open={isAddModalOpen}
        onCancel={() => {
          setIsAddModalOpen(false);
          formAdd.resetFields();
        }}
        onOk={() => formAdd.submit()}
      >
        <Form form={formAdd} layout="vertical" onFinish={handleAddClass}>
          <Form.Item
            name="className"
            label="Tên lớp"
            rules={[{ required: true, message: "Nhập tên lớp!" }]}
          >
            <Input placeholder="VD: 10A1" />
          </Form.Item>
          <Form.Item
            name="gradeLevel"
            label="Khối"
            rules={[{ required: true, message: "Nhập khối!" }]}
          >
            <InputNumber placeholder="VD: 10" style={{ width: "100%" }} />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default AdminClassListPage;

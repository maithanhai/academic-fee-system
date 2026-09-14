import {
  Table,
  Button,
  Space,
  Typography,
  Modal,
  Form,
  InputNumber,
  Select,
  message,
  Tag,
  Row,
  Col,
  Divider,
  Empty,
  Card,
} from "antd";
import { SettingOutlined, EditOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import gradeConfigApi from "../api/gradeConfigApi";
import subjectApi from "../../academic/api/subjectApi";

const { Text } = Typography;

const EXAM_TYPE_LABELS = {
  MIENG: { text: "Miệng", color: "cyan" },
  PHUT_15: { text: "15 Phút", color: "blue" },
  TIET_1: { text: "1 Tiết", color: "geekblue" },
  HOC_KY: { text: "Học kỳ", color: "purple" },
};

const CONFIG_FIELDS = [
  { key: "oralExamConfig", type: "MIENG" },
  { key: "quizExamConfig", type: "PHUT_15" },
  { key: "midtermExamConfig", type: "TIET_1" },
  { key: "finalExamConfig", type: "HOC_KY" },
];

const GradeConfigTab = () => {
  const [subjects, setSubjects] = useState([]);
  const [selectedSubjectId, setSelectedSubjectId] = useState(null);
  const [currentConfig, setCurrentConfig] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchSubjects = async () => {
      try {
        const res = await subjectApi.getActiveSubjects();
        const fetchedSubjects = res.data?.data || [];
        setSubjects(fetchedSubjects);
        if (fetchedSubjects.length > 0) {
          setSelectedSubjectId(fetchedSubjects[0].id);
        }
      } catch {
        message.error("Lỗi tải dữ liệu!");
      }
    };
    fetchSubjects();
  }, []);

  const fetchConfigBySubject = async (subjectId) => {
    if (!subjectId) return;
    setLoading(true);
    try {
      const res = await gradeConfigApi.getGradeConfigsBySubjectId(subjectId);
      setCurrentConfig(res.data?.data);
    } catch {
      setCurrentConfig(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    Promise.resolve().then(() => fetchConfigBySubject(selectedSubjectId));
  }, [selectedSubjectId]);

  const currentSubject = subjects.find((s) => s.id === selectedSubjectId);
  const hasConfig = !!currentConfig;

  const tableData = currentConfig
    ? [
        { id: "1", examType: "MIENG", ...currentConfig.oralExamConfig },
        { id: "2", examType: "PHUT_15", ...currentConfig.quizExamConfig },
        { id: "3", examType: "TIET_1", ...currentConfig.midtermExamConfig },
        { id: "4", examType: "HOC_KY", ...currentConfig.finalExamConfig },
      ]
    : [];

  const handleOpenModal = () => {
    form.resetFields();
    if (hasConfig) {
      form.setFieldsValue({
        oralExamConfig: currentConfig.oralExamConfig,
        quizExamConfig: currentConfig.quizExamConfig,
        midtermExamConfig: currentConfig.midtermExamConfig,
        finalExamConfig: currentConfig.finalExamConfig,
      });
    } else {
      form.setFieldsValue({
        oralExamConfig: { coefficient: 1, maxColumn: 1 },
        quizExamConfig: { coefficient: 1, maxColumn: 2 },
        midtermExamConfig: { coefficient: 2, maxColumn: 1 },
        finalExamConfig: { coefficient: 3, maxColumn: 1 },
      });
    }
    setIsModalOpen(true);
  };

  const handleSubmit = async (values) => {
    setSubmitting(true);
    const payload = {
      subjectId: selectedSubjectId,
      oralExamConfig: values.oralExamConfig,
      quizExamConfig: values.quizExamConfig,
      midtermExamConfig: values.midtermExamConfig,
      finalExamConfig: values.finalExamConfig,
    };

    try {
      if (hasConfig) {
        await gradeConfigApi.updateGradeConfigs(payload); // PUT
        message.success("Cập nhật cấu hình điểm thành công!");
      } else {
        await gradeConfigApi.createGradeConfigs(payload); // POST
        message.success(
          `Đã thiết lập cấu hình điểm cho môn ${currentSubject?.name}!`,
        );
      }
      setIsModalOpen(false);
      fetchConfigBySubject(selectedSubjectId); // Load lại bảng
    } catch (error) {
      message.error(error.response?.data?.message || "Thao tác thất bại!");
    } finally {
      setSubmitting(false);
    }
  };

  const columns = [
    {
      title: "LOẠI ĐIỂM",
      dataIndex: "examType",
      render: (type) => {
        const info = EXAM_TYPE_LABELS[type] || { text: type, color: "default" };
        return <Tag color={info.color}>{info.text}</Tag>;
      },
    },
    {
      title: "HỆ SỐ",
      dataIndex: "coefficient",
      align: "center",
      render: (val) => (
        <Text strong color="#1677ff">
          x{val}
        </Text>
      ),
    },
    {
      title: "SỐ CỘT TỐI ĐA",
      dataIndex: "maxColumn",
      align: "center",
    },
  ];

  return (
    <Space
      orientation="vertical"
      size="large"
      style={{ display: "flex", width: "100%" }}
    >
      <Card size="small" style={{ backgroundColor: "#f5f5f5" }}>
        <Space size="middle">
          <Text strong>Chọn môn học:</Text>
          <Select
            style={{ width: 300 }}
            placeholder="-- Chọn môn học --"
            options={subjects.map((s) => ({ value: s.id, label: s.name }))}
            value={selectedSubjectId}
            onChange={(val) => setSelectedSubjectId(val)}
            showSearch="label"
          />
        </Space>
      </Card>

      {selectedSubjectId ? (
        <Card
          title={`Cấu hình cột điểm - ${currentSubject?.name}`}
          extra={
            hasConfig && (
              <Button
                type="primary"
                ghost
                icon={<EditOutlined />}
                onClick={handleOpenModal}
              >
                Cập nhật cấu hình
              </Button>
            )
          }
        >
          {hasConfig ? (
            <Table
              columns={columns}
              dataSource={tableData}
              rowKey="id"
              loading={loading}
              bordered
              pagination={false}
            />
          ) : (
            <Empty
              image={Empty.PRESENTED_IMAGE_SIMPLE}
              description={`Môn ${currentSubject?.name} chưa được thiết lập cấu hình điểm.`}
            >
              <Button
                type="primary"
                icon={<SettingOutlined />}
                onClick={handleOpenModal}
              >
                Thiết lập ngay
              </Button>
            </Empty>
          )}
        </Card>
      ) : null}

      <Modal
        title={
          hasConfig
            ? `Cấu hình cột điểm môn ${currentSubject?.name}`
            : `Thiết lập mới - ${currentSubject?.name}`
        }
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        onOk={() => form.submit()}
        confirmLoading={submitting}
        destroyOnHidden
        width={650}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          style={{ marginTop: 16 }}
        >
          <Divider orientation="left">Thông số 4 loại điểm</Divider>
          <Row gutter={16} style={{ marginBottom: 8, fontWeight: "bold" }}>
            <Col span={8}>Loại điểm</Col>
            <Col span={8}>Hệ số</Col>
            <Col span={8}>Số cột tối đa</Col>
          </Row>

          {CONFIG_FIELDS.map((field) => (
            <Row gutter={16} key={field.key} style={{ marginBottom: 8 }}>
              <Col span={8}>
                <div style={{ marginTop: 5 }}>
                  <Tag color={EXAM_TYPE_LABELS[field.type]?.color}>
                    {EXAM_TYPE_LABELS[field.type]?.text}
                  </Tag>
                </div>
              </Col>
              <Col span={8}>
                <Form.Item
                  name={[field.key, "coefficient"]}
                  rules={[{ required: true }]}
                  style={{ marginBottom: 0 }}
                >
                  <InputNumber min={1} max={10} style={{ width: "100%" }} />
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item
                  name={[field.key, "maxColumn"]}
                  rules={[{ required: true }]}
                  style={{ marginBottom: 0 }}
                >
                  <InputNumber min={1} max={10} style={{ width: "100%" }} />
                </Form.Item>
              </Col>
            </Row>
          ))}
        </Form>
      </Modal>
    </Space>
  );
};

export default GradeConfigTab;

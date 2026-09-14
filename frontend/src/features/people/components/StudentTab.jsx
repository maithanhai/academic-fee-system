import {
  Table, Input, Select, Button, Space, Typography, Badge, Upload, Avatar, Modal, message
} from "antd";
import {
  PlusOutlined, UploadOutlined, SearchOutlined, UserOutlined, ArrowDownOutlined
} from "@ant-design/icons";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useStudents from "../hooks/useStudents";

const { Text } = Typography;
const StudentTab = () => {
  const navigate = useNavigate();
  const {
    students, loading, pagination, filters, setFilters, 
    cohorts, academicYears,
    importing, importStudents: handleImport, fetchStudents,
  } = useStudents();
  const [isImportModalOpen, setIsImportModalOpen] = useState(false);
  const [importData, setImportData] = useState({ academicYearId: null, cohortId: null, file: null });
  const handleConfirmImport = async () => {
    if (!importData.academicYearId || !importData.cohortId || !importData.file) {
      message.error("Vui lòng chọn đầy đủ Năm học, Khóa học và File Excel!");
      return;
    }
    await handleImport(importData.file, importData.academicYearId, importData.cohortId);
    setIsImportModalOpen(false);
    setImportData({ academicYearId: null, cohortId: null, file: null });
  };
  const columns = [
    { title: "ID", dataIndex: "id", width: 70 },
    {
      title: "HỌC SINH",
      render: (_, record) => (
        <Space>
          <Avatar style={{ backgroundColor: "#1677ff" }} icon={<UserOutlined />} />
          <Space orientation="vertical" size={0}>
            <Text strong>{record.fullName}</Text>
            <Text type="secondary">{record.username}</Text>
          </Space>
        </Space>
      ),
    },
    {
      title: "NIÊN KHÓA",
      render: (_, record) => <Text>{record.cohort?.name || "---"}</Text>,
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "active",
      render: (active) => (
        <Badge status={active ? "success" : "error"} text={active ? "Hoạt động" : "Vô hiệu"} />
      ),
    },
    {
      title: "THAO TÁC",
      render: (_, record) => (
        <Button size="small" onClick={() => navigate(`/admin/students/${record.id}`)}>
          Chi tiết
        </Button>
      ),
    },
  ];

  return (
    <>
      <Space orientation="vertical" size="middle" style={{ display: "flex" }}>
        <div style={{ display: "flex", justifyContent: "space-between", flexWrap: "wrap", gap: 16 }}>
          <Space wrap>
            <Input
              prefix={<SearchOutlined />}
              placeholder="Tìm theo tên, tài khoản..."
              allowClear
              style={{ width: 250 }}
              onChange={(event) =>
                setFilters((previous) => ({ ...previous, keyword: event.target.value }))
              }
            />
            <Select
              placeholder="Trạng thái"
              allowClear
              style={{ width: 150 }}
              onChange={(value) => setFilters((previous) => ({ ...previous, active: value }))}
              options={[
                { value: true, label: "Hoạt động" },
                { value: false, label: "Vô hiệu" },
              ]}
            />
            <Select
              placeholder="Tất cả khóa học"
              allowClear
              style={{ width: 150 }}
              onChange={(value) => setFilters((previous) => ({ ...previous, cohortId: value }))}
              options={cohorts.map((cohort) => ({
                value: cohort.id,
                label: cohort.name,
              }))}
            />
          </Space>
          <Space>
            <Button icon={<UploadOutlined />} onClick={() => setIsImportModalOpen(true)}>
              Import Excel
            </Button>

            <Button type="primary" onClick={() => navigate("/admin/students/create")} icon={<PlusOutlined />}>
              Tạo tài khoản
            </Button>
          </Space>
        </div>
        <Table
          columns={columns}
          dataSource={students}
          rowKey="id"
          loading={loading}
          pagination={{ ...pagination, showSizeChanger: true }}
          onChange={(page) => fetchStudents(page.current, page.pageSize, filters)}
          bordered
        />
      </Space>

      <Modal
        title="Import danh sách học sinh"
        open={isImportModalOpen}
        onCancel={() => setIsImportModalOpen(false)}
        onOk={handleConfirmImport}
        confirmLoading={importing}
        destroyOnHidden
      >
        <Space orientation="vertical" size="middle" style={{ width: '100%', marginTop: 16 }}>
          <div>
            <Text strong>1. Chọn năm học:</Text>
            <Select 
              style={{ width: '100%', marginTop: 8 }} 
              placeholder="Ví dụ: 2025-2026" 
              options={academicYears?.map(y => ({ label: y.name, value: y.id }))}
              onChange={(val) => setImportData(prev => ({ ...prev, academicYearId: val }))}
            />
          </div>
          <div>
            <Text strong>2. Chọn khóa học:</Text>
            <Select 
              style={{ width: '100%', marginTop: 8 }} 
              placeholder="Ví dụ: Khóa 2025" 
              options={cohorts?.map(c => ({ label: c.name, value: c.id }))}
              onChange={(val) => setImportData(prev => ({ ...prev, cohortId: val }))}
            />
          </div>
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Text strong>3. Tải lên file Excel:</Text>
              <Button 
                type="link" 
                icon={<ArrowDownOutlined />} 
                href="/FileSample.xlsx" 
                download="FileSample.xlsx"
                style={{ padding: 0 }}
              >
                Tải file mẫu
              </Button>
            </div>
            
            <div style={{ marginTop: 8 }}>
              <Upload 
                accept=".xlsx, .xls" 
                maxCount={1}
                beforeUpload={(file) => {
                  setImportData(prev => ({ ...prev, file: file }));
                  return false; 
                }}
                onRemove={() => setImportData(prev => ({ ...prev, file: null }))}
              >
                <Button icon={<UploadOutlined />}>Chọn file Excel</Button>
              </Upload>
            </div>
          </div>
        </Space>
      </Modal>
    </>
  );
};

export default StudentTab;
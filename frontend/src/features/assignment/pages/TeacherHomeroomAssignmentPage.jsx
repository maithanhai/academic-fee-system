import { useState, useEffect, useCallback, useMemo } from "react";
import { Card, Typography, Table, Select, Space, Button, message, Alert } from "antd";
import { EyeOutlined, LockOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import academicYearApi from "../../academic/api/academicYearApi";
import homeroomAssigntmentApi from "../api/homeroomAssignmentApi";

const { Title, Text } = Typography;

const TeacherHomeroomAssignmentPage = () => {
  const navigate = useNavigate();
  const [academicYears, setAcademicYears] = useState([]);
  const [classes, setClasses] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchInitialData = useCallback(async () => {
    try {
      setLoading(true);
      const [yearRes, classRes] = await Promise.all([
        academicYearApi.getAcademicYears(),
        homeroomAssigntmentApi.getHomeroomClasses()
      ]);
      const years = yearRes.data?.data || [];
      setAcademicYears(years);
      setClasses(classRes.data?.data || []);
      if (years.length > 0) {
        const activeYear = years.find(y => y.active) || years[0];
        setSelectedYearId(activeYear.id);
      }
    } catch {
      message.error("Lỗi khi tải dữ liệu trang chủ nhiệm");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    Promise.resolve().then(() => fetchInitialData());
  }, [fetchInitialData]);

  const displayClasses = useMemo(() => {
    if (!selectedYearId) return [];
    return classes.filter((c) => c.academicYearId === selectedYearId);
  }, [classes, selectedYearId]);

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const columns = useMemo(() => [
    { 
      title: "TÊN LỚP", 
      dataIndex: "name", 
      key: "name", 
      width: 150,
      render: (text) => <Text strong>{text}</Text>,
    },
    { 
      title: "KHỐI", 
      dataIndex: "gradeLevel", 
      key: "gradeLevel", 
      width: 120,
      align: "center",
      render: (grade) => <Text>Khối {grade}</Text>,
    },
    { 
      title: "SĨ SỐ", 
      dataIndex: "totalStudents", 
      key: "totalStudents", 
      width: 120,
      align: "center",
      render: (total) => <Text>{total} HS</Text>,
    },
    {
      title: "NĂM HỌC",
      key: "academicYear",
      align: "center",
      render: () => {
        const year = academicYears.find(y => y.id === selectedYearId);
        return year ? year.name : "N/A";
      }
    },
    {
      title: "THAO TÁC",
      key: "action",
      align: "center",
      render: (_, record) => (
        <Button
          size="small"
          icon={<EyeOutlined />}
          onClick={() => {
            const year = academicYears.find(y => y.id === selectedYearId);
            navigate(`/teacher/homeroom-classes/${record.id}/students`, {
              state: {
                classId: record.id,
                className: record.name,
                isYearLocked: !year?.active, 
                academicYearId: selectedYearId,
              }
            });
          }}
        >
          Chi tiết lớp
        </Button>
      ),
    },
  ], [academicYears, selectedYearId, navigate]);

  return (
    <Card
      variant={false} 
      title={
        <Title level={3} style={{ margin: 0 }}>
          Lớp chủ nhiệm
        </Title>
      }
    >
      <Space orientation="vertical" size="middle" style={{ display: "flex", width: "100%" }}>
        
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
            <span style={{ fontWeight: 500, marginRight: 8 }}>Năm học:</span>
            <Select
              value={selectedYearId}
              onChange={setSelectedYearId}
              style={{ width: 250 }}
              loading={loading}
              options={academicYears.map((year) => ({
                value: year.id,
                label: year.name,
              }))}
            />
          </Space>
        </div>

        <Table
          columns={columns}
          dataSource={displayClasses}
          rowKey="id"
          loading={loading}
          pagination={false}
          bordered
          size="small" 
        />
      </Space>
    </Card>
  );
};

export default TeacherHomeroomAssignmentPage;
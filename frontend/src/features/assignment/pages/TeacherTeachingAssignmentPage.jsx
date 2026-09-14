import { useState, useEffect, useCallback, useMemo } from "react";
import { Card, Typography, Table, Select, Space, Button, message, Tag, Alert } from "antd";
import { EditOutlined, LockOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import academicYearApi from "../../academic/api/academicYearApi";
import teachingAssignmentApi from "../api/teachingAssignmentApi";

const { Title, Text } = Typography;

const TeacherTeachingAssignmentPage = () => {
  const navigate = useNavigate();
  const [academicYears, setAcademicYears] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [loading, setLoading] = useState(false);
  
  const fetchAcademicYears = useCallback(async () => {
    try {
      setLoading(true);
      const yearRes = await academicYearApi.getAcademicYears();
      const years = yearRes.data?.data || yearRes.data || [];
      setAcademicYears(years);
      
      if (years.length > 0) {
        const activeYear = years.find((y) => y.active) || years[0];
        setSelectedYearId(activeYear.id);
      }
    } catch (error) {
      console.error(error);
      message.error("Lỗi khi tải danh sách năm học!");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    Promise.resolve().then(() => fetchAcademicYears());
  }, [fetchAcademicYears]);

  const fetchAssignments = useCallback(async (yearId) => {
    if (!yearId) return;
    try {
      setLoading(true);
      const assignRes = await teachingAssignmentApi.getTeachingClasses(yearId);
      setAssignments(assignRes.data?.data || []);
    } catch (error) {
      console.error(error);
      message.error("Lỗi khi tải danh sách phân công giảng dạy!");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (selectedYearId) {
      Promise.resolve().then(() => fetchAssignments(selectedYearId));
    }
  }, [selectedYearId, fetchAssignments]);

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const columns = useMemo(() => [
    {
      title: "LỚP HỌC",
      dataIndex: "schoolClassName",
      key: "schoolClassName",
      width: 150,
      render: (text) => <Text strong>{text}</Text>,
    },
    {
      title: "MÔN DẠY",
      dataIndex: "subjectName",
      key: "subjectName",
      width: 250,
      render: (text) => <Tag color="blue">{text}</Tag>,
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
          icon={<EditOutlined />}
          onClick={() => {
            const year = academicYears.find(y => y.id === selectedYearId);
            navigate(`/teacher/teaching-classes/${record.schoolClassId}/grades`, {
              state: {
                classId: record.schoolClassId,
                className: record.schoolClassName,
                subjectId: record.subjectId,
                subjectName: record.subjectName,
                isYearLocked: !year?.active,
                academicYearId: selectedYearId,
              },
            });
          }}
        >
          Vào sổ điểm
        </Button>
      ),
    },
  ], [academicYears, selectedYearId, navigate]); // 🔥 Cập nhật mảng dependencies để render đúng năm học

  return (
    <Card
      variant={false}
      title={
        <Title level={3} style={{ margin: 0 }}>
          Danh sách các lớp giảng dạy
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
              style={{ width: 250 }}
              value={selectedYearId}
              onChange={setSelectedYearId}
              loading={loading}
              options={academicYears.map((y) => ({ value: y.id, label: y.name }))}
            />
          </Space>
        </div>

        <Table
          rowKey={(record) => `${record.schoolClassId}_${record.subjectId}`}
          columns={columns}
          dataSource={assignments}
          loading={loading}
          pagination={false}
          bordered
          size="small" 
        />
      </Space>
    </Card>
  );
};

export default TeacherTeachingAssignmentPage;
import { useState, useEffect } from "react";
import { Space, Select, Button, Table, message, Typography, Alert } from "antd";
import { SaveOutlined, LockOutlined } from "@ant-design/icons";
import academicYearApi from "../../academic/api/academicYearApi";
import teacherApi from "../../people/api/teacherApi";
import homeroomAssigntmentApi from "../api/homeroomAssignmentApi";

const { Text } = Typography;

const normalizeString = (str) => {
  if (!str) return "";
  return str
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .replace(/đ/g, "d")
    .replace(/Đ/g, "D");
};

const GRADE_OPTIONS = [
  { value: "ALL", label: "Tất cả khối" },
  { value: 10, label: "Khối 10" },
  { value: 11, label: "Khối 11" },
  { value: 12, label: "Khối 12" },
];

const HomeroomAssignmentTab = () => {
  const [academicYears, setAcademicYears] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [selectedGrade, setSelectedGrade] = useState("ALL");
  const [classes, setClasses] = useState([]);
  const [activeTeachers, setActiveTeachers] = useState([]);
  const [assignments, setAssignments] = useState({});
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [yearRes, teacherRes] = await Promise.all([
          academicYearApi.getAcademicYears(),
          teacherApi.getActiveTeachers(),
        ]);

        const years = yearRes.data?.data || [];
        setAcademicYears(years);
        if (years.length > 0) setSelectedYearId(years[0].id);

        const teachers = teacherRes.data?.data || [];
        const teacherOptions = teachers.map((t) => ({
          value: t.id,
          label: t.teacherName
            ? `${t.teacherName} (ID: ${t.id})`
            : "Không xác định",
        }));
        setActiveTeachers(teacherOptions);
      } catch {
        message.error("Lỗi tải dữ liệu ban đầu!");
      }
    };
    fetchInitialData();
  }, []);

  useEffect(() => {
    if (!selectedYearId) return;

    const fetchDataForYear = async () => {
      setLoading(true);
      try {
        const [classRes, assignRes] = await Promise.all([
          academicYearApi.getClassesByAcademicYearId(selectedYearId),
          homeroomAssigntmentApi.getExistingAssignments(selectedYearId)
        ]);
        const classData = classRes.data?.data || [];
        setClasses(classData);
        const existingData = assignRes.data?.data || [];
        const loadedAssignments = {};
        
        existingData.forEach((item) => {
          loadedAssignments[item.classId] = {
            assignmentId: item.id, 
            teacherId: item.teacherId,
          };
        });
        setAssignments(loadedAssignments);
      } catch {
        message.error("Lỗi tải dữ liệu!");
      } finally {
        setLoading(false);
      }
    };

    fetchDataForYear();
  }, [selectedYearId]);

  const handleSave = async () => {
    const assignmentList = [];
    Object.keys(assignments).forEach((classId) => {
      const teacherData = assignments[classId];
      assignmentList.push({
        classId: Number(classId),
        teacherId: teacherData?.teacherId || null,
      });
    });

    if (assignmentList.length === 0)
      return message.warning("Chưa có thông tin thay đổi để lưu!");

    const finalPayload = {
      academicYearId: selectedYearId,
      assignments: assignmentList
    };

    setSaving(true);
    try {
      await homeroomAssigntmentApi.assignHomeroomTeachers(finalPayload);
      message.success("Đã lưu danh sách chủ nhiệm thành công!");
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi lưu phân công!");
    } finally {
      setSaving(false);
    }
  };

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const displayClasses =
    selectedGrade === "ALL"
      ? classes
      : classes.filter((c) => c.gradeLevel === selectedGrade);

  const allAssignedTeacherIds = Object.values(assignments)
    .map((a) => a?.teacherId)
    .filter((id) => id != null);

  const columns = [
    {
      title: "LỚP",
      dataIndex: "name",
      width: 150,
      render: (text) => <Text strong>{text}</Text>,
    },
    {
      title: "KHỐI",
      dataIndex: "gradeLevel",
      width: 150,
      render: (grade) => <Text type="secondary">Khối {grade}</Text>,
    },
    {
      title: "GIÁO VIÊN CHỦ NHIỆM",
      dataIndex: "id",
      render: (classId) => {
        const cellData = assignments[classId];
        const currentTeacherId = cellData?.teacherId;

        const availableTeacherOptions = activeTeachers.filter(
          (t) => !allAssignedTeacherIds.includes(t.value) || t.value === currentTeacherId
        );

        return (
          <Select
            style={{ width: "100%" }}
            placeholder="--Chưa phân công--"
            value={currentTeacherId || null}
            options={availableTeacherOptions}
            allowClear
            disabled={isYearLocked}
            
            showSearch={{
              filterOption: (input, option) => {
                const searchInput = normalizeString(input);
                const optionLabel = normalizeString(option?.label);
                return optionLabel.includes(searchInput);
              }
            }}

            onChange={(val, option) => {
              setAssignments((prev) => ({
                ...prev,
                [classId]: val
                  ? { teacherId: val, teacherName: option.label }
                  : null,
              }));
            }}
          />
        );
      },
    },
  ];

  return (
    <Space
      orientation="vertical"
      size="middle"
      style={{ display: "flex", width: "100%" }}
    >
      {isYearLocked && (
        <Alert
          message={`Năm học ${currentYearObj?.name} đã đóng`}
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
            style={{ width: 150 }}
            value={selectedYearId}
            onChange={setSelectedYearId}
            options={academicYears.map((y) => ({ value: y.id, label: y.name }))}
          />
          <Select
            style={{ width: 150 }}
            value={selectedGrade}
            onChange={setSelectedGrade}
            options={GRADE_OPTIONS}
          />
        </Space>

        {!isYearLocked && (
          <Space wrap>
            <Button
              type="primary"
              icon={<SaveOutlined />}
              style={{ backgroundColor: "#1677ff" }}
              onClick={handleSave}
              loading={saving}
            >
              Lưu phân công
            </Button>
          </Space>
        )}
      </div>

      <Table
        columns={columns}
        dataSource={displayClasses}
        rowKey="id"
        pagination={false}
        bordered
        size="small"
        scroll={{ x: "max-content", y: "calc(100vh - 350px)" }}
        loading={loading}
      />
    </Space>
  );
};

export default HomeroomAssignmentTab;
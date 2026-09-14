import { useState, useEffect } from "react";
import {
  Space,
  Select,
  Button,
  Table,
  message,
  Typography,
  Badge,
  Alert,
} from "antd";
import {
  RobotOutlined,
  CopyOutlined,
  SaveOutlined,
  LockOutlined,
} from "@ant-design/icons";
import academicYearApi from "../../academic/api/academicYearApi";
import subjectApi from "../../academic/api/subjectApi";
import teacherExpertiseApi from "../../people/api/teacherExpertiseApi";
import teachingAssignmentApi from "../api/teachingAssignmentApi";

const { Text } = Typography;

const GRADE_OPTIONS = [
  { value: "ALL", label: "Tất cả khối" },
  { value: 10, label: "Khối 10" },
  { value: 11, label: "Khối 11" },
  { value: 12, label: "Khối 12" },
];

const getWorkloadColor = (workload) => {
  if (!workload || workload === 0) return "blue";
  if (workload <= 3) return "green";
  if (workload <= 5) return "orange";
  return "red";
};

const TeachingAssignmentTab = () => {
  const [academicYears, setAcademicYears] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [selectedGrade, setSelectedGrade] = useState("ALL");
  const [classes, setClasses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [teachersBySubject, setTeachersBySubject] = useState({});
  const [initialAssignments, setInitialAssignments] = useState({});
  const [assignments, setAssignments] = useState({});
  const [isMatrixEmpty, setIsMatrixEmpty] = useState(false);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [yearRes, subjectRes] = await Promise.all([
          academicYearApi.getAcademicYears(),
          subjectApi.getSubjects(),
        ]);
        const years = yearRes.data?.data || [];
        setAcademicYears(years);
        if (years.length > 0) setSelectedYearId(years[0].id);
        setSubjects(subjectRes.data?.data || []);
      } catch {
        message.error("Lỗi tải dữ liệu!");
      }
    };
    fetchInitialData();
  }, []);

  useEffect(() => {
    if (!selectedYearId) return;
    const fetchDataForYear = async () => {
      setLoading(true);
      try {
        const [classRes, teacherRes] = await Promise.all([
          academicYearApi.getClassesByAcademicYearId(selectedYearId),
          teacherExpertiseApi.getTeacherExpertisesWithWorkload(selectedYearId),
        ]);

        setClasses(classRes.data?.data || []);

        const expertises = teacherRes.data?.data || [];
        const groupedTeachers = {};
        expertises.forEach((item) => {
          if (!groupedTeachers[item.subjectId]) {
            groupedTeachers[item.subjectId] = [];
          }
          groupedTeachers[item.subjectId].push({
            value: item.teacher.teacherId,
            label: item.teacher.teacherName,
            baseWorkload: item.teacher.workload, 
          });
        });
        setTeachersBySubject(groupedTeachers);
      } catch {
        message.error("Lỗi tải dữ liệu!");
      } finally {
        setLoading(false);
      }
    };
    fetchDataForYear();
  }, [selectedYearId]);

  useEffect(() => {
    if (!selectedYearId || classes.length === 0) return;
    const fetchExistingAssignments = async () => {
      setLoading(true);
      try {
        const gradeList = selectedGrade === "ALL" ? [10, 11, 12] : [selectedGrade];
        const res = await teachingAssignmentApi.getExistingAssignments(
          selectedYearId,
          gradeList.join(","),
        );
        const data = res.data?.data || [];
        setIsMatrixEmpty(data.length === 0);

        const loadedAssignments = {};
        data.forEach((item) => {
          const cellKey = `${item.classId}_${item.subjectId}`;
          loadedAssignments[cellKey] = {
            teacherId: item.teacherId,
            teacherName: item.teacherName,
          };
        });

        setAssignments(loadedAssignments);
        setInitialAssignments(loadedAssignments); 
      } catch {
        message.error("Lỗi khi tải dữ liệu phân công!");
      } finally {
        setLoading(false);
      }
    };
    fetchExistingAssignments();
  }, [selectedYearId, selectedGrade, classes]);

  const handleCopyAssignmentsPreviousYear = async () => {
    setLoading(true);
    try {
      const gradeList = selectedGrade === "ALL" ? [10, 11, 12] : [selectedGrade];
      const res = await teachingAssignmentApi.copyAssignmentsPreviousYear(
        selectedYearId,
        gradeList.join(","),
      );
      const data = res.data?.data || [];
      const newAssignments = { ...assignments };
      data.forEach((item) => {
        const cellKey = `${item.classId}_${item.subjectId}`;
        newAssignments[cellKey] = {
          teacherId: item.teacherId,
          teacherName: item.teacherName,
        };
      });
      setAssignments(newAssignments);
      setIsMatrixEmpty(false);
      message.success(
        "Đã sao chép dữ liệu nháp từ năm trước. Vui lòng kiểm tra và Lưu lại!",
      );
    } catch (error) {
      message.error(
        error.response?.data?.message || "Lỗi sao chép từ năm trước!",
      );
    } finally {
      setLoading(false);
    }
  };

  const handleAutoAssign = async () => {
    setLoading(true);
    try {
      const gradeList = selectedGrade === "ALL" ? [10, 11, 12] : [selectedGrade];
      const res = await teachingAssignmentApi.previewAutoAssign(
        selectedYearId,
        gradeList.join(","),
      );
      const data = res.data?.data || [];
      const newAssignments = { ...assignments };
      data.forEach((item) => {
        const cellKey = `${item.classId}_${item.subjectId}`;
        newAssignments[cellKey] = {
          teacherId: item.teacherId,
          teacherName: item.teacherName,
        };
      });
      setAssignments(newAssignments);
      setIsMatrixEmpty(false);
      message.success(`Đã xếp lịch tự động. Kiểm tra và lưu lại.`);
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi phân công tự động!");
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    const payload = [];
    Object.keys(assignments).forEach((key) => {
      const [classId, subjectId] = key.split("_");
      const teacher = assignments[key];
      payload.push({
        classId: Number(classId),
        subjectId: Number(subjectId),
        teacherId: teacher?.teacherId || null,
      });
    });

    if (payload.length === 0)
      return message.warning("Chưa có dữ liệu nào để lưu!");
    
    const finalPayload = {
      academicYearId: selectedYearId,
      assignments: payload,
    };

    setSaving(true);
    try {
      await teachingAssignmentApi.saveBulkTeachingAssignments(finalPayload);
      message.success("Đã lưu dữ liệu phân công giảng dạy thành công!");
      setInitialAssignments(assignments);
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi lưu phân công!");
    } finally {
      setSaving(false);
    }
  };

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const workloadDeltas = {};
  Object.values(initialAssignments).forEach((a) => {
    if (a?.teacherId)
      workloadDeltas[a.teacherId] = (workloadDeltas[a.teacherId] || 0) - 1;
  });
  Object.values(assignments).forEach((a) => {
    if (a?.teacherId)
      workloadDeltas[a.teacherId] = (workloadDeltas[a.teacherId] || 0) + 1;
  });

  const displayClasses =
    selectedGrade === "ALL"
      ? classes
      : classes.filter((c) => c.gradeLevel === selectedGrade);

  const columns = [
    {
      title: "LỚP",
      dataIndex: "name",
      width: 100,
      fixed: "left",
      align: "center",
      render: (text) => <Text strong>{text}</Text>,
    },
    ...subjects.map((subject) => ({
      title: subject.name,
      dataIndex: `subject_${subject.id}`,
      width: 250,
      render: (_, recordClass) => {
        const cellKey = `${recordClass.id}_${subject.id}`;
        const cellData = assignments[cellKey];
        const rawTeachers = teachersBySubject[subject.id] || [];
        const teacherOptions = rawTeachers.map((t) => ({
          value: t.value,
          label: t.label,
          workload: Math.max(
            0,
            t.baseWorkload + (workloadDeltas[t.value] || 0),
          ),
        }));

        return (
          <Select
            style={{ width: "100%" }}
            placeholder="--Chưa phân công--"
            value={cellData?.teacherId || null}
            options={teacherOptions}
            allowClear
            disabled={isYearLocked} 
            labelRender={({ label, value }) => {
              const optionData = teacherOptions.find(
                (opt) => opt.value === value,
              );
              return optionData ? (
                <Badge
                  color={getWorkloadColor(optionData.workload)}
                  text={label}
                />
              ) : (
                label
              );
            }}
            optionRender={(option) => (
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <Badge
                  color={getWorkloadColor(option.data.workload)}
                  text={option.data.label}
                />
                <span style={{ color: "#999", fontSize: "12px" }}>
                  {option.data.workload} lớp
                </span>
              </div>
            )}
            onChange={(val, option) => {
              setAssignments((prev) => ({
                ...prev,
                [cellKey]: val
                  ? { teacherId: val, teacherName: option.label }
                  : null,
              }));
            }}
          />
        );
      },
    })),
  ];

  return (
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
            {isMatrixEmpty && (
              <Button
                type="dashed"
                icon={<CopyOutlined />}
                onClick={handleCopyAssignmentsPreviousYear}
                loading={loading}
              >
                Sao chép năm trước
              </Button>
            )}
            <Button
              icon={<RobotOutlined />}
              onClick={handleAutoAssign}
              loading={loading}
            >
              Phân công tự động
            </Button>
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

export default TeachingAssignmentTab;
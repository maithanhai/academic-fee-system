import { useState, useEffect } from "react";
import { Table, Typography, Space, Spin, message, Button, Card, Select, Alert } from "antd";
import { useLocation, useNavigate } from "react-router-dom";
import { LockOutlined } from "@ant-design/icons";
import gradeApi from "../api/gradeApi";
import academicYearApi from "../../academic/api/academicYearApi";
import GradeInputCell from "../components/GradeInputCell";

const { Text } = Typography;

const EXAM_TYPE_LABELS = {
  MIENG: "Điểm miệng",
  PHUT_15: "Điểm 15 phút",
  TIET_1: "Điểm 1 tiết",
  HOC_KY: "Điểm học kỳ",
};

const SEMESTER_LABELS = {
  FIRST_SEMESTER: "Học kỳ 1",
  SECOND_SEMESTER: "Học kỳ 2",
};

const StudentsGradeTablePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { classId, className, subjectId, subjectName, isYearLocked, academicYearId } = location.state || {};
  const [loading, setLoading] = useState(false);
  const [tableData, setTableData] = useState({ columns: [], rows: [] });
  const [semesters, setSemesters] = useState([]);
  const [semesterId, setSemesterId] = useState(null);

  useEffect(() => {
    const fetchSemesters = async () => {
      try {
        const res = await academicYearApi.getSemesters(academicYearId);
        const data = res.data?.data || [];
        setSemesters(data);
        if (data.length > 0) {
          setSemesterId(data[0].id); 
        }
      } catch (error) {
        console.error(error);
        message.error("Lỗi tải danh sách học kỳ!");
      }
    };

    fetchSemesters();
  }, [academicYearId]);

  useEffect(() => {
    if (!classId || !subjectId) {
      message.error("Thiếu thông tin lớp hoặc môn học!");
      navigate("/teacher/teaching-classes");
      return;
    }
    if (!semesterId) return; 

    const loadMatrixTable = async () => {
      setLoading(true);
      try {
        const res = await gradeApi.getGradeTable(classId, subjectId, semesterId);
        const data = res.data?.data || { columns: [], rows: [] };
        if (data.rows && data.rows.length > 0) {
          data.rows.sort((a, b) => {
            const getFirstName = (fullName) => {
              if (!fullName) return "";
              const parts = fullName.trim().split(" ");
              return parts[parts.length - 1];
            };
            const nameA = getFirstName(a.studentName);
            const nameB = getFirstName(b.studentName);
            const compareName = nameA.localeCompare(nameB, "vi");
            if (compareName === 0) {
              return a.studentName.localeCompare(b.studentName, "vi");
            }
            return compareName;
          });
        }

        setTableData(data);
      } catch (error) {
        console.error(error);
        message.error("Lỗi khi tải bảng điểm!");
      } finally {
        setLoading(false);
      }
    };

    loadMatrixTable();
  }, [classId, subjectId, semesterId, navigate]);

  const tableColumns = [
    {
      title: "STT",
      key: "stt",
      width: 60,
      align: "center",
      render: (_, __, index) => index + 1,
      fixed: "left",
    },
    {
      title: "Họ và tên",
      dataIndex: "studentName",
      width: 200,
      fixed: "left",
      render: (text) => <Text strong>{text}</Text>,
    },
  ];

  tableData.columns?.forEach((colConfig) => {
    const label = EXAM_TYPE_LABELS[colConfig.examType] || colConfig.examType;
    
    // Khối tiêu đề chính 
    const titleContent = (
      <div style={{ textAlign: "center" }}>
        <span>{label}</span>
        <br />
        <Text type="secondary" style={{ fontSize: 12, fontWeight: "normal" }}>
          (Hệ số {colConfig.coefficient})
        </Text>
      </div>
    );

    // TRƯỜNG HỢP 1: Cột có 1 đầu điểm (ví dụ: Điểm học kỳ) -> Không đánh số 1, 2
    if (colConfig.maxColumn === 1) {
      tableColumns.push({
        title: titleContent,
        align: "center",
        width: 100,
        render: (_, record) => {
          const gradesForThisType = record.scores?.[colConfig.examType] || [];
          const currentGrade = gradesForThisType.find((g) => g.ordinalNumber === 1);
          return (
            <GradeInputCell
              // Key động giúp ép React tự tạo ô mới khi dữ liệu đổi (khắc phục lỗi lưu mà giao diện kẹt điểm cũ)
              key={`${semesterId}_${record.studentId}_${colConfig.examType}_1_${currentGrade?.score ?? "null"}`}
              initialScore={currentGrade?.score ?? null}
              classId={classId}
              subjectId={subjectId}
              semesterId={semesterId}
              studentId={record.studentId}
              examType={colConfig.examType}
              ordinalNumber={1}
              isLocked={isYearLocked}
            />
          );
        },
      });
    } 
    // TRƯỜNG HỢP 2: Cột có nhiều đầu điểm -> Đánh số cột con 1, 2, 3... như vnEdu
    else {
      const subColumns = Array.from({ length: colConfig.maxColumn }).map((_, index) => {
        const ordinal = index + 1;
        return {
          title: `${ordinal}`,
          key: `${colConfig.examType}_${ordinal}`,
          align: "center",
          width: 75,
          render: (_, record) => {
            const gradesForThisType = record.scores?.[colConfig.examType] || [];
            const currentGrade = gradesForThisType.find((g) => g.ordinalNumber === ordinal);
            
            return (
              <GradeInputCell
                key={`${semesterId}_${record.studentId}_${colConfig.examType}_${ordinal}_${currentGrade?.score ?? "null"}`}
                initialScore={currentGrade?.score ?? null}
                classId={classId}
                subjectId={subjectId}
                semesterId={semesterId}
                studentId={record.studentId}
                examType={colConfig.examType}
                ordinalNumber={ordinal}
                isLocked={isYearLocked}
              />
            );
          },
        };
      });

      tableColumns.push({
        title: titleContent,
        children: subColumns,
      });
    }
  });

  return (
    <Card
      variant={false}
      title={`Sổ điểm môn ${subjectName || ""} - Lớp ${className || ""}`}
      extra={
        <Button onClick={() => navigate("/teacher/teaching-classes")}>
          Quay lại
        </Button>
      }
    >
      <Space
        orientation="vertical"
        size="middle"
        style={{ display: "flex", width: "100%" }}
      >
        {isYearLocked && (
          <Alert
            title="Năm học này đã đóng" 
            type="warning"
            showIcon
            icon={<LockOutlined />}
          />
        )}

        <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
          <span style={{ fontWeight: 500 }}>Học kỳ:</span>
          <Select
            value={semesterId}
            onChange={(value) => setSemesterId(value)}
            style={{ width: 250 }}
            loading={semesters.length === 0}
            options={semesters.map((s) => ({
              value: s.id,
              label: SEMESTER_LABELS[s.name] || s.name,
            }))}
          />
        </div>
        
        <Spin spinning={loading}>
          <Table
            key={semesterId}
            columns={tableColumns}
            dataSource={tableData.rows}
            rowKey="studentId"
            pagination={false}
            bordered
            size="small"
            scroll={{ x: "max-content", y: "calc(100vh - 300px)" }}
          />
        </Spin>
      </Space>
    </Card>
  );
};

export default StudentsGradeTablePage;
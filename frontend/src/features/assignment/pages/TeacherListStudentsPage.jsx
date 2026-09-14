import { useState, useEffect, useCallback, useMemo } from "react";
import { Card, Typography, Table, Button, message, Space } from "antd";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import homeroomAssigntmentApi from "../api/homeroomAssignmentApi";
import StudentInvoiceDrawer from "../components/StudentInvoiceDrawer";
import StudentTranscriptModal from "../../grade/components/StudentTranscriptModal";
import { toDisplayDate } from "../../../shared/utils/dateUtils";

const { Title } = Typography;

const TeacherListStudentsPage = () => {
  const params = useParams();
  const location = useLocation();
  const classId = params.id || params.classId;
  const navigate = useNavigate();
  const academicYearId = location.state?.academicYearId;
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isTranscriptModalOpen, setIsTranscriptModalOpen] = useState(false);
  const [isFeeDrawerOpen, setIsFeeDrawerOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const fetchStudents = useCallback(async () => {
    if (!classId) return;
    try {
      setLoading(true);
      const res = await homeroomAssigntmentApi.getStudentsByClassId(classId);
      setStudents(res.data?.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách học sinh");
    } finally {
      setLoading(false);
    }
  }, [classId]);

  useEffect(() => {
    Promise.resolve().then(() => fetchStudents());
  }, [fetchStudents]);

  const sortedStudents = useMemo(() => {
    return [...students].sort((a, b) => {
      const getFirstName = (fullName) => {
        if (!fullName) return "";
        const parts = fullName.trim().split(" ");
        return parts[parts.length - 1];
      };
      const nameA = getFirstName(a.studentName);
      const nameB = getFirstName(b.studentName);
      return nameA.localeCompare(nameB, 'vi'); 
    });
  }, [students]);

  const openTranscriptModal = useCallback((record) => {
    setSelectedStudent(record);
    setIsTranscriptModalOpen(true);
  }, []);

  const openFeeDrawer = useCallback((record) => {
    setSelectedStudent(record);
    setIsFeeDrawerOpen(true);
  }, []);

  const columns = useMemo(() => [
    { title: "STT", key: "index", render: (_, __, index) => index + 1, width: 60, align: "center" },
    { title: "Mã HS", dataIndex: "studentId", key: "studentId", width: 100 },
    { title: "Họ và tên", dataIndex: "studentName", key: "studentName" },
    { 
      title: "Giới tính", 
      dataIndex: "gender", 
      key: "gender",
      render: (val) => val === "MALE" ? "Nam" : (val === "FEMALE" ? "Nữ" : val)
    },
    { title: "Ngày sinh", dataIndex: "dayOfBirth", key: "dayOfBirth" ,render:(date)=>(toDisplayDate(date))},
    {
      title: "Thao tác",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="primary" size="small" onClick={() => openTranscriptModal(record)}>
            Xem bảng điểm
          </Button>
          <Button style={{ backgroundColor: '#52c41a', color: 'white' }} size="small" onClick={() => openFeeDrawer(record)}>
            Các khoản phí
          </Button>
        </Space>
      ),
    },
  ], [openTranscriptModal, openFeeDrawer]);

  return (
    <div className="teacher-students-page">
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 24 }}>
        <Title level={3} style={{ margin: 0 }}>Danh sách học sinh</Title>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>

      <Card variant={false}>
        <Table 
          columns={columns} 
          dataSource={sortedStudents} 
          rowKey="studentId" 
          loading={loading}
          pagination={false}
          bordered
        />

        <StudentTranscriptModal
          open={isTranscriptModalOpen}
          onClose={() => setIsTranscriptModalOpen(false)}
          studentId={selectedStudent?.studentId}
          classId={classId} 
          fetchData={()=>homeroomAssigntmentApi.getStudentTranscript(classId,selectedStudent?.studentId)}        
        />
        
        <StudentInvoiceDrawer
          open={isFeeDrawerOpen}
          onClose={() => setIsFeeDrawerOpen(false)}
          student={selectedStudent}
          classId={classId} 
          academicYearId={academicYearId}
        />
      </Card>
    </div>
  );
};

export default TeacherListStudentsPage;
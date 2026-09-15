import { useState, useEffect, useCallback, useMemo } from 'react';
import { Card, Typography, Table, Button, Space, message } from 'antd';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import classApi from '../api/classApi';
import EnrollStudentModal from '../components/EnrollStudentModel';
import TransferClassModal from '../components/TransferClassModal';
import StudentTranscriptModal from '../../grade/components/StudentTranscriptModal';
import gradeApi from '../../grade/api/gradeApi';
import {toDisplayDate} from "../../../shared/utils/dateUtils"

const { Title } = Typography;

const AdminClassDetailPage = () => {
  const { id: classId } = useParams();
  const location = useLocation();
  const navigate = useNavigate(); 
  const academicYearId = location.state?.academicYearId; 

  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isEnrollModalOpen, setIsEnrollModalOpen] = useState(false);
  const [isTransferModalOpen, setIsTransferModalOpen] = useState(false);
  const [isTranscriptModalOpen, setIsTranscriptModalOpen] = useState(false);
  const [selectedEnrollmentId, setSelectedEnrollmentId] = useState(null);
  const [selectedStudent, setSelectedStudent] = useState(null);

  const fetchStudents = useCallback(async () => {
    try {
      setLoading(true);
      const res = await classApi.getStudentsByClassId(classId);
      setStudents(res.data?.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách học sinh");
    } finally {
      setLoading(false);
    }
  }, [classId]);

  useEffect(() => {
    if (classId) {
      Promise.resolve().then(() => fetchStudents());
    }
  }, [classId, fetchStudents]);

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

  const openTransferModal = useCallback((enrollmentId) => {
    setSelectedEnrollmentId(enrollmentId);
    setIsTransferModalOpen(true);
  }, []);

  const openTranscriptModal = useCallback((record) => {
    setSelectedStudent(record);
    setIsTranscriptModalOpen(true);
  }, []);

  const columns = useMemo(() => [
    { 
      title: "STT", 
      key: "index", 
      render: (_, __, index) => index + 1, 
      width: 60, 
      align: "center" 
    },
    { 
      title: "Mã HS", 
      dataIndex: "studentId", 
      key: "studentId", 
      width: 100 
    },
    { 
      title: 'Họ và tên', 
      dataIndex: 'studentName', 
      key: 'studentName' 
    },
    { 
      title: 'Giới tính', 
      dataIndex: 'gender', 
      key: 'gender',
      render: (val) => val === 'MALE' ? 'Nam' : (val === 'FEMALE' ? 'Nữ' : val)
    },
    { 
      title: 'Ngày sinh', 
      dataIndex: 'dayOfBirth', 
      key: 'dayOfBirth',
      render: (val) => toDisplayDate(val)
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button size="small" onClick={() => openTransferModal(record.enrollmentId)}>
            Chuyển lớp
          </Button>
          <Button type="primary" size="small" onClick={() => openTranscriptModal(record)}>
            Xem bảng điểm
          </Button>
        </Space>
      ),
    },
  ], [openTransferModal, openTranscriptModal]);

  return (
    <div className="admin-class-detail-page">
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 24,
        }}
      >
        <Title level={3} style={{ margin: 0 }}>Danh sách học sinh</Title>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>

      <Card 
        variant={false} 
        extra={
          <Button type="primary" onClick={() => setIsEnrollModalOpen(true)}>
            Thêm học sinh vào lớp
          </Button>
        }
      >
        <div style={{ marginBottom: 16 }}></div>

        <Table 
          columns={columns} 
          dataSource={sortedStudents} 
          rowKey="enrollmentId" 
          loading={loading}
          bordered
          pagination={false}
        />

        <EnrollStudentModal 
          open={isEnrollModalOpen}
          onClose={() => setIsEnrollModalOpen(false)}
          classId={classId}
          academicYearId={academicYearId}
          onSuccess={fetchStudents} 
        />

        <TransferClassModal
          open={isTransferModalOpen}
          onClose={() => setIsTransferModalOpen(false)}
          enrollmentId={selectedEnrollmentId}
          academicYearId={academicYearId}
          onSuccess={fetchStudents}
        />

        <StudentTranscriptModal
          open={isTranscriptModalOpen}
          onClose={() => setIsTranscriptModalOpen(false)}
          studentId={selectedStudent?.studentId}
          classId={classId}
          fetchData={() => gradeApi.getTranscriptByAdmin(selectedStudent?.studentId,classId)}
        />
      </Card>
    </div>
  );
};

export default AdminClassDetailPage;
import { useState, useEffect, useCallback } from 'react';
import { Card, Table, Typography, Button, Tag, message } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import StudentTranscriptModal from '../components/StudentTranscriptModal';
import academicYearApi from '../../academic/api/academicYearApi';
import gradeApi from '../api/gradeApi';

const { Title } = Typography;

const StudentGradeTablePage = () => {
  const [academicYears, setAcademicYears] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedYear, setSelectedYear] = useState(null);

  const fetchEnrolledYears = useCallback(async () => {
    setLoading(true);
    try {
      const res = await academicYearApi.getAcademicYearsByStudent();
      setAcademicYears(res.data?.data || []);
    } catch {
      message.error('Lỗi khi tải danh sách năm học!');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    Promise.resolve().then(() =>fetchEnrolledYears());
  }, [fetchEnrolledYears]);

  const handleOpenTranscript = (record) => {
    setSelectedYear(record);
    setIsModalOpen(true);
  };

  const columns = [
    {
      title: 'STT',
      key: 'index',
      width: 70,
      align: 'center',
      render: (_, __, index) => index + 1,
    },
    {
      title: 'Năm học',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Trạng thái',
      dataIndex: 'active',
      key: 'active',
      align: 'center',
      width: 160,
      render: (active) => (
        <Tag color={active ? 'processing' : 'default'}>
          {active ? 'Đang diễn ra' : 'Đã kết thúc'}
        </Tag>
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      align: 'center',
      width: 180,
      render: (_, record) => (
        <Button
          type="primary"
          icon={<EyeOutlined />}
          size="small"
          onClick={() => handleOpenTranscript(record)}
        >
          Xem bảng điểm
        </Button>
      ),
    },
  ];

  return (
    <div className="student-grade-page">
      <Title level={3} style={{ marginBottom: 20 }}>
        Kết quả học tập các năm học
      </Title>

      <Card variant={false}>
        <Table
          columns={columns}
          dataSource={academicYears}
          rowKey="id"
          loading={loading}
          pagination={false}
          bordered
        />

        <StudentTranscriptModal
          open={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          fetchData={() => gradeApi.getTranscriptByStudent(selectedYear?.id)}
        />
      </Card>
    </div>
  );
};

export default StudentGradeTablePage;
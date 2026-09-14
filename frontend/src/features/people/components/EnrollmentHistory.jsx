import { Card, Table, Tag } from 'antd';

const EnrollmentHistory = ({ enrollments }) => {
  const columns = [
    { title: 'Lớp', dataIndex: 'className', render: (text) => <span style={{ fontWeight: 'bold', color: '#1677ff' }}>{text}</span> },
    { title: 'Khối', dataIndex: 'gradeLevel', align: 'center', render: (text) => <Tag>{text}</Tag> },
    { title: 'Trạng thái', dataIndex: 'status', align: 'center', render: (status) => <Tag color={status === 'ACTIVE' ? 'success' : status === 'TRANSFERRED' ? 'warning' : 'default'}>{status === 'ACTIVE' ? 'Đang học' : status === 'TRANSFERRED' ? 'Chuyển lớp' : 'Đã hoàn thành'}</Tag> },
  ];

  if (!enrollments) return null;
  return <Card title="Lịch sử xếp lớp" variant={false} className="shadow-sm" style={{ marginTop: 24 }}><Table columns={columns} dataSource={enrollments} rowKey={(record) => record.className} pagination={false} size="small" bordered /></Card>;
};

export default EnrollmentHistory;

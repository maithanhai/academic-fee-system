import { Card, Table, Tag } from 'antd';

const EnrollmentHistory = ({ enrollments }) => {
  if (!enrollments) return null;
  const columns = [
    { title: 'Lớp', dataIndex: 'className' },
    { title: 'Khối', dataIndex: 'gradeLevel', render: (value) => <Tag>{value}</Tag> },
    { title: 'Trạng thái', dataIndex: 'status', render: (status) => <Tag color={status === 'ACTIVE' ? 'success' : 'default'}>{status === 'ACTIVE' ? 'Đang học' : 'Đã hoàn thành'}</Tag> },
  ];
  return <Card title="Lịch sử học tập" variant={false} style={{ marginTop: 24 }}><Table columns={columns} dataSource={enrollments} rowKey={(record, index) => `${record.className}-${index}`} pagination={false} size="small" bordered /></Card>;
};

export default EnrollmentHistory;

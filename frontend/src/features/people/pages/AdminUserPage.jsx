import { useState } from 'react';
import { Card, Tabs, Typography } from 'antd';
import TeacherTab from '../components/TeacherTab';
import StudentTab from '../components/StudentTab';

const { Title } = Typography;

const AdminUserPage = () => {
  const [activeTab, setActiveTab] = useState('teachers');

  return (
    <Card variant={false} title={<Title level={3} style={{ margin: 0 }}>Quản lý tài khoản</Title>}>
      <Tabs
        activeKey={activeTab}
        onChange={setActiveTab}
        type="card"
        items={[
          { key: 'teachers', label: 'Giáo viên', children: <TeacherTab /> },
          { key: 'students', label: 'Học sinh', children: <StudentTab /> },
        ]}
      />
    </Card>
  );
};

export default AdminUserPage;

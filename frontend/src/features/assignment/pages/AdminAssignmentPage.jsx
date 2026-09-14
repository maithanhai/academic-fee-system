import { useState } from 'react';
import { Card, Tabs, Typography } from 'antd';
import TeachingAssignmentTab from '../components/TeachingAssignmentTab';
import HomeroomAssignmentTab from '../components/HomeroomAssignmentTab';

const { Title } = Typography;

const AdminAssignmentPage = () => {
  const [activeTab, setActiveTab] = useState('teaching');

  return (
    <Card variant={false} title={<Title level={3} style={{ margin: 0 }}>Quản lý Phân công</Title>}>
      <Tabs
        activeKey={activeTab}
        onChange={setActiveTab}
        type="card"
        items={[
          { key: 'teaching', label: 'Phân công Giảng dạy', children: <TeachingAssignmentTab /> },
          { key: 'homeroom', label: 'Phân công Chủ nhiệm', children: <HomeroomAssignmentTab /> },
        ]}
      />
    </Card>
  );
};

export default AdminAssignmentPage;
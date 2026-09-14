import { Card, Descriptions, Tag } from 'antd';

const UserSystemInfo = ({ userData, isTeacher }) => (
  <Card title="Thông tin hệ thống" variant={false} className="shadow-sm">
    <Descriptions column={1} size="small" bordered>
      <Descriptions.Item label="ID">{userData?.id}</Descriptions.Item>
      <Descriptions.Item label="Username"><span style={{ fontWeight: 'bold', color: '#1677ff' }}>{userData?.username}</span></Descriptions.Item>
      <Descriptions.Item label="Ngày tạo">{userData?.createdDate || '-'}</Descriptions.Item>
      <Descriptions.Item label="Cập nhật">{userData?.updatedDate || '-'}</Descriptions.Item>
      <Descriptions.Item label="Vai trò"><Tag color={isTeacher ? 'geekblue' : 'cyan'}>{isTeacher ? 'GIÁO VIÊN' : 'HỌC SINH'}</Tag></Descriptions.Item>
      {!isTeacher && userData?.cohort && <Descriptions.Item label="Khóa học"><Tag color="purple">{userData.cohort.name} ({userData.cohort.admissionYear})</Tag></Descriptions.Item>}
    </Descriptions>
  </Card>
);

export default UserSystemInfo;

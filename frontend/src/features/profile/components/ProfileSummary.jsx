import { Card, Descriptions, Space, Tag } from 'antd';
import {toDisplayDate} from "../../../shared/utils/dateUtils"

const ProfileSummary = ({ profile, role, isStudent, isTeacher }) => (
  <Card title="Thông tin cơ bản" variant={false} className="shadow-sm">
    <Descriptions column={1} bordered>
      <Descriptions.Item label="Họ và tên">{profile?.fullName}</Descriptions.Item>
      <Descriptions.Item label="Ngày sinh">{toDisplayDate(profile?.dateOfBirth) || '-'}</Descriptions.Item>
      <Descriptions.Item label="Giới tính">{profile?.gender === 'MALE' ? 'Nam' : profile?.gender === 'FEMALE' ? 'Nữ' : 'Khác'}</Descriptions.Item>
      <Descriptions.Item label="Vai trò"><Tag color={isStudent ? 'cyan' : isTeacher ? 'geekblue' : 'magenta'}>{role ? role.replace('ROLE_', '') : 'Đang cập nhật'}</Tag></Descriptions.Item>
      {isStudent && profile?.cohort && <Descriptions.Item label="Khóa học">{profile.cohort.name}</Descriptions.Item>}
      {isTeacher && <>
        <Descriptions.Item label="Khoa / Bộ môn">{profile?.department?.name || 'Chưa cập nhật'}</Descriptions.Item>
        <Descriptions.Item label="Chuyên môn"><Space wrap>{profile?.subjects?.map((subject) => <Tag key={subject.id} color="blue">{subject.name}</Tag>) || 'Chưa cập nhật'}</Space></Descriptions.Item>
      </>}
    </Descriptions>
  </Card>
);

export default ProfileSummary;

import { Card, Tabs, Typography } from "antd";
import AcademicYearTab from "../components/AcademicYearTab";
import CohortTab from "../components/CohortTab";
import DepartmentTab from "../components/DepartmentTab";
import SubjectTab from "../components/SubjectTab";
import { useState } from "react";
import { 
  AppstoreOutlined, 
  BookOutlined, 
  CalendarOutlined, 
  SettingOutlined, 
  TeamOutlined 
} from '@ant-design/icons';
import GradeConfigTab from "../../grade/components/GradeConfigTab";

const { Title } = Typography;
const AdminAcademicPage = () => {
  const [activeTab, setActiveTab] = useState("academic_year");

  const tabItems = [
    {
      key: "academic_year",
      label: (
        <span>
          <CalendarOutlined /> Năm học
        </span>
      ),
      children: <AcademicYearTab />,
    },
    {
      key: "cohort",
      label: (
        <span>
          <AppstoreOutlined /> Khóa học
        </span>
      ),
      children: <CohortTab />,
    },
    {
      key: "department",
      label: (
        <span>
          <TeamOutlined /> Tổ bộ môn
        </span>
      ),
      children: <DepartmentTab />,
    },
    {
      key: "subject",
      label: (
        <span>
          <BookOutlined /> Môn học
        </span>
      ),
      children: <SubjectTab />,
    },{
      key: "gradeConfig",
      label: (
        <span>
          <SettingOutlined /> Cấu hình điểm
        </span>
      ),
      children: <GradeConfigTab />,
    },
  ];
  return (
    <Card
      variant={false}
      title={
        <Title level={3} style={{ margin: 0 }}>
          Quản lý danh mục
        </Title>
      }
    >
      <Tabs
        activeKey={activeTab}
        onChange={setActiveTab}
        type="card"
        items={tabItems}
      />
    </Card>
  );
};

export default AdminAcademicPage;

import { useEffect, useState } from 'react';
import { 
  Table, Input, Select, Button, Space, Typography, 
  Avatar, message, Tabs, Card, Badge 
} from 'antd';
import { PlusOutlined, UploadOutlined, SearchOutlined, UserOutlined } from '@ant-design/icons';
import adminUserApi from '../../api/adminUserApi';
import classApi from '../../api/classApi';
import departmentApi from '../../api/departmentApi';
import { useNavigate } from 'react-router-dom';

const { Text, Title } = Typography;

const AdminUserPage = () => {
    const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState('teachers');
  const [teachers, setTeachers] = useState([]);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);

  const [teacherPagination, setTeacherPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  const [studentPagination, setStudentPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  const [teacherFilters, setTeacherFilters] = useState({ keyword: '', active: null, departmentId: null });
  const [studentFilters, setStudentFilters] = useState({ keyword: '', active: null, gradeLevel: null, classId: null });
  const [departments, setDepartments] = useState([]);
  const [classes, setClasses] = useState([]);

  useEffect(() => {
    const fetchDropdownData = async () => {
      try {
        const [deptResponse, classResponse] = await Promise.all([
          departmentApi.getDepartments(), 
          classApi.getClasses()
        ]);
        
        setDepartments(deptResponse.data?.data || []);
        setClasses(classResponse.data?.data || []);
      } catch (error) {
        message.error('Lỗi khi tải dữ liệu Tổ bộ môn và Lớp học!');
        console.error("Fetch dropdown error:", error);
      }
    };

    fetchDropdownData();
  }, []);

  const fetchTeachers = async (page = 1, pageSize = 10, filters = teacherFilters) => {
    setLoading(true);
    try {
      const params = {
        page: page - 1, size: pageSize,
        keyword: filters.keyword || null,
        departmentId: filters.departmentId, active: filters.active,
      };
      const response = await adminUserApi.getTeachers(params);
      const { data, totalElements } = response.data.data;
      setTeachers(data);
      setTeacherPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error('Lỗi khi tải danh sách giáo viên!');
    } finally {
      setLoading(false);
    }
  };

  const fetchStudents = async (page = 1, pageSize = 10, filters = studentFilters) => {
    setLoading(true);
    try {
      const params = {
        page: page - 1, size: pageSize,
        keyword: filters.keyword || null,
        gradeLevel: filters.gradeLevel, classId: filters.classId, active: filters.active,
      };
      const response = await adminUserApi.getStudents(params);
      const { data, totalElements } = response.data.data;
      setStudents(data);
      console.log(response)
      setStudentPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error('Lỗi khi tải danh sách học sinh!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      if (activeTab === 'teachers') {
        fetchTeachers(1, teacherPagination.pageSize, teacherFilters);
      } else {
        fetchStudents(1, studentPagination.pageSize, studentFilters);
      }
    }, 300);
    return () => clearTimeout(delayDebounceFn);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab, teacherFilters, studentFilters]);

  const UserInfo = ({ record }) => (
    <Space>
      <Avatar style={{ backgroundColor: '#1677ff' }} icon={<UserOutlined />}>
        {record.fullName ? record.fullName.charAt(0).toUpperCase() : ''}
      </Avatar>
      <Space orientation="vertical" size={0}>
        <Text strong>{record.fullName}</Text>
        <Text type="secondary" style={{ fontSize: '12px' }}>{record.username}</Text>
      </Space>
    </Space>
  );

  const teacherColumns = [
    { title: 'ID', key: 'id', render: (_, record) => <Text>{record.id || '---'}</Text>},
    { title: 'GIÁO VIÊN', key: 'fullName', render: (_, record) => <Space>
      <Avatar style={{ backgroundColor: '#1677ff' }} icon={<UserOutlined />}>
        {record.fullName ? record.fullName.charAt(0).toUpperCase() : ''}
      </Avatar>
      <Space orientation="vertical" size={0}>
        <Text strong>{record.fullName}</Text>
        <Text type="secondary" style={{ fontSize: '12px' }}>{record.username}</Text>
      </Space>
    </Space>},
    { title: 'TỔ BỘ MÔN', key: 'department', render: (_, record) => <Text>{record.department?.name || '---'}</Text> },
    { 
      title: 'TRẠNG THÁI', 
      dataIndex: 'active', 
      key: 'status', 
      render: (active) => <Badge status={active ? 'success' : 'error'} text={active ? 'Hoạt động' : 'Vô hiệu'} /> 
    },
    { title: 'THAO TÁC', key: 'action', render: (_, record) => (
        <Space>
          <Button size="small" onClick={()=>navigate(`/admin/teachers/${record.id}`)}>Chi tiết</Button>
        </Space>
      ) 
    },
  ];

  const studentColumns = [
    { title: 'ID', key: 'id', render: (_, record) => <Text>{record.id || '---'}</Text>},
    { title: 'HỌC SINH', key: 'user', render: (_, record) => <UserInfo record={record} /> },
    { title: 'NIÊN KHÓA', key: 'cohort', render: (_, record) => <Text>{record.cohort?.name || '---'}</Text> },
    { title: 'LỚP', key: 'class', render: (_, record) => <Text>{record.schoolClass?.name || '---'}</Text> },
    { 
      title: 'TRẠNG THÁI', 
      dataIndex: 'active', 
      key: 'status', 
      render: (active) => <Badge status={active ? 'success' : 'error'} text={active ? 'Hoạt động' : 'Vô hiệu'} /> 
    },
    { title: 'THAO TÁC', key: 'action', render: (_, record) => (
        <Space>
          <Button size="small" onClick={()=>navigate(`/admin/students/${record.id}`)}>Chi tiết</Button>
        </Space>
      ) 
    },
  ];

  const tabItems = [
    {
      key: 'teachers',
      label: `Giáo viên (${teacherPagination.total})`,
      children: (
        <Space orientation="vertical" size="middle" style={{ display: 'flex' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 16 }}>
            <Space style={{ flexWrap: 'wrap' }}>
              <Input 
                prefix={<SearchOutlined />}
                placeholder="Tìm theo tên, tài khoản..."
                allowClear 
                onChange={(e) => setTeacherFilters(prev => ({ ...prev, keyword: e.target.value }))}
                style={{ width: 250 }} 
              />
              <Select 
                defaultValue={null} 
                style={{ width: 150 }}
                onChange={(val) => setTeacherFilters(p => ({ ...p, active: val }))}
                options={[
                  { value: null, label: 'Tất cả trạng thái' },
                  { value: true, label: 'Hoạt động' },
                  { value: false, label: 'Vô hiệu' },
                ]}
              />
              <Select 
                placeholder="Tất cả tổ bộ môn" allowClear style={{ width: 180 }}
                onChange={(val) => setTeacherFilters(p => ({ ...p, departmentId: val }))}
                options={departments.map(d => ({ value: d.id, label: d.name }))}
              />
            </Space>
            <Button type="primary" onClick={()=>navigate("/admin/teachers/create")} icon={<PlusOutlined/>}>Tạo tài khoản</Button>
          </div>

          <Table 
            columns={teacherColumns} 
            dataSource={teachers} 
            rowKey="id"
            loading={loading}
            pagination={{
              current: teacherPagination.current,
              pageSize: teacherPagination.pageSize,
              total: teacherPagination.total,
              showSizeChanger: true,
            }}
            onChange={(pagination) => fetchTeachers(pagination.current, pagination.pageSize)}
            bordered
          />
        </Space>
      )
    },
    {
      key: 'students',
      label: `Học sinh (${studentPagination.total})`,
      children: (
        <Space orientation="vertical" size="middle" style={{ display: 'flex' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 16 }}>
            <Space style={{ flexWrap: 'wrap' }}>
              <Input 
                prefix={<SearchOutlined />}
                placeholder="Tìm theo tên, tài khoản..."
                allowClear 
                onChange={(e) => setStudentFilters(prev => ({ ...prev, keyword: e.target.value }))}
                style={{ width: 250 }} 
              />
              <Select 
                defaultValue={null} 
                style={{ width: 150 }}
                onChange={(val) => setStudentFilters(p => ({ ...p, active: val }))}
                options={[
                  { value: null, label: 'Tất cả trạng thái' },
                  { value: true, label: 'Hoạt động' },
                  { value: false, label: 'Vô hiệu' },
                ]}
              />
              <Select 
                placeholder="Tất cả khối" allowClear style={{ width: 120 }}
                onChange={(val) => setStudentFilters(p => ({ ...p, gradeLevel: val, classId: null }))}
                options={[{ value: 10, label: 'Khối 10' }, { value: 11, label: 'Khối 11' }, { value: 12, label: 'Khối 12' }]}
              />
              <Select 
                placeholder="Tất cả lớp" allowClear style={{ width: 120 }} value={studentFilters.classId}
                onChange={(val) => setStudentFilters(p => ({ ...p, classId: val }))}
                options={classes.filter(c => !studentFilters.gradeLevel || c.gradeLevel === studentFilters.gradeLevel)
                  .map(c => ({ value: c.id, label: c.name }))}
              />
            </Space>
            <Space>
              <Button icon={<UploadOutlined />}>Import Excel</Button>
              <Button type="primary" onClick={()=>navigate("/admin/students/create")} icon={<PlusOutlined />}>Tạo tài khoản</Button>
            </Space>
          </div>

          <Table 
            columns={studentColumns} 
            dataSource={students} 
            rowKey="id"
            loading={loading}
            pagination={{
              current: studentPagination.current,
              pageSize: studentPagination.pageSize,
              total: studentPagination.total,
              showSizeChanger: true,
            }}
            onChange={(pagination) => fetchStudents(pagination.current, pagination.pageSize)}
            bordered
          />
        </Space>
      )
    }
  ];

  return (
    <Card 
      variant={false} 
      title={<Title level={3} style={{ margin: 0 }}>Quản lý tài khoản</Title>}
      extra={<Text type="secondary">Tạo và quản lý tài khoản giáo viên, học sinh</Text>}
    >
      <Tabs 
        activeKey={activeTab} 
        onChange={setActiveTab} 
        items={tabItems}
        type="card"
      />
    </Card>
  );
};

export default AdminUserPage;
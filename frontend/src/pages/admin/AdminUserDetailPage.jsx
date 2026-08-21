import { useEffect, useState } from "react";
import { Form, Input, Button, message, Skeleton, Row, Col, Card, Descriptions, Tag, Table, Select, DatePicker, Switch, Space } from "antd";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import dayjs from "dayjs";
import adminUserApi from "../../api/adminUserApi";
import departmentApi from "../../api/departmentApi";
import subjectApi from "../../api/subjectApi";
import teacherApi from "../../api/teacherApi";
import studentApi from "../../api/studentApi";

const AdminUserDetailPage = () => {
  const [form] = Form.useForm();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [userData, setUserData] = useState(null);

  const [departments, setDepartments] = useState([]);
  const [subjects, setSubjects] = useState([]);

  const isTeacher = location.pathname.includes("teacher");

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        setLoading(true);
        if (isTeacher) {
          const [deptRes, subjRes] = await Promise.all([
            departmentApi.getDepartments(),
            subjectApi.getSubjects()
          ]);
          setDepartments(deptRes.data.data || []);
          setSubjects(subjRes.data.data || []);
        }

        const userRes = isTeacher 
          ? await adminUserApi.getTeacherById(id)
          : await adminUserApi.getStudentById(id);
          
        const data = userRes.data.data;
        setUserData(data);

      } catch (error) {
        console.error("Lỗi lấy dữ liệu:", error);
        message.error("Không lấy được thông tin chi tiết!");
      } finally {
        setLoading(false);
      }
    };

    if (id) fetchInitialData();
  }, [id, isTeacher]);

  useEffect(() => {
    if (userData && !loading) {
      form.setFieldsValue({
        fullName: userData.fullName,
        dateOfBirth: userData.dateOfBirth ? dayjs(userData.dateOfBirth) : null,
        phone: userData.phone,
        email: userData.email,
        gender: userData.gender,
        active: userData.active,
        
        address: userData.address,
        phoneParent: userData.phoneParent,
        
        departmentId: userData.department?.id,
        subjectIds: userData.subjects?.map(sub => sub.id) || [],
      });
    }
  }, [userData, loading, form]);

  const onFinish = async (values) => {
    setUpdating(true);
    try {
      const payload = {
        ...values,
        dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format("YYYY-MM-DD") : null,
      };

      if (isTeacher) {
        await teacherApi.updateTeacher(id, payload);
      } else {
        await studentApi.updateStudent(id, payload);
      }

      message.success("Cập nhật thông tin thành công!");
    } catch (error) {
      console.error("Lỗi cập nhật:", error);
      message.error("Lỗi khi lưu thay đổi!");
    } finally {
      setUpdating(false);
    }
  };

  const handleReset = () => {
    if (userData) {
      form.setFieldsValue({
        fullName: userData.fullName,
        dateOfBirth: userData.dateOfBirth ? dayjs(userData.dateOfBirth) : null,
        phone: userData.phone,
        email: userData.email,
        gender: userData.gender,
        active: userData.active,
        
        address: userData.address,
        phoneParent: userData.phoneParent,
        
        departmentId: userData.department?.id,
        subjectIds: userData.subjects?.map(sub => sub.id) || [],
      });
      message.info("Đã đặt lại dữ liệu gốc!");
    }
  };

  const enrollmentColumns = [
    {
      title: 'Lớp',
      dataIndex: 'className',
      key: 'className',
      render: (text) => <span style={{ fontWeight: 'bold', color: '#1677ff' }}>{text}</span>,
    },
    {
      title: 'Khối',
      dataIndex: 'gradeLevel',
      key: 'gradeLevel',
      align: 'center',
      render: (text) => <Tag>{text}</Tag>,
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      render: (status) => (
        <Tag 
      color={
        status === 'ACTIVE' ? 'success' : 
        status === 'TRANSFERRED' ? 'warning' : 
        'default'
      }
    >
      {
        status === 'ACTIVE' ? 'Đang học' : 
        status === 'TRANSFERRED' ? 'Chuyển lớp' : 
        'Đã hoàn thành'
      }
    </Tag>
      ),
    },
  ];

  if (loading) {
    return <Skeleton active paragraph={{ rows: 10 }} />;
  }

  return (
    <div className="admin-user-detail-page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2>Chi tiết tài khoản: {userData?.username}</h2>
        <Button onClick={() => navigate(-1)}>Quay lại</Button>
      </div>
      
      <Row gutter={[24, 24]}>
        <Col xs={24} md={14} lg={16}>
          <Card title="Chỉnh sửa thông tin" variant={false} className="shadow-sm">
            <Form
              form={form}
              layout="vertical"
              onFinish={onFinish}
            >
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item name="fullName" label="Họ và tên" rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}>
                    <Input placeholder="Nhập họ và tên" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item name="email" label="Địa chỉ Email" rules={[{ required: true, message: 'Vui lòng nhập email!' }, { type: 'email', message: 'Email không hợp lệ!' }]}>
                    <Input placeholder="Nhập email" />
                  </Form.Item>
                </Col>
              </Row>

              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item name="phone" label="Số điện thoại" rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}>
                    <Input placeholder="Nhập số điện thoại" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item name="dateOfBirth" label="Ngày sinh" rules={[{ required: true, message: 'Vui lòng chọn ngày sinh!' }]}>
                    <DatePicker style={{ width: '100%' }} format="YYYY-MM-DD" placeholder="Chọn ngày sinh" />
                  </Form.Item>
                </Col>
              </Row>

              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item name="gender" label="Giới tính" rules={[{ required: true, message: 'Vui lòng chọn giới tính!' }]}>
                    <Select 
                      placeholder="Chọn giới tính" 
                      options={[
                        { value: 'MALE', label: 'Nam' },
                        { value: 'FEMALE', label: 'Nữ' }
                      ]}
                    />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item name="active" label="Trạng thái tài khoản" valuePropName="checked">
                    <Switch checkedChildren="Hoạt động" unCheckedChildren="Vô hiệu hóa" />
                  </Form.Item>
                </Col>
              </Row>

              {!isTeacher && (
                <>
                  <Row gutter={16}>
                    <Col span={24}>
                      <Form.Item name="address" label="Địa chỉ thường trú">
                        <Input placeholder="Nhập địa chỉ" />
                      </Form.Item>
                    </Col>
                  </Row>
                  <Row gutter={16}>
                    <Col span={12}>
                      <Form.Item name="phoneParent" label="Số điện thoại Phụ huynh">
                        <Input placeholder="Nhập SĐT người thân" />
                      </Form.Item>
                    </Col>
                  </Row>
                </>
              )}

              {isTeacher && (
                <Row gutter={16}>
                  <Col span={12}>
                    <Form.Item name="departmentId" label="Tổ / Bộ môn" rules={[{ required: true, message: 'Vui lòng chọn bộ môn!' }]}>
                      <Select 
                        placeholder="Chọn tổ bộ môn"
                        options={departments.map(dept => ({ value: dept.id, label: dept.name }))}
                      />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item name="subjectIds" label="Các môn giảng dạy">
                      <Select 
                        mode="multiple" 
                        placeholder="Chọn các môn học"
                        allowClear
                        options={subjects.map(sub => ({ value: sub.id, label: sub.name }))}
                      />
                    </Form.Item>
                  </Col>
                </Row>
              )}

              <Form.Item style={{ marginTop: 24, marginBottom: 0, textAlign: 'right' }}>
                <Space>
                  <Button onClick={handleReset}>Đặt lại</Button>
                  <Button type="primary" htmlType="submit" loading={updating}>
                    Lưu thay đổi
                  </Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>

        <Col xs={24} md={10} lg={8}>
          <Card title="Thông tin hệ thống" variant={false} className="shadow-sm">
            <Descriptions column={1} size="small" bordered>
              <Descriptions.Item label="ID">{userData?.id}</Descriptions.Item>
              <Descriptions.Item label="Username">
                <span style={{ fontWeight: 'bold', color: '#1677ff' }}>{userData?.username}</span>
              </Descriptions.Item>
              <Descriptions.Item label="Ngày tạo">
                {userData?.createdDate ? dayjs(userData.createdDate).format("DD/MM/YYYY HH:mm") : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Cập nhật">
                {userData?.updatedDate ? dayjs(userData.updatedDate).format("DD/MM/YYYY HH:mm") : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Vai trò">
                <Tag color={!isTeacher ? 'cyan' : 'geekblue'}>
                  {!isTeacher ? "HỌC SINH" : "GIÁO VIÊN"}
                </Tag>
              </Descriptions.Item>
              
              {!isTeacher && userData?.cohort && (
                <Descriptions.Item label="Niên khóa">
                  <Tag color="purple">{userData.cohort.name} ({userData.cohort.admissionYear})</Tag>
                </Descriptions.Item>
              )}
            </Descriptions>
          </Card>

          {!isTeacher && userData?.enrollments && (
            <Card title="Lịch sử xếp lớp" variant={false} className="shadow-sm" style={{ marginTop: '24px' }}>
              <Table 
                columns={enrollmentColumns} 
                dataSource={userData.enrollments}
                rowKey={(record) => record.className} 
                pagination={false} 
                size="small"
                bordered
              />
            </Card>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AdminUserDetailPage;
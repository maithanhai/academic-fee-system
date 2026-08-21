import { Form, Input, Button, message, Skeleton, Row, Col, Card, Descriptions, Tag, Table, Space } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { ROLES } from "../../configs/roles";
import userApi from "../../api/userApi";

const ProfilePage = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [profileData, setProfileData] = useState(null); 

  const { role } = useSelector((state) => state.auth);
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await userApi.profile();
        const responseData = response.data.data;
        const actualUserData = responseData.teacherDetailResponse 
                            || responseData.studentDetailResponse 
                            || responseData;
                            
        setProfileData(actualUserData);
        console.log(responseData)
      } catch (error) {
        console.error("Lỗi gọi API Profile:", error);
        message.error("Không lấy được thông tin cá nhân!");
      } finally {
        setLoading(false);
      }
    };
    
    fetchProfile();
  }, []);

  useEffect(() => {
    if (profileData && !loading) {
      form.setFieldsValue({
        email: profileData.email,
        phone: profileData.phone,
        address: profileData.address,
        phoneParent: profileData.phoneParent,
      });
    }
  }, [profileData, loading, form]);

  const onFinish = async (values) => {
    setUpdating(true);
    try {
      await userApi.updateProfile(values);
      message.success("Cập nhật thông tin thành công!");
    } catch  {
      message.error("Lỗi khi cập nhật thông tin!");
    } finally {
      setUpdating(false);
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
        <Tag color={status === 'ACTIVE' ? 'success' : 'default'}>
          {status === 'ACTIVE' ? 'Đang học' : 'Đã hoàn thành'}
        </Tag>
      ),
    },
  ];

  if (loading) {
    return <Skeleton active paragraph={{ rows: 10 }} />;
  }

  return (
    <div className="profile-page">
      <h2 style={{ marginBottom: 24 }}>Hồ sơ cá nhân</h2>
      
      <Row gutter={[24, 24]}>
        <Col xs={24} md={12} lg={10}>
          <Card title="Cập nhật liên hệ" variant={false} className="shadow-sm">
            <Form
              form={form}
              layout="vertical"
              onFinish={onFinish}
            >
              <Form.Item 
                name="email" 
                label="Địa chỉ Email" 
                rules={[
                  { required: true, message: 'Vui lòng nhập email!' },
                  { type: 'email', message: 'Email không đúng định dạng!' }
                ]}
              >
                <Input placeholder="Ví dụ: hs11@edu.vn" />
              </Form.Item>

              <Form.Item 
                name="phone" 
                label="Số điện thoại cá nhân"
                rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}
              >
                <Input placeholder="Ví dụ: 0901xxx" />
              </Form.Item>

              {role === ROLES.STUDENT && (
                <>
                  <Form.Item name="address" label="Địa chỉ thường trú">
                    <Input placeholder="Ví dụ: HCM" />
                  </Form.Item>
                  <Form.Item name="phoneParent" label="Số điện thoại Phụ huynh">
                    <Input placeholder="SĐT người thân để liên hệ" />
                  </Form.Item>
                </>
              )}

              <Form.Item style={{ marginTop: 32, marginBottom: 0 }}>
                <Button type="primary" htmlType="submit" loading={updating} block>
                  Lưu thay đổi
                </Button>
              </Form.Item>
            </Form>
          </Card>
        </Col>

        <Col xs={24} md={12} lg={14}>
          <Card title="Thông tin cơ bản" variant={false} className="shadow-sm">
            <Descriptions column={1} style={{ fontWeight: 'bold', width: '150px' ,whiteSpace:'nowrap'}}>
              <Descriptions.Item label="Họ và tên">
                <span style={{ fontSize: '16px', color: '#1677ff', fontWeight: 'bold' }}>
                  {profileData?.fullName}
                </span>
              </Descriptions.Item>
              
              <Descriptions.Item label="Ngày sinh">
                {profileData?.dateOfBirth}
              </Descriptions.Item>
              
              <Descriptions.Item label="Giới tính">
                {profileData?.gender === 'MALE' ? 'Nam' : profileData?.gender === 'FEMALE' ? 'Nữ' : 'Khác'}
              </Descriptions.Item>
              
              <Descriptions.Item label="Vai trò">
                <Tag color={role === ROLES.STUDENT ? 'cyan' : role === ROLES.TEACHER ? 'geekblue' : 'magenta'}>
                  {role ? role.replace("ROLE_","") : "Đang cập nhật"}
                </Tag>
              </Descriptions.Item>

              {role === ROLES.STUDENT && profileData?.cohort && (
                <>
                  <Descriptions.Item label="Niên khóa">
                    {profileData.cohort.name}
                  </Descriptions.Item>
                  <Descriptions.Item label=""></Descriptions.Item>
                </>
              )}

              {role === ROLES.STUDENT && profileData?.enrollments && (
            <Card title="Lịch sử học tập" variant={false} className="shadow-sm" style={{ marginTop: '24px' }}>
              <Table 
                columns={enrollmentColumns} 
                dataSource={profileData.enrollments}
                rowKey={(record, index) => record.className + index} 
                pagination={false} 
                size="small"
                bordered
              />
            </Card>
          )}

              {role === ROLES.TEACHER && (
                <>
                  <Descriptions.Item label="Khoa / Bộ môn">
                    {profileData?.department?.name || 'Chưa cập nhật'}
                  </Descriptions.Item>
                  <Descriptions.Item label="Chuyên môn">
                    {profileData?.subjects && profileData.subjects.length > 0 ? (
                      <Space wrap>
                        {profileData.subjects.map((subject) => (
                          <Tag key={subject.id} color="blue">
                            {subject.name}
                          </Tag>
                        ))}
                      </Space>
                    ) : (
                      'Chưa cập nhật'
                    )}
                  </Descriptions.Item>
                </>
              )}
            </Descriptions>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default ProfilePage;
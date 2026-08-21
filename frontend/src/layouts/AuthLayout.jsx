import { Layout, Row, Col, theme } from 'antd';
import { Outlet } from 'react-router-dom';

const { Content } = Layout; 

const AuthLayout = () => {
  const {
    token: { colorBgContainer, colorBgLayout, colorText },
  } = theme.useToken(); 

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Content style={{ display: 'flex' }}>
        <Row style={{ width: '100%', flex: 1 }}>
          
          <Col
            xs={0}
            md={12}
            style={{
              backgroundColor: colorBgLayout, 
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <div style={{ textAlign: 'center', color: colorText }}>
              <h2>Hệ thống quản lý học vụ và tài chính nội bộ THPT</h2>
            </div>
          </Col>

          <Col
            xs={24}
            md={12}
            style={{
              backgroundColor: colorBgContainer, 
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <div style={{ width: '100%', maxWidth: 400, padding: 24 }}>
              <h2 style={{ textAlign: 'center', marginBottom: 32, color: colorText }}>
                Đăng nhập Hệ thống
              </h2>
              <Outlet />
            </div>
          </Col>

        </Row>
      </Content>
    </Layout>
  );
};

export default AuthLayout;
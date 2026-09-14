import {
UserOutlined,LogoutOutlined,NotificationOutlined,DownOutlined,IdcardOutlined,KeyOutlined,
ProfileOutlined,
CreditCardOutlined,
} from "@ant-design/icons";
import { Layout, Menu, theme, Typography, Space, message, Dropdown, Avatar } from "antd";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../app/store/slices/authSlice";
import authApi from "../features/auth/api/authApi";

const { Header, Content, Footer, Sider } = Layout;
const { Text } = Typography;

const StudentLayout = () => {
  const {token: { colorBgContainer, borderRadiusLG }} = theme.useToken();

  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();

  const { fullname, username } = useSelector((state) => state.auth);

  const menuItems = [
    { key: "/student/notifications", icon: <NotificationOutlined />, label: "Thông báo" },
    { key: "/student/grades", icon: <ProfileOutlined />, label: "Bảng điểm" },
    { key: "/student/fees", icon: <CreditCardOutlined />, label: "Các đợt thu phí" },
  ];

  const userMenuItems = [
    {
      key: "info",
      label: (
        <div style={{ padding: "4px 0", minWidth: "160px" }}>
          <div style={{ fontWeight: "bold", fontSize: "14px", color: "#1f1f1f" }}>
            {fullname || "Giáo viên"}
          </div>
          <div style={{ color: "#8c8c8c", fontSize: "12px", marginTop: "2px" }}>
            @{username || "teacher"}
          </div>
        </div>
      ),
      disabled: true,
    },
    { type: "divider" },
    { key: "/student/profile", icon: <IdcardOutlined />, label: "Thông tin cá nhân" },
    { key: "/student/change-password", icon: <KeyOutlined />, label: "Đổi mật khẩu" },
    { type: "divider" },
    { key: "logout", icon: <LogoutOutlined />, label: "Đăng xuất", danger: true },
  ];

  const handleMenuClick = ({ key }) => {
    navigate(key);
  };

  const handleLogout = async () => {
    try {
      await authApi.logout();
      dispatch(logout());
      message.success("Đăng xuất thành công!");
      navigate("/login");
    } catch {
      message.error("Lỗi khi đăng xuất!");
    }
  };

  const handleUserMenuClick = ({ key }) => {
    if (key === "logout") {
      handleLogout();
    } else {
      navigate(key);
    }
  };

  return (
    <Layout hasSider style={{ minHeight: "100vh" }}>
      <Sider
        breakpoint="lg"
        collapsedWidth="0"
        onBreakpoint={(broken) => console.log(broken)}
        onCollapse={(collapsed, type) => console.log(collapsed, type)}
        style={{
          overflow: "auto",    
          height: "100vh",       
          position: "sticky",   
          insetInlineStart: 0,   
          top: 0,               
          scrollbarWidth: "thin",
          scrollbarGutter: "stable",
        }}
      >
        <div
          style={{
            height: 32,
            margin: 16,
            background: "rgba(255, 255, 255, 0.2)",
            borderRadius: 6,
          }}
        />
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={handleMenuClick}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: "0 24px",
            background: colorBgContainer,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            boxShadow: "0 1px 4px rgba(0,21,41,.08)",
            zIndex: 1,
          }}
        >
          <div style={{ fontSize: "18px", fontWeight: "bold" }}>
            Cổng Thông Tin Học Sinh
          </div>

          <Dropdown
            menu={{ items: userMenuItems, onClick: handleUserMenuClick }}
            trigger={["click"]}
            placement="bottomRight"
          >
            <Space style={{ cursor: "pointer", padding: "0 8px" }}>
              <Avatar
                icon={<UserOutlined />}
                style={{ backgroundColor: "#1677ff" }} 
              />
              <Text strong style={{ fontSize: "14px" }}>
                {fullname || "Học sinh"}
              </Text>
              <DownOutlined style={{ fontSize: "12px", color: "#8c8c8c" }} />
            </Space>
          </Dropdown>
        </Header>

        <Content style={{ margin: "24px 16px 0", overflow: "initial" }}>
          <div
            style={{
              padding: 24,
              minHeight: 360,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
              height: "100%",
            }}
          >
            <Outlet />
          </div>
        </Content>

        <Footer style={{ textAlign: "center" }}>
          Academic Fee System ©{new Date().getFullYear()} Created by 2351010054
          - Mai Thanh Hải
        </Footer>
      </Layout>
    </Layout>
  );
};

export default StudentLayout;
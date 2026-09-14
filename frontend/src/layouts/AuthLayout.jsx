import { Layout } from "antd";
import { Outlet } from "react-router-dom";
import bgImage from "../assets/THPT.jpg";

const AuthLayout = () => {
  return (
    <Layout
      style={{
        minHeight: "100vh",
        width: "100vw",
        // Linear-gradient ở đây đóng vai trò là lớp phủ đen mờ (opacity 0.5) đè lên ảnh
        backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${bgImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        // Flexbox để căn giữa hoàn toàn cái Form đăng nhập
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {/* Outlet sẽ render LoginPage vào ngay chính giữa màn hình */}
      <div style={{ width: "100%", maxWidth: 420, padding: 20 }}>
        <Outlet />
      </div>
    </Layout>
  );
};

export default AuthLayout;
import { useState } from "react";
import { message } from "antd";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import authApi from "../api/authApi";
import { setCredentials } from "../../../app/store/slices/authSlice";
import { ROLES } from "../../../shared/constants/roles";

const useLogin = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const submit = async (values) => {
    setLoading(true);
    try {
      const response = await authApi.login(values);
      const loginData = response.data.data;
      dispatch(setCredentials(loginData));
      message.success("Đăng nhập thành công!");
      const routes = {
        [ROLES.ADMIN]: "/admin/users",
        [ROLES.TEACHER]: "/teacher/homeroom-classes",
        [ROLES.STUDENT]: "/student/notifications",
      };
      navigate(routes[loginData.role] || "/login");
    } catch (error) {
      console.error("Lỗi đăng nhập:", error);
      message.error(
        "Đăng nhập không thành công, kiểm tra tài khoản và mật khẩu!",
      );
    } finally {
      setLoading(false);
    }
  };

  return { loading, submit };
};

export default useLogin;

import { Route, Routes } from "react-router-dom";
import TeacherDashboardPage from "../pages/teacher/TeacherDashboardPage";
import TeacherLayout from "../layouts/TeacherLayout";
import ChangePasswordPage from "../pages/common/ChangePasswordPage";
import ProfilePage from "../pages/common/ProfilePage";
import ErrorPage from "../pages/common/ErrorPage";
const TeacherRoutes = () => {
  return (
    <Routes>
      <Route element={<TeacherLayout />}>
        <Route path="dashboard" element={<TeacherDashboardPage />} />
        <Route path="change-password" element={<ChangePasswordPage />} />
        <Route path="profile" element={<ProfilePage />} />

        <Route path="*" element={<ErrorPage status="404" />} />
      </Route>
    </Routes>
  );
};

export default TeacherRoutes;

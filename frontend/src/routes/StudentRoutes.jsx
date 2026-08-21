import { Route, Routes } from "react-router-dom";
import StudentNotificationPage from "../pages/student/StudentNotificationPage";
import StudentLayout from "../layouts/StudentLayout";
import ChangePasswordPage from "../pages/common/ChangePasswordPage";
import ProfilePage from "../pages/common/ProfilePage";
import ErrorPage from "../pages/common/ErrorPage";
const StudentRoutes = () => {
  return (
    <Routes>
      <Route element={<StudentLayout />}>
        <Route path="notification" element={<StudentNotificationPage />} />
        <Route path="changePassword" element={<ChangePasswordPage />} />
        <Route path="profile" element={<ProfilePage />} />

        <Route path="*" element={<ErrorPage status="404" />} />
      </Route>
    </Routes>
  );
};

export default StudentRoutes;

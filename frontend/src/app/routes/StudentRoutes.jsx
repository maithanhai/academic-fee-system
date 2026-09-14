import { Route, Routes } from "react-router-dom";
import StudentNotificationPage from "../../features/notification/pages/StudentNotificationPage";
import StudentLayout from "../../layouts/StudentLayout";
import ChangePasswordPage from "../../features/profile/pages/ChangePasswordPage";
import ProfilePage from "../../features/profile/pages/ProfilePage";
import ErrorPage from "../../shared/components/ErrorPage";
import StudentGradeTablePage from "../../features/grade/pages/StudentGradeTablePage";
import StudentFinancePage from "../../features/finance/pages/StudentFinancePage";
const StudentRoutes = () => {
  return (
    <Routes>
      <Route element={<StudentLayout />}>
        <Route path="notifications" element={<StudentNotificationPage />} />
        <Route path="change-password" element={<ChangePasswordPage />} />
        <Route path="grades" element={<StudentGradeTablePage />} />
        <Route path="fees" element={<StudentFinancePage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="*" element={<ErrorPage status="404" />} />
      </Route>
    </Routes>
  );
};

export default StudentRoutes;

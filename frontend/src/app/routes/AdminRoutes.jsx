import { Route, Routes } from "react-router-dom";
import AdminLayout from "../../layouts/AdminLayout";
import ChangePasswordPage from "../../features/profile/pages/ChangePasswordPage";
import ErrorPage from "../../shared/components/ErrorPage";
import AdminUserPage from "../../features/people/pages/AdminUserPage";
import AdminUserDetailPage from "../../features/people/pages/AdminUserDetailPage";
import AdminRegisterStudentPage from "../../features/people/pages/AdminRegisterStudentPage";
import AdminRegisterTeacherPage from "../../features/people/pages/AdminRegisterTeacherPage";
import AdminNotificationPage from "../../features/notification/pages/AdminNotificationPage";
import AdminAuditLogPage from "../../features/audit/pages/AdminAuditLogPage";
import AdminFinancePage from "../../features/finance/pages/AdminFinancePage";
import AdminNotificationDetailPage from "../../features/notification/pages/AdminNotificationDetailPage";
import AdminAcademicPage from "../../features/academic/pages/AdminAcademicPage";
import AdminAssignmentPage from "../../features/assignment/pages/AdminAssignmentPage";
import AdminClassListPage from "../../features/academic/pages/AdminClassListPage";
import AdminClassDetailPage from "../../features/academic/pages/AdminClassDetailPage";
const AdminRoutes = () => {
  return (
    <Routes>
      <Route element={<AdminLayout />}>
        <Route path="users" element={<AdminUserPage />} />
        <Route path="change-password" element={<ChangePasswordPage />} />
        <Route path="students/:id" element={<AdminUserDetailPage />} />
        <Route path="teachers/:id" element={<AdminUserDetailPage />} />
        <Route path="students/create" element={<AdminRegisterStudentPage />} />
        <Route path="teachers/create" element={<AdminRegisterTeacherPage />} />
        <Route path="assignments" element={<AdminAssignmentPage />} />
        <Route path="notifications" element={<AdminNotificationPage />} />
        <Route
          path="notifications/:id"
          element={<AdminNotificationDetailPage />}
        />
        <Route path="audit-logs" element={<AdminAuditLogPage />} />
        <Route path="finances" element={<AdminFinancePage />} />
        <Route path="academics" element={<AdminAcademicPage />} />
        <Route path="classes" element={<AdminClassListPage/>}/>
        <Route path="classes/:id/students" element={<AdminClassDetailPage/>}/>
        <Route path="*" element={<ErrorPage status="404" />} />
      </Route>
    </Routes>
  );
};

export default AdminRoutes;

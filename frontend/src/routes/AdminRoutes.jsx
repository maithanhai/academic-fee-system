import { Route, Routes } from "react-router-dom"
import AdminDashboardPage from "../pages/admin/AdminDashboardPage"
import AdminLayout from "../layouts/AdminLayout"
import ChangePasswordPage from "../pages/common/ChangePasswordPage"
import ErrorPage from "../pages/common/ErrorPage"
import AdminUserPage from "../pages/admin/AdminUserPage"
import AdminUserDetailPage from "../pages/admin/AdminUserDetailPage"
import AdminRegisterStudentPage from "../pages/admin/AdminRegisterStudentPage"
import AdminRegisterTeacherPage from "../pages/admin/AdminRegisterTeacherPage"
import AdminNotificationPage from "../pages/admin/AdminNotificationPage"
import AdminAuditLogPage from "../pages/admin/AdminAuditLogPage"
import AdminFinancePage from "../pages/admin/AdminFinancePage"
const AdminRoutes = ()=>{
    return (
        <Routes>
            <Route element={<AdminLayout/>}>
                <Route path="dashboard" element={<AdminDashboardPage/>}/>
                <Route path="users" element={<AdminUserPage/>}/>
                <Route path="change-password" element={<ChangePasswordPage/>}/>
                <Route path="students/:id" element={<AdminUserDetailPage/>}/>
                <Route path="teachers/:id" element={<AdminUserDetailPage/>}/>
                <Route path="students/create" element={<AdminRegisterStudentPage/>}/>
                <Route path="teachers/create" element={<AdminRegisterTeacherPage/>}/>
                <Route path="notifications" element={<AdminNotificationPage/>}/>
                <Route path="audit-logs" element={<AdminAuditLogPage/>}/>
                <Route path="finances" element={<AdminFinancePage/>}/>
                <Route path="*" element={<ErrorPage status="404" />} />
            </Route>
        </Routes>
    )
}

export default AdminRoutes
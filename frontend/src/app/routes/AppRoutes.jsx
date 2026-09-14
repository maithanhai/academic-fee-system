import { Navigate, Route, Routes } from "react-router-dom";
import AuthLayout from "../../layouts/AuthLayout";
import LoginPage from "../../features/auth/pages/LoginPage";
import React, { Suspense } from "react";
import { Skeleton } from "antd";
import ProtectedRoute from "./ProtectedRoute";
import { ROLES } from "../../shared/constants/roles";
import ErrorPage from "../../shared/components/ErrorPage";

const AdminRoutes = React.lazy(() => import("./AdminRoutes"));
const TeacherRoutes = React.lazy(() => import("./TeacherRoutes"));
const StudentRoutes = React.lazy(() => import("./StudentRoutes"));

const LoadingScreen = () => (
  <div style={{ padding: 50 }}>
    <Skeleton active paragraph={{ rows: 8 }} />
  </div>
);

const AppRoutes = () => {
  return (
    <Suspense fallback={<LoadingScreen />}>
      <Routes>
        <Route element={<AuthLayout />}>
          <Route path="/login" element={<LoginPage />} />
        </Route>
        <Route path="/403" element={<ErrorPage status="403" />} />
        <Route path="/500" element={<ErrorPage status="500" />} />

        <Route element={<ProtectedRoute allowedRoles={[ROLES.ADMIN]} />}>
          <Route path="/admin/*" element={<AdminRoutes />} />
        </Route>

        <Route element={<ProtectedRoute allowedRoles={[ROLES.TEACHER]} />}>
          <Route path="/teacher/*" element={<TeacherRoutes />} />
        </Route>

        <Route element={<ProtectedRoute allowedRoles={[ROLES.STUDENT]} />}>
          <Route path="/student/*" element={<StudentRoutes />} />
        </Route>

        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="*" element={<ErrorPage status="404" />} />
      </Routes>
    </Suspense>
  );
};

export default AppRoutes;
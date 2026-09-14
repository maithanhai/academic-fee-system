import { Route, Routes } from "react-router-dom";
import TeacherLayout from "../../layouts/TeacherLayout";
import ChangePasswordPage from "../../features/profile/pages/ChangePasswordPage";
import ProfilePage from "../../features/profile/pages/ProfilePage";
import ErrorPage from "../../shared/components/ErrorPage";
import TeacherHomeroomAssignmentPage from "../../features/assignment/pages/TeacherHomeroomAssignmentPage";
import TeacherTeachingAssignmentPage from "../../features/assignment/pages/TeacherTeachingAssignmentPage";
import TeacherListStudentsPage from "../../features/assignment/pages/TeacherListStudentsPage";
import StudentsGradeTablePage from "../../features/grade/pages/StudentsGradeTablePage";
const TeacherRoutes = () => {
  return (
    <Routes>
      <Route element={<TeacherLayout />}>
        <Route path="change-password" element={<ChangePasswordPage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="homeroom-classes" element={<TeacherHomeroomAssignmentPage/>}/>
        <Route path="homeroom-classes/:id/students" element={<TeacherListStudentsPage/>}/>
        <Route path="teaching-classes" element={<TeacherTeachingAssignmentPage/>}/>
        <Route path="teaching-classes/:id/grades" element={<StudentsGradeTablePage/>}/>
        <Route path="*" element={<ErrorPage status="404" />} />
      </Route>
    </Routes>
  );
};

export default TeacherRoutes;

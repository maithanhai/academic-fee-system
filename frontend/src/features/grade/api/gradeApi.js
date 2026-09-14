import axiosClient from "../../../shared/api/axiosClient";

const gradeApi = {
  inputGrades: (data) => axiosClient.post("/teachers/grades", data),
  getTeacherGradeBoard: (classId, subjectId, semesterId) =>
    axiosClient.get(
      `/teachers/classes/${classId}/subjects/${subjectId}/grades`,
      {
        params: { semesterId },
      },
    ),
  getMyGrades: (semesterId) =>
    axiosClient.get("/students/me/grades", { params: { semesterId } }),
  updateGradeByTeacher: (gradeId, data) =>
    axiosClient.put(`/teachers/grades/${gradeId}`, data),

  getTranscriptByAdmin: (studentId, classId) =>
    axiosClient.get(`/admin/grades/students/${studentId}/transcript`, { params: { classId } }),
  getGradeTable: (classId, subjectId, semesterId) =>
    axiosClient.get(
      `/teacher/classes/${classId}/subjects/${subjectId}/grades`,
      {
        params: { semesterId },
      },
    ),
  autoSave: (classId, subjectId, data) =>
    axiosClient.post(
      `/teacher/classes/${classId}/subjects/${subjectId}/grades/auto-save`,
      data,
    ),
  getTranscriptByStudent: (academicYearId) =>
    axiosClient.get("/student/grades/transcript", { params: { academicYearId } }),
};

export default gradeApi;

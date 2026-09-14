import axiosClient from "../../../shared/api/axiosClient";

const classApi = {
  getClasses: () => {
    return axiosClient.get("/admin/classes");
  },
  getClassesByAcademicYearId: (academicYearId) =>
    axiosClient.get(`/admin/classes/academic-years/${academicYearId}/total`),
  getStudentsByClassId: (classId) =>
    axiosClient.get(`/admin/classes/${classId}/students`),
  createClass: (data) => {
    return axiosClient.post("/admin/classes", data);
  },
};
export default classApi;

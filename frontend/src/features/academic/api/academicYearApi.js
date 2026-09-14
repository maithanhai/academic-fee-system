import axiosClient from "../../../shared/api/axiosClient";

const academicYearApi = {
  getAcademicYears: () => {
    return axiosClient.get("/academic-years");
  },
  addAcademicYear: () => {
    return axiosClient.post("/admin/academic-years");
  },
  updateActiveAcademicYear: (id, data) => {
    return axiosClient.put(`/admin/academic-years/${id}`, data);
  },
  getClassesByAcademicYearId: (academicYearId) =>
    axiosClient.get(`/academic-years/${academicYearId}/classes`),
  getSemesters: (academicYearId) =>
    axiosClient.get(`/academic-years/${academicYearId}/semesters`),
  getAcademicYearsByStudent: () => axiosClient.get("/student/academic-years"),
};
export default academicYearApi;

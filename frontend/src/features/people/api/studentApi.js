import axiosClient from "../../../shared/api/axiosClient";

const studentApi = {
  updateStudent: (id, data) => {
    return axiosClient.put(`/admin/students/${id}`, data);
  },
  importStudentsExcel: (file, academicYearId, cohortId) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("academicYearId", academicYearId);
    formData.append("cohortId", cohortId);
    return axiosClient.post("/admin/students/import", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  getUnenrolledStudents: (academicYearId) =>
    axiosClient.get(`/admin/students/unenrollments`, {
      params: { academicYearId },
    }),
};
export default studentApi;

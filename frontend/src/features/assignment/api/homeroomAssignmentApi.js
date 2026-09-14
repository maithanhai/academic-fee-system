import axiosClient from "../../../shared/api/axiosClient";

const homeroomAssigntmentApi = {
  assignHomeroomTeachers: (data) =>
    axiosClient.post("/admin/homeroom-assignments", data),
  endHomeroomAssignment: (id) =>
    axiosClient.put(`/admin/homeroom-assignments/${id}/end`),
  getMyHomeroomClass: () => axiosClient.get("/secure/my-homeroom-class"),

  getExistingAssignments: (academicYearId) =>
    axiosClient.get("/admin/homeroom-assignments", {
      params: { academicYearId },
    }),
  getStudentTranscript: (schoolClassId, studentId) => {
    return axiosClient.get(
      `/teacher/homeroom-classes/${schoolClassId}/students/${studentId}/transcript`,
    );
  },
  getHomeroomClasses:(data)=>{
    return axiosClient.get("/teacher/homeroom-classes",data)
  },
  getStudentsByClassId:(classId)=>{
    return axiosClient.get(`/teacher/homeroom-classes/${classId}/students`)
  }
};

export default homeroomAssigntmentApi;

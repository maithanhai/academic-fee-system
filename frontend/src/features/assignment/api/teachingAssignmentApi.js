import axiosClient from "../../../shared/api/axiosClient";

const teachingAssignmentApi = {
  getExistingAssignments: (academicYearId, gradeLevels) =>
    axiosClient.get("/admin/teaching-assignments", {
      params: { academicYearId, gradeLevels },
    }),
  copyAssignmentsPreviousYear: (academicYearId, gradeLevels) =>
    axiosClient.get("/admin/teaching-assignments/copy-previous", {
      params: { academicYearId, gradeLevels },
    }),
  previewAutoAssign: (academicYearId, gradeLevels) =>
    axiosClient.get("/admin/teaching-assignments/preview-auto", {
      params: { academicYearId, gradeLevels },
    }),
  saveBulkTeachingAssignments: (assignments) =>
    axiosClient.post("/admin/teaching-assignments/bulk", assignments),
  getTeachingClasses: (academicYearId, data) =>
    axiosClient.get(
      `/teacher/teaching-assignments/academic-years/${academicYearId}`,
      data,
    ),
};

export default teachingAssignmentApi;

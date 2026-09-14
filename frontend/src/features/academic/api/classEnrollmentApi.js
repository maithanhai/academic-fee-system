import axiosClient from "../../../shared/api/axiosClient";

const classEnrollmentApi = {
  createEnrollments: (data) =>
    axiosClient.post("/admin/class-enrollments", data),
  transferStudent: (data) =>
    axiosClient.post("/admin/class-enrollments/transfer", data),
};
export default classEnrollmentApi;

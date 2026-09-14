import axiosClient from "../../../shared/api/axiosClient";

const cohortApi = {
  getCohorts: () => {
    return axiosClient.get("/cohorts");
  },
  addCohort: (data) => {
    return axiosClient.post("/admin/cohorts", data);
  }
};
export default cohortApi

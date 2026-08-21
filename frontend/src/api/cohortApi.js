import axiosClient from "./axiosClient";

const cohortApi = {
  getCohorts: () => {
    return axiosClient.get("/admin/cohorts");
  },
  
};
export default cohortApi

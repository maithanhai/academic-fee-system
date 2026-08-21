import axiosClient from "./axiosClient";

const classApi = {
  getClasses: () => {
    return axiosClient.get("/admin/classes");
  },
  
};
export default classApi

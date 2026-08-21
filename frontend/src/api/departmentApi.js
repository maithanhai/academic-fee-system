import axiosClient from "./axiosClient";

const departmentApi = {
  getDepartments: () => {
    return axiosClient.get("/admin/departments");
  },
  
};
export default departmentApi

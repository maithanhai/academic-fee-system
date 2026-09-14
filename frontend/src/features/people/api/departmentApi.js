import axiosClient from "../../../shared/api/axiosClient";

const departmentApi = {
  getDepartments: () => {
    return axiosClient.get("/departments");
  },
  addDepartment: (data) => {
    return axiosClient.post("/admin/departments", data);
  },
  updateDepartment: (id, data) => {
    return axiosClient.put(`/admin/departments/${id}`, data);
  },
  
};
export default departmentApi

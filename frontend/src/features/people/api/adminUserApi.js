import axiosClient from "../../../shared/api/axiosClient";

const adminUserApi = {
  getStudents: (params) => {
    return axiosClient.get("/admin/students", { params });
  },
  getTeachers: (params) => {
    return axiosClient.get("/admin/teachers", { params });
  },
  getStudentById: (id)=>{
    return axiosClient.get(`/admin/students/${id}`)
  },
  getTeacherById:(id)=>{
    return axiosClient.get(`/admin/teachers/${id}`)
  },
  createStudent: (data)=>{
    return axiosClient.post("/admin/students",data)
  },
  createTeacher: (data)=>{
    return axiosClient.post("/admin/teachers",data)
  }
};
export default adminUserApi;

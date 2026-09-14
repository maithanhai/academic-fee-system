
import axiosClient from "../../../shared/api/axiosClient"

const teacherApi = {
    getActiveTeachers:()=>{
      return axiosClient.get("/admin/teachers/active")
    },
    updateTeacher:(id,data)=>{
    return axiosClient.put(`/admin/teachers/${id}`,data)
  }
}
export default teacherApi
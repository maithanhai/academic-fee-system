
import axiosClient from "./axiosClient"

const teacherApi = {
    updateTeacher:(id,data)=>{
    return axiosClient.put(`/admin/teachers/${id}`,data)
  }
}
export default teacherApi

import axiosClient from "./axiosClient"

const studentApi = {
    updateStudent:(id,data)=>{
    return axiosClient.put(`/admin/students/${id}`,data)
  }
}
export default studentApi
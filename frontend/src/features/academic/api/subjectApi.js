import axiosClient from "../../../shared/api/axiosClient";

const subjectApi = {
  getSubjects:()=>{
    return axiosClient.get("/admin/subjects");
  },
  getActiveSubjects:()=>{
    return axiosClient.get("/subjects");
  },
  addSubject:(data)=>{
    return axiosClient.post("/admin/subjects", data)
  },
  updateSubject:(id,data)=>{
    return axiosClient.put(`/admin/subjects/${id}`, data)
  }
};
export default subjectApi;

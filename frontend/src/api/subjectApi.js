import axiosClient from "./axiosClient";

const subjectApi = {
  getSubjects:()=>{
    return axiosClient.get("/admin/subjects");
  }
};
export default subjectApi;

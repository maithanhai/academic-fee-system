import axiosClient from "../../../shared/api/axiosClient"

const teacherExpertiseApi = {
    getTeacherExpertisesWithWorkload:(academicYearId)=>{
      return axiosClient.get("/admin/teacher-expertises/workloads",{
        params:{academicYearId}
      })
    }
}
export default teacherExpertiseApi
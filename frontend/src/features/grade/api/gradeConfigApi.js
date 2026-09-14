import axiosClient from '../../../shared/api/axiosClient';

const gradeConfigApi = {
  createGradeConfigs: (data) =>
    axiosClient.post('/admin/grade-configs', data),
  updateGradeConfigs: (data) =>
    axiosClient.put('/admin/grade-configs', data),
  getGradeConfigsBySubjectId: (subjectId) =>
    axiosClient.get(`/admin/grade-configs/${subjectId}`),
};

export default gradeConfigApi;

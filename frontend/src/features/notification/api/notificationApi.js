import axiosClient from "../../../shared/api/axiosClient";

const notificationApi = {
  getNotifications: (params) => {
    return axiosClient.get("/admin/notifications",{params});
  },
  createNotification: (data) => {
    return axiosClient.post("/admin/notifications",data);
  },
  updateNotification: (id,data) => {
    return axiosClient.put(`/admin/notifications/${id}`,data);
  },
  getNotificationById: (id) => { 
    return axiosClient.get(`/admin/notifications/${id}`);
  },
  sendNotificationToStudents: (id) => {
    return axiosClient.post(`/admin/notifications/${id}/send-to-students`);
  },
  getNotificationsForStudent: () => {
    return axiosClient.get("/student/notifications");
  },
  getNotificationByIdForStudent: (id) => {
    return axiosClient.get(`/student/notifications/${id}`);
  }
};
export default notificationApi

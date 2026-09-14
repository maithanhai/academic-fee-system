import axiosClient from "../../../shared/api/axiosClient";

const feeInvoiceApi = {
  getInvoices: (params) => {
    const filteredParams = Object.fromEntries(
      Object.entries(params).filter(
        ([, value]) => value != null && value !== "",
      ),
    );
    return axiosClient.get("/admin/invoices", { params: filteredParams });
  },
  getInvoiceById: (id) => {
    return axiosClient.get(`/admin/invoices/${id}`);
  },
  confirmCashByAdmin: (data) => {
    return axiosClient.post(`/admin/invoices/confirm-cash`, data);
  },
  undoInvoiceByAdmin: (data) => {
    return axiosClient.post(`/admin/invoices/undo`, data);
  },
  confirmCashByTeacher: (classId, data) => {
    return axiosClient.post(`/teacher/invoices/confirm-cash`, data, {
      params: { classId },
    });
  },
  undoInvoiceByTeacher: (classId, data) => {
    return axiosClient.post(`/teacher/invoices/undo`, data, {
      params: { classId },
    });
  },
  getInvoicesByTeacher: (studentId, classId, academicYearId) => {
    return axiosClient.get(`/teacher/students/${studentId}/invoices`, {
      params: { classId, academicYearId },
    });
  },
  getInvoicesByStudent: (academicYearId) =>
    axiosClient.get(`/student/invoices`, {
      params: { academicYearId },
    }),
  getInvoiceByStudent: (invoiceId) =>
    axiosClient.get(`/student/invoices/${invoiceId}`),
  payInvoiceByStudent: (invoiceId) =>
    axiosClient.post(`/student/invoices/${invoiceId}/pay`),
};

export default feeInvoiceApi;

import axiosClient from "../../../shared/api/axiosClient";

const feeApi = {
    getFees: (params) => {
        const filteredParams = Object.fromEntries(Object.entries(params).filter(([, value]) => value != null && value !== ''));
        return axiosClient.get("/admin/fees", { params: filteredParams });
    },
    createFee: (data) => {
        return axiosClient.post("/admin/fees", data);
    },
    updateFee: (id, data) => {
        return axiosClient.put(`/admin/fees/${id}`, data);
    },
    generateInvoices: (feeId) => {
        return axiosClient.post(`/admin/fees/${feeId}/invoices/generate`);
    }
};

export default feeApi;
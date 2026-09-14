import axiosClient from "../../../shared/api/axiosClient"

const auditApi = {
    getInvoiceAuditLogs: (params) => {
        return axiosClient.get("/admin/audit-logs/invoices", {params})
    },
    getInvoiceAuditLogById:(id)=>{
        return axiosClient.get(`/admin/audit-logs/invoices/${id}`)
    },
    getGradeAuditLogs: (params) => {
        return axiosClient.get("/admin/audit-logs/grades", {params})
    },
    getGradeAuditLogById:(id)=>{
        return axiosClient.get(`/admin/audit-logs/grades/${id}`)
    }
}
export default auditApi
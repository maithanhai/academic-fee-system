import { useState } from "react";
import { message } from "antd";
import auditLogApi from "../api/auditLogApi";

const useAuditLogDetail = () => {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [detail, setDetail] = useState(null);

  const openDetail = async (id, type) => {
    setOpen(true);
    setLoading(true);
    setDetail(null);
    try {
      const response =
        type === "grades"
          ? await auditLogApi.getGradeAuditLogById(id)
          : await auditLogApi.getInvoiceAuditLogById(id);
      setDetail(response.data.data);
    } catch (error) {
      message.error(error.response?.data?.message || "Không tải được chi tiết lịch sử!");
      setOpen(false);
    } finally {
      setLoading(false);
    }
  };

  return { open, loading, detail, openDetail, close: () => setOpen(false) };
};

export default useAuditLogDetail;

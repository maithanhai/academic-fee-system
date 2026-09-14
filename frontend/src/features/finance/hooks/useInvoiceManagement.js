import { message, Modal } from "antd";
import feeInvoiceApi from "../api/feeInvoiceApi";
import { useEffect, useState } from "react";

export const useInvoiceManagement = () => {
  const [invoices, setInvoices] = useState([]);
  const [invoiceLoading, setInvoiceLoading] = useState(false);
  const [invoicePagination, setInvoicePagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  const [invoiceFilters, setInvoiceFilters] = useState({
    keyword: "",
    feeId: null,
    status: null,
  });
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [invoiceDetail, setInvoiceDetail] = useState(null);
  const [detailLoading, setDetailLoading] = useState(false);
  const [undoModalVisible, setUndoModalVisible] = useState(false);
  const [undoInvoiceId, setUndoInvoiceId] = useState(null);
  const [undoSubmitting, setUndoSubmitting] = useState(false);

  const fetchInvoices = async (
    page = 1,
    pageSize = 10,
    filters = invoiceFilters,
  ) => {
    setInvoiceLoading(true);
    try {
      const params = {
        page: page - 1,
        size: pageSize,
        keyword: filters.keyword,
        feeId: filters.feeId,
        status: filters.status,
      };
      const response = await feeInvoiceApi.getInvoices(params);
      const { data, totalElements } = response.data.data;
      setInvoices(data);
      setInvoicePagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error("Lỗi khi tải danh sách hóa đơn");
    } finally {
      setInvoiceLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      fetchInvoices(1, invoicePagination.pageSize, invoiceFilters);
    }, 400);
    return () => clearTimeout(delayDebounceFn);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [invoiceFilters]);

  const handleConfirmCash = (invoice) => {
    Modal.confirm({
      title: "Xác nhận thu tiền mặt",
      content: `Xác nhận đã thu ${invoice.amount?.toLocaleString("vi-VN")} ₫ tiền mặt từ học sinh ${invoice.studentName}?`,
      okText: "Xác nhận",
      cancelText: "Hủy",
      onOk: async () => {
        try {
          await feeInvoiceApi.confirmCashByAdmin({ invoiceId: invoice.invoiceId });
          message.success("Xác nhận thanh toán thành công!");
          fetchInvoices(
            invoicePagination.current,
            invoicePagination.pageSize,
            invoiceFilters,
          );
        } catch (error) {
          message.error(
            error.response?.data?.message || "Lỗi khi xác nhận thanh toán",
          );
        }
      },
    });
  };

  const handleOpenUndo = (id) => {
    setUndoInvoiceId(id);
    setUndoModalVisible(true);
  };

  const submitUndo = async (values) => {
    setUndoSubmitting(true);
    try {
      await feeInvoiceApi.undoInvoiceByAdmin({
        invoiceId: undoInvoiceId,
        undoReason: values.reason,
      });
      message.success("Hoàn tác hóa đơn thành công!");
      setUndoModalVisible(false);
      fetchInvoices(
        invoicePagination.current,
        invoicePagination.pageSize,
        invoiceFilters,
      );
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi hoàn tác");
    } finally {
      setUndoSubmitting(false);
    }
  };

  const handleOpenDetail = async (id) => {
    setDrawerVisible(true);
    setDetailLoading(true);
    try {
      const response = await feeInvoiceApi.getInvoiceById(id);
      setInvoiceDetail(response.data.data);
    } catch {
      message.error("Lỗi tải dữ liệu");
      setDrawerVisible(false);
    } finally {
      setDetailLoading(false);
    }
  };

  return {
    state: {
      invoices,
      invoiceLoading,
      invoicePagination,
      invoiceFilters,
      drawerVisible,
      invoiceDetail,
      detailLoading,
      undoModalVisible,
      undoInvoiceId,
      undoSubmitting,
    },
    actions: {
      setInvoiceFilters,
      fetchInvoices,
      handleConfirmCash,
      handleOpenUndo,
      submitUndo,
      handleOpenDetail,
      setDrawerVisible,
      setUndoModalVisible,
      setUndoInvoiceId,
      setUndoSubmitting,
    },
  };
};

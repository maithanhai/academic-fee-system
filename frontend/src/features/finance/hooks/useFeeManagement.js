import dayjs from "dayjs";
import feeApi from "../api/feeApi";
import { Form, message, Modal } from "antd";
import { useEffect, useState } from "react";
import academicYearApi from "../../academic/api/academicYearApi";

export const useFeeManagement = () => {
  const [fees, setFees] = useState([]);
  const [feeLoading, setFeeLoading] = useState(false);
  const [feePagination, setFeePagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  const [feeFilters, setFeeFilters] = useState({
    keyword: "",
    academicYearId: null,
    active: null,
    isExpired: null,
  });
  const [feeModalVisible, setFeeModalVisible] = useState(false);
  const [isEditFee, setIsEditFee] = useState(false);
  const [editingFeeId, setEditingFeeId] = useState(null);
  const [feeSubmitting, setFeeSubmitting] = useState(false);
  const [feeForm] = Form.useForm();
  const [generateLoading, setGenerateLoading] = useState(false);
  const [academicYears, setAcademicYears] = useState([]);

  useEffect(() => {
    const fetchAcademicYears = async () => {
      try {
        const response = await academicYearApi.getAcademicYears();
        setAcademicYears(response.data?.data || []);
      } catch {
        message.error("Lỗi khi tải danh sách năm học");
      }
    };
    fetchAcademicYears();
  }, []);

  const fetchFees = async (page = 1, pageSize = 10, filters = feeFilters) => {
    setFeeLoading(true);
    try {
      const params = {
        page: page, 
        size: pageSize,
      };

      if (filters.keyword && filters.keyword.trim()) {
        params.keyword = filters.keyword.trim();
      }
      if (filters.academicYearId !== null && filters.academicYearId !== undefined) {
        params.academicYearId = filters.academicYearId;
      }
      if (filters.active !== null && filters.active !== undefined) {
        params.active = filters.active;
      }
      if (filters.isExpired !== null && filters.isExpired !== undefined) {
        params.isExpired = filters.isExpired;
      }

      const response = await feeApi.getFees(params);

      // 2. Bóc tách linh hoạt: Hỗ trợ cả Spring Data (content) và custom wrapper (data/items)
      const resData = response.data?.data || response.data || {};
      const feeList =
        resData.content ||
        resData.data ||
        resData.items ||
        (Array.isArray(resData) ? resData : []);
      const total =
        resData.totalElements ?? resData.total ?? feeList.length;

      setFees(feeList);
      setFeePagination({
        current: page,
        pageSize: pageSize,
        total: total,
      });
    } catch (error) {
      console.error("Lỗi tải phí:", error);
      message.error("Lỗi khi tải danh sách khoản phí");
    } finally {
      setFeeLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      fetchFees(1, feePagination.pageSize, feeFilters);
    }, 400);
    return () => clearTimeout(delayDebounceFn);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [feeFilters]);

  const handleOpenCreateFee = () => {
    setIsEditFee(false);
    setEditingFeeId(null);
    feeForm.resetFields();
    feeForm.setFieldsValue({ active: true });
    setFeeModalVisible(true);
  };

  const handleOpenEditFee = (record) => {
    setIsEditFee(true);
    setEditingFeeId(record.id);
    feeForm.setFieldsValue({
      name: record.name,
      feeAmount: record.feeAmount,
      dueDate: dayjs(record.dueDate),
      academicYearId: record.academicYearId,
      active: record.active,
    });
    setFeeModalVisible(true);
  };

  const handleSubmitFee = async (values) => {
    setFeeSubmitting(true);
    try {
      const payload = {
        ...values,
        dueDate: values.dueDate.format("YYYY-MM-DD"),
      };
      if (isEditFee) {
        await feeApi.updateFee(editingFeeId, payload);
        message.success("Cập nhật khoản phí thành công!");
      } else {
        await feeApi.createFee(payload);
        message.success("Tạo khoản phí thành công!");
      }
      setFeeModalVisible(false);
      fetchFees(feePagination.current, feePagination.pageSize, feeFilters);
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi lưu khoản phí");
    } finally {
      setFeeSubmitting(false);
    }
  };

  const handleGenerateInvoices = (fee) => {
    if (!fee.active) {
      message.warning("Không thể tạo hóa đơn cho khoản phí đang tắt!");
      return;
    }
    Modal.confirm({
      title: "Xác nhận tạo hóa đơn",
      content: `Bạn có chắc muốn tạo hóa đơn cho khoản phí "${fee.name}"? Hành động này sẽ tạo hóa đơn cho tất cả học sinh trong năm học tương ứng.`,
      okText: "Tạo",
      cancelText: "Hủy",
      onOk: async () => {
        setGenerateLoading(true);
        try {
          const response = await feeApi.generateInvoices(fee.id);
          const { createdCount, skippedCount } = response.data.data;
          message.success(
            `Tạo hóa đơn thành công: ${createdCount} tạo mới, ${skippedCount} bỏ qua.`
          );
          fetchFees(feePagination.current, feePagination.pageSize, feeFilters);
        } catch (error) {
          message.error(error.response?.data?.message || "Lỗi khi tạo hóa đơn");
        } finally {
          setGenerateLoading(false);
        }
      },
    });
  };

  return {
    state: {
      fees,
      feeLoading,
      feePagination,
      feeFilters,
      feeModalVisible,
      isEditFee,
      feeSubmitting,
      generateLoading,
      feeForm,
      academicYears,
    },
    actions: {
      setFeeFilters,
      handleOpenCreateFee,
      handleOpenEditFee,
      handleSubmitFee,
      handleGenerateInvoices,
      fetchFees,
      setFeeModalVisible,
    },
  };
};
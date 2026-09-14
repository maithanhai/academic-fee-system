import { useEffect, useState } from "react";
import { message } from "antd";
import notificationApi from "../api/notificationApi";

const useNotifications = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  const [filters, setFilters] = useState({ title: "", active: null });

  const fetchNotifications = async (
    page = 1,
    pageSize = 10,
    currentFilters = filters,
  ) => {
    setLoading(true);
    try {
      const response = await notificationApi.getNotifications({
        page: page - 1,
        size: pageSize,
        title: currentFilters.title || null,
        active: currentFilters.active,
      });
      const { data, totalElements } = response.data.data;
      setNotifications(data);
      setPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error("Lỗi khi tải danh sách thông báo!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(
      () => fetchNotifications(1, pagination.pageSize, filters),
      500,
    );
    return () => clearTimeout(timer);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  return {
    notifications,
    loading,
    pagination,
    filters,
    setFilters,
    fetchNotifications,
  };
};

export default useNotifications;

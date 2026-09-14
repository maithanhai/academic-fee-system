import { useEffect, useState } from 'react';
import { message } from 'antd';
import adminUserApi from '../api/adminUserApi';
import departmentApi from '../api/departmentApi';

const useTeachers = () => {
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  const [filters, setFilters] = useState({ keyword: '', active: null, departmentId: null });
  const [departments, setDepartments] = useState([]);

  const fetchTeachers = async (page = 1, pageSize = 10, currentFilters = filters) => {
    setLoading(true);
    try {
      const response = await adminUserApi.getTeachers({
        page: page - 1,
        size: pageSize,
        keyword: currentFilters.keyword || null,
        departmentId: currentFilters.departmentId,
        active: currentFilters.active,
      });
      const { data, totalElements } = response.data.data;
      setTeachers(data);
      setPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error('Lỗi khi tải danh sách giáo viên!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    departmentApi.getDepartments()
      .then((response) => setDepartments(response.data?.data || []))
      .catch(() => message.error('Lỗi khi tải tổ bộ môn!'));
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => fetchTeachers(1, pagination.pageSize, filters), 300);
    return () => clearTimeout(timer);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  return { teachers, loading, pagination, filters, setFilters, departments, fetchTeachers };
};

export default useTeachers;

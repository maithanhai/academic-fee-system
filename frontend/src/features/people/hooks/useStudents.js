import { useEffect, useState } from 'react';
import { message } from 'antd';
import adminUserApi from '../api/adminUserApi';
import studentApi from '../api/studentApi';
import academicYearApi from '../../academic/api/academicYearApi'; 
import cohortApi from '../../academic/api/cohortApi';

const useStudents = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  
  const [filters, setFilters] = useState({ 
    keyword: '', 
    active: null, 
    cohortId: null 
  });
  
  const [academicYears, setAcademicYears] = useState([]);
  const [cohorts, setCohorts] = useState([]);
  const [importing, setImporting] = useState(false);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [yearRes, cohortRes] = await Promise.all([
          academicYearApi.getAcademicYears(), 
          cohortApi.getCohorts()
        ]);
        setAcademicYears(yearRes.data?.data || []);
        setCohorts(cohortRes.data?.data || []);
      } catch {
        message.error('Lỗi khi tải dữ liệu danh mục ban đầu!');
      }
    };
    fetchInitialData();
  }, []);

  const fetchStudents = async (page = 1, pageSize = 10, currentFilters = filters) => {
    setLoading(true);
    try {
      const response = await adminUserApi.getStudents({
        page: page - 1,
        size: pageSize,
        keyword: currentFilters.keyword || null,
        active: currentFilters.active,
        cohortId: currentFilters.cohortId
      });
      const { data, totalElements } = response.data.data;
      setStudents(data);
      setPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error('Lỗi khi tải danh sách học sinh!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => fetchStudents(1, pagination.pageSize, filters), 300);
    return () => clearTimeout(timer);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  const importStudents = async (file, academicYearId, cohortId) => {
    setImporting(true);
    const hide = message.loading('Đang import dữ liệu, vui lòng chờ...', 0);
    try {
      const res = await studentApi.importStudentsExcel(file, academicYearId, cohortId);
      const result = res.data?.data;
      const successCount = result?.importedCount || 0;
      const skippedRows = result?.skippedRows || [];

      if (skippedRows.length > 0) {
        message.warning(
          `Import thành công ${successCount} học sinh. Bỏ qua ${skippedRows.length} dòng không hợp lệ: ${skippedRows.slice(0, 3).join('; ')}` +
          (skippedRows.length > 3 ? '...' : '')
        );
      } else {
        message.success(`Import thành công ${successCount} học sinh!`);
      }

      fetchStudents(pagination.current, pagination.pageSize, filters);
    } catch (error) {
      message.error(error.response?.data?.message || 'Import thất bại!');
    } finally {
      hide();
      setImporting(false);
    }
    return false;
  };

  return { 
    students, loading, pagination, filters, setFilters, 
    academicYears, cohorts,
    importing, importStudents, fetchStudents 
  };
};

export default useStudents;
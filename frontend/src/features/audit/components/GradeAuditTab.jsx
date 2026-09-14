import { useEffect, useState } from 'react';
import { DatePicker, Input, Select, Table, Space, Typography, message, Button } from 'antd';
import { EyeOutlined, SearchOutlined } from '@ant-design/icons';
import auditLogApi from '../api/auditLogApi';

const { Text } = Typography;
const { RangePicker } = DatePicker;

const GradeAuditTab = ({ onOpenDetail }) => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  const [filters, setFilters] = useState({ actorName: '', action: null, dateRange: null });

  const fetchLogs = async (page = 1, pageSize = 10, currentFilters = filters) => {
    setLoading(true);
    try {
      const params = {
        page: page - 1,
        size: pageSize,
        actorName: currentFilters.actorName || null,
        action: currentFilters.action || null,
        fromDate: currentFilters.dateRange?.[0]?.format('YYYY-MM-DD') || null,
        toDate: currentFilters.dateRange?.[1]?.format('YYYY-MM-DD') || null,
      };
      const response = await auditLogApi.getGradeAuditLogs(params);
      const { data, totalElements } = response.data.data;
      setLogs(data);
      setPagination({ current: page, pageSize, total: totalElements });
    } catch {
      message.error('Lỗi khi tải lịch sử điểm số!');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => fetchLogs(1, pagination.pageSize, filters), 500);
    return () => clearTimeout(timer);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 70 },
    { title: 'THỜI GIAN', dataIndex: 'createdDate', render: (date) => <Text>{new Date(date).toLocaleString('vi-VN')}</Text> },
    { title: 'NGƯỜI THỰC HIỆN', dataIndex: 'actorFullName', render: (name) => <Text strong>{name}</Text> },
    { title: 'HÀNH ĐỘNG', dataIndex: 'action' },
    { title: 'IP', dataIndex: 'ipAddress', width: 150 },
    { title: 'THAO TÁC', width: 100, render: (_, record) => <Button size="small" icon={<EyeOutlined />} onClick={() => onOpenDetail(record.id)} /> },
  ];

  return (
    <Space orientation="vertical" size="middle" style={{ display: 'flex' }}>
      <Space wrap>
        <Input prefix={<SearchOutlined />} placeholder="Tên người thực hiện..." allowClear style={{ width: 220 }} onChange={(event) => setFilters((previous) => ({ ...previous, actorName: event.target.value }))} />
        <Select placeholder="Loại hành động" allowClear style={{ width: 180 }} onChange={(value) => setFilters((previous) => ({ ...previous, action: value }))} options={[{ value: 'UPDATE_GRADE', label: 'Cập nhật điểm' }]} />
        <RangePicker placeholder={['Từ ngày', 'Đến ngày']} format="DD/MM/YYYY" onChange={(dates) => setFilters((previous) => ({ ...previous, dateRange: dates }))} />
      </Space>
      <Table columns={columns} dataSource={logs} rowKey="id" loading={loading} bordered pagination={{ ...pagination, showSizeChanger: true }} onChange={(page) => fetchLogs(page.current, page.pageSize, filters)} />
    </Space>
  );
};

export default GradeAuditTab;

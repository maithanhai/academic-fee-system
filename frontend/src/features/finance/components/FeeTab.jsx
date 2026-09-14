import {
  Table, Input, Select, Button, Space, Typography, Tag,
} from 'antd';
import {
  SearchOutlined, PlusOutlined, EditOutlined, SendOutlined, EyeOutlined
} from '@ant-design/icons';
import dayjs from 'dayjs';
import FeeFormModal from './FeeFormModal';
import { useFeeManagement } from '../hooks/useFeeManagement';

const { Text } = Typography;

const FeeTab = ({ onSwitchToInvoice }) => {
  const { state, actions } = useFeeManagement();

  const feeColumns = [
    { title: 'ID', dataIndex: 'id', width: 60 },
    { title: 'TÊN KHOẢN PHÍ', dataIndex: 'name', render: (text) => <Text strong>{text}</Text> },
    {
      title: 'SỐ TIỀN',
      dataIndex: 'feeAmount',
      render: (amount) => <Text style={{ color: '#1677ff' }}>{amount?.toLocaleString('vi-VN')} ₫</Text>,
    },
    { title: 'NĂM HỌC', dataIndex: 'academicYearName' },
    {
      title: 'HẠN ĐÓNG',
      dataIndex: 'dueDate',
      render: (date) => {
        const isExpired = dayjs(date).isBefore(dayjs().startOf('day'));
        return (
          <Space orientation="vertical" size={0}>
            <Text>{dayjs(date).format('DD/MM/YYYY')}</Text>
            {isExpired ? <Tag color="error">Quá hạn</Tag> : <Tag color="processing">Còn hạn</Tag>}
          </Space>
        );
      },
    },
    {
      title: 'TRẠNG THÁI',
      dataIndex: 'active',
      render: (active) => <Tag color={active ? 'success' : 'default'}>{active ? 'Hoạt động' : 'Đã tắt'}</Tag>,
    },
    {
      title: 'THAO TÁC',
      render: (_, record) => {
        const hasInvoices = record.invoiceCount > 0;

        return (
          <Space>
            <Button size="small" icon={<EditOutlined />} onClick={() => actions.handleOpenEditFee(record)}>
              Sửa
            </Button>
            
            {hasInvoices ? (
              <Button size="small" icon={<EyeOutlined />} onClick={() => onSwitchToInvoice(record.id)}>
                Xem hóa đơn
              </Button>
            ) : (
              <Button
                size="small"
                type="primary"
                icon={<SendOutlined />}
                disabled={!record.active}
                loading={actions.generateLoading}
                onClick={() => actions.handleGenerateInvoices(record)}
              >
                Tạo hóa đơn
              </Button>
            )}
          </Space>
        );
      },
    },
  ];

  return (
    <>
      <Space orientation="vertical" size="middle" style={{ display: 'flex' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 16 }}>
          <Space style={{ flexWrap: 'wrap' }}>
            <Input
              prefix={<SearchOutlined />}
              placeholder="Tìm tên khoản phí..."
              allowClear
              onChange={(event) => actions.setFeeFilters((previous) => ({ ...previous, keyword: event.target.value }))}
              style={{ width: 250 }}
            />
            <Select
              placeholder="Trạng thái"
              allowClear
              style={{ width: 150 }}
              onChange={(value) => actions.setFeeFilters((previous) => ({ ...previous, active: value }))}
              options={[{ value: true, label: 'Hoạt động' }, { value: false, label: 'Đã tắt' }]}
            />
            <Select
              placeholder="Hạn nộp"
              allowClear
              style={{ width: 150 }}
              onChange={(value) => actions.setFeeFilters((previous) => ({ ...previous, isExpired: value }))}
              options={[{ value: false, label: 'Còn hạn' }, { value: true, label: 'Đã quá hạn' }]}
            />
            <Select
              placeholder="Năm học"
              allowClear
              onChange={(value) => actions.setFeeFilters((previous) => ({ ...previous, academicYearId: value }))}
              options={state.academicYears.map((year) => ({
                value: year.id,
                label: year.name,
              }))}
            />
          </Space>
          <Button type="primary" onClick={actions.handleOpenCreateFee} icon={<PlusOutlined />}>Tạo Khoản Phí</Button>
        </div>
        <Table
          columns={feeColumns}
          dataSource={state.fees}
          rowKey="id"
          loading={state.feeLoading}
          bordered
          pagination={{
            current: state.feePagination.current,
            pageSize: state.feePagination.pageSize,
            total: state.feePagination.total,
            showSizeChanger: true,
          }}
          onChange={(pagination) => actions.fetchFees(pagination.current, pagination.pageSize, state.feeFilters)}
        />
      </Space>

      <FeeFormModal
        open={state.feeModalVisible}
        form={state.feeForm}
        editing={state.isEditFee}
        submitting={state.feeSubmitting}
        onCancel={() => actions.setFeeModalVisible(false)}
        onSubmit={actions.handleSubmitFee}
        academicYears={state.academicYears}
      />
    </>
  );
};

export default FeeTab;
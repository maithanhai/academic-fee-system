import { Drawer, Spin, Descriptions, Typography, Tag } from 'antd';
import dayjs from 'dayjs';

const { Text } = Typography;

const InvoiceDetailDrawer = ({ open, loading, invoice, onClose }) => {
  const getStatusTag = (status) => {
    const statusMap = {
      UNPAID: { color: 'warning', text: 'Chưa thanh toán' },
      PENDING: { color: 'processing', text: 'Đang xử lý' },
      PAID: { color: 'success', text: 'Đã thanh toán' },
      CANCELLED: { color: 'error', text: 'Đã hủy' },
    };
    const config = statusMap[status] || { color: 'default', text: status };
    return <Tag color={config.color}>{config.text}</Tag>;
  };

  return (
    <Drawer title="Chi tiết hóa đơn" size={500} onClose={onClose} open={open}>
      <Spin spinning={loading}>
        {invoice && (
          <Descriptions column={1} bordered size="small">
            <Descriptions.Item label="Mã HĐ">{invoice.id}</Descriptions.Item>
            <Descriptions.Item label="Học sinh">
              <Text strong>{invoice.studentName}</Text>
            </Descriptions.Item>
            <Descriptions.Item label="Khoản phí">{invoice.feeName}</Descriptions.Item>
            <Descriptions.Item label="Số tiền">
              <Text type="success" strong>{invoice.amount?.toLocaleString('vi-VN')} ₫</Text>
            </Descriptions.Item>
            <Descriptions.Item label="Hạn đóng">
              {dayjs(invoice.dueDate).format('DD/MM/YYYY')}
            </Descriptions.Item>
            <Descriptions.Item label="Trạng thái">
              {getStatusTag(invoice.status)}
            </Descriptions.Item>
            <Descriptions.Item label="Phương thức">
              {invoice.paymentMethod || 'Chưa có'}
            </Descriptions.Item>
            <Descriptions.Item label="Người xử lý">
              {invoice.actionByName || '---'}
            </Descriptions.Item>
            {invoice.undoReason && (
              <Descriptions.Item label="Lý do hoàn tác">
                <Text type="danger">{invoice.undoReason}</Text>
              </Descriptions.Item>
            )}
          </Descriptions>
        )}
      </Spin>
    </Drawer>
  );
};

export default InvoiceDetailDrawer;

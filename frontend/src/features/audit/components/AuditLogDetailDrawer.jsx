import { Drawer, Spin, Descriptions, Typography } from 'antd';
import dayjs from 'dayjs';

const { Text } = Typography;

const AuditLogDetailDrawer = ({ open, loading, detail, onClose }) => {
  const formatPayload = (payload) => {
    if (!payload) return 'Không có dữ liệu';
    try {
      return JSON.stringify(JSON.parse(payload), null, 2);
    } catch {
      return payload;
    }
  };

  return (
    <Drawer
      title={detail ? `Chi tiết Log ID: ${detail.id}` : 'Đang tải...'}
      size={650}
      onClose={onClose}
      open={open}
    >
      <Spin spinning={loading}>
        {detail && (
          <Descriptions column={1} bordered size="small">
            <Descriptions.Item label="Thời gian">
              {dayjs(detail.createdDate).format('DD/MM/YYYY HH:mm:ss')}
            </Descriptions.Item>
            <Descriptions.Item label="Người thực hiện">
              <Text strong>{detail.actorFullName}</Text> (ID: {detail.actorId})
            </Descriptions.Item>
            <Descriptions.Item label="Hành động">
              <Text code>{detail.action}</Text>
            </Descriptions.Item>
            <Descriptions.Item label="Địa chỉ IP">
              {detail.ipAddress || '---'}
            </Descriptions.Item>
            <Descriptions.Item label="Chi tiết">
              <pre style={{
                backgroundColor: '#f5f5f5',
                padding: '12px',
                borderRadius: '6px',
                overflowX: 'auto',
                border: '1px solid #e8e8e8',
                fontSize: '13px',
              }}>
                {formatPayload(detail.payload)}
              </pre>
            </Descriptions.Item>
          </Descriptions>
        )}
      </Spin>
    </Drawer>
  );
};

export default AuditLogDetailDrawer;

import { useState } from 'react';
import { Card, Tabs, Typography } from 'antd';
import FeeTab from '../components/FeeTab';
import InvoiceTab from '../components/InvoiceTab';

const { Title } = Typography;

const AdminFinancePage = () => {
  const [activeTab, setActiveTab] = useState('fees');
  const [targetFeeId, setTargetFeeId] = useState(null);

  const handleSwitchToInvoice = (feeId) => {
    setTargetFeeId(feeId);
    setActiveTab('invoices');
  };

  return (
    <Card variant={false} title={<Title level={3} style={{ margin: 0 }}>Quản lý Tài chính</Title>}>
      <Tabs
        activeKey={activeTab}
        onChange={setActiveTab}
        items={[
          { 
            key: 'fees', 
            label: 'Khoản phí', 
            children: <FeeTab onSwitchToInvoice={handleSwitchToInvoice} /> 
          },
          { 
            key: 'invoices', 
            label: 'Hóa đơn', 
            children: <InvoiceTab preSelectedFeeId={targetFeeId} /> 
          },
        ]}
        type="card"
      />
    </Card>
  );
};

export default AdminFinancePage;
import { useState } from "react";
import { Card, Tabs, Typography } from "antd";
import AuditLogDetailDrawer from "../components/AuditLogDetailDrawer";
import GradeAuditTab from "../components/GradeAuditTab";
import InvoiceAuditTab from "../components/InvoiceAuditTab";
import useAuditLogDetail from "../hooks/useAuditLogDetail";

const { Title } = Typography;

const AdminAuditLogPage = () => {
  const [activeTab, setActiveTab] = useState("grades");
  const detail = useAuditLogDetail();

  return (
    <>
      <Card
        variant={false}
        title={
          <Title level={3} style={{ margin: 0 }}>
            Nhật ký Hệ thống
          </Title>
        }
      >
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          type="card"
          items={[
            {
              key: "grades",
              label: "Lịch sử Điểm số",
              children: (
                <GradeAuditTab
                  onOpenDetail={(id) => detail.openDetail(id, "grades")}
                />
              ),
            },
            {
              key: "invoices",
              label: "Lịch sử Hóa đơn",
              children: (
                <InvoiceAuditTab
                  onOpenDetail={(id) => detail.openDetail(id, "invoices")}
                />
              ),
            },
          ]}
        />
      </Card>
      <AuditLogDetailDrawer
        open={detail.open}
        loading={detail.loading}
        detail={detail.detail}
        onClose={detail.close}
      />
    </>
  );
};

export default AdminAuditLogPage;

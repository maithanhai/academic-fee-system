import { useEffect } from "react";
import { Table, Input, Select, Button, Space, Typography, Tag } from "antd";
import {
  SearchOutlined,
  CheckCircleOutlined,
  RollbackOutlined,
  EyeOutlined,
} from "@ant-design/icons";
import InvoiceDetailDrawer from "./InvoiceDetailDrawer";
import UndoInvoiceModal from "./UndoInvoiceModal";
import { useInvoiceManagement } from "../hooks/useInvoiceManagement";

const { Text } = Typography;

const InvoiceTab = ({ preSelectedFeeId }) => {
  const { state, actions } = useInvoiceManagement();

  useEffect(() => {
    if (preSelectedFeeId) {
    actions.setInvoiceFilters((prev) => {
      if (prev.feeId === preSelectedFeeId) return prev;
      return {
        ...prev,
        feeId: preSelectedFeeId,
      };
    });
  }
  // eslint-disable-next-line react-hooks/exhaustive-deps
}, [preSelectedFeeId]); 

  const getStatusTag = (status) => {
    const statusMap = {
      UNPAID: { color: "warning", text: "Chưa thanh toán" },
      PENDING: { color: "processing", text: "Đang xử lý" },
      PAID: { color: "success", text: "Đã thanh toán" },
      CANCELLED: { color: "error", text: "Đã hủy" },
    };
    const config = statusMap[status] || { color: "default", text: status };
    return <Tag color={config.color}>{config.text}</Tag>;
  };

  const invoiceColumns = [
    { title: "ID", dataIndex: "invoiceId", width: 60 },
    {
      title: "HỌC SINH",
      dataIndex: "studentName",
      render: (text) => <Text strong>{text}</Text>,
    },
    { title: "KHOẢN PHÍ", dataIndex: "feeName", width: 200, ellipsis: true },
    {
      title: "SỐ TIỀN",
      dataIndex: "amount",
      render: (amount) => (
        <Text style={{ color: "#1677ff" }}>
          {amount?.toLocaleString("vi-VN")} ₫
        </Text>
      ),
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "status",
      render: (status) => getStatusTag(status),
    },
    {
      title: "THAO TÁC",
      width: 250,
      render: (_, record) => (
        <Space>
          <Button
            size="small"
            icon={<EyeOutlined />}
            onClick={() => actions.handleOpenDetail(record.invoiceId)}
          />
          {(record.status === "UNPAID" || record.status === "PENDING") && (
            <Button
              size="small"
              type="primary"
              style={{ backgroundColor: "#52c41a" }}
              icon={<CheckCircleOutlined />}
              onClick={() => actions.handleConfirmCash(record)}
            >
              Thu tiền mặt
            </Button>
          )}
          {record.status === "PAID" && (
            <Button
              size="small"
              danger
              icon={<RollbackOutlined />}
              onClick={() => actions.handleOpenUndo(record.invoiceId)}
            >
              Hoàn tác
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <>
      <Space orientation="vertical" size="middle" style={{ display: "flex" }}>
        <div style={{ display: "flex", gap: 16, flexWrap: "wrap", alignItems: 'center' }}>
          <Input
            prefix={<SearchOutlined />}
            placeholder="Tên học sinh hoặc khoản phí..."
            allowClear
            value={state.invoiceFilters.keyword}
            onChange={(event) =>
              actions.setInvoiceFilters((previous) => ({
                ...previous,
                keyword: event.target.value,
              }))
            }
            style={{ width: 300 }}
          />
          <Select
            placeholder="Trạng thái thanh toán"
            allowClear
            value={state.invoiceFilters.status}
            style={{ width: 200 }}
            onChange={(value) =>
              actions.setInvoiceFilters((previous) => ({
                ...previous,
                status: value,
              }))
            }
            options={[
              { value: "UNPAID", label: "Chưa thanh toán" },
              { value: "PENDING", label: "Đang xử lý" },
              { value: "PAID", label: "Đã thanh toán" },
              { value: "CANCELLED", label: "Đã hủy" },
            ]}
          />
          {state.invoiceFilters.feeId && (
            <Tag
              closable
              onClose={() => actions.setInvoiceFilters(prev => ({ ...prev, feeId: null }))}
              color="blue"
              style={{ padding: '4px 10px', fontSize: 14 }}
            >
              Đang xem hóa đơn của Khoản phí ID: {state.invoiceFilters.feeId}
            </Tag>
          )}
        </div>
        <Table
          columns={invoiceColumns}
          dataSource={state.invoices}
          rowKey="id"
          loading={state.invoiceLoading}
          bordered
          pagination={{
            current: state.invoicePagination.current,
            pageSize: state.invoicePagination.pageSize,
            total: state.invoicePagination.total,
            showSizeChanger: true,
          }}
          onChange={(pagination) =>
            actions.fetchInvoices(
              pagination.current,
              pagination.pageSize,
              state.invoiceFilters,
            )
          }
        />
      </Space>

      <UndoInvoiceModal
        open={state.undoModalVisible}
        submitting={state.undoSubmitting}
        onCancel={() => actions.setUndoModalVisible(false)}
        onSubmit={actions.submitUndo}
      />

      <InvoiceDetailDrawer
        open={state.drawerVisible}
        loading={state.detailLoading}
        invoice={state.invoiceDetail}
        onClose={() => actions.setDrawerVisible(false)}
      />
    </>
  );
};

export default InvoiceTab;
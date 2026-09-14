import { useState, useEffect, useCallback } from "react";
import {
  Drawer,
  Table,
  Typography,
  Tag,
  Space,
  Button,
  message,
} from "antd";
import {
  CheckCircleOutlined,
  RollbackOutlined,
  UserOutlined,
  IdcardOutlined
} from "@ant-design/icons";
import feeInvoiceApi from "../../finance/api/feeInvoiceApi";
import UndoInvoiceModal from "../../finance/components/UndoInvoiceModal";
import { toDisplayDate } from "../../../shared/utils/dateUtils";

const { Text } = Typography;

const StudentInvoiceDrawer = ({
  open,
  onClose,
  student,
  classId,
  academicYearId,
}) => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [undoModalVisible, setUndoModalVisible] = useState(false);
  const [undoSubmitting, setUndoSubmitting] = useState(false);
  const [selectedInvoiceId, setSelectedInvoiceId] = useState(null);

  const fetchInvoices = useCallback(async () => {
    if (!student?.studentId || !classId || !academicYearId || !open) return;
    try {
      setLoading(true);
      const res = await feeInvoiceApi.getInvoicesByTeacher(
        student.studentId,
        classId,
        academicYearId
      );
      setInvoices(res.data?.data || []);
    } catch {
      message.error("Lỗi khi tải danh sách khoản phí");
    } finally {
      setLoading(false);
    }
  }, [student, classId, academicYearId, open]);

  useEffect(() => {
    if (open) {
      Promise.resolve().then(() => fetchInvoices());
    } else {
      Promise.resolve().then(() => setInvoices([]));
    }
  }, [open, fetchInvoices]);

  const handleConfirmCash = async (record) => {
    try {
      setLoading(true);
      await feeInvoiceApi.confirmCashByTeacher(classId,{
        studentId: student.studentId,
        invoiceId: record.id,
      });
      message.success("Xác nhận thu tiền mặt thành công!");
      fetchInvoices();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi thu tiền");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenUndo = (id) => {
    setSelectedInvoiceId(id);
    setUndoModalVisible(true);
  };

  const submitUndo = async (values) => {
    try {
      setUndoSubmitting(true);
      await feeInvoiceApi.undoInvoiceByTeacher(classId,{
        studentId: student.studentId,
        invoiceId: selectedInvoiceId,
        undoReason: values.reason,
      });
      message.success("Hoàn tác hóa đơn thành công!");
      setUndoModalVisible(false);
      fetchInvoices();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi hoàn tác");
    } finally {
      setUndoSubmitting(false);
    }
  };

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
    { title: "ID", dataIndex: "id", width: 80, align: "center" },
    { title: "KHOẢN PHÍ", dataIndex: "feeName", width:250,ellipsis: true },
    {
      title: "SỐ TIỀN",
      dataIndex: "amount",
      align: "right",
      render: (amount) => (
        <Text style={{ color: "#1677ff", fontWeight: 500 }}>
          {amount?.toLocaleString("vi-VN")} ₫
        </Text>
      ),
    },
    {
      title: "HẠN ĐÓNG",
      dataIndex: "dueDate",
      align: "center",
      render: (date) => (toDisplayDate(date)) ,
    },
    {
      title: "HÌNH THỨC",
      dataIndex: "paymentMethod",
      align: "center",
      render: (method) => {
        if (!method) return <Text type="secondary">-</Text>;
        if (method === "CASH") return <Tag color="cyan">Tiền mặt</Tag>;
        if (method === "BANK_TRANSFER") return <Tag color="blue">Chuyển khoản</Tag>;
        return <Tag>{method}</Tag>;
      }
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "status",
      align: "center",
      render: (status) => getStatusTag(status),
    },
    {
      title: "THAO TÁC",
      width: 150, 
      align: "center",
      render: (_, record) => (
        <Space>
          {(record.status === "UNPAID" || record.status === "PENDING") && (
            <Button
              size="small"
              type="primary"
              style={{ backgroundColor: "#52c41a" }}
              icon={<CheckCircleOutlined />}
              onClick={() => handleConfirmCash(record)}
            >
              Thu tiền mặt
            </Button>
          )}
          {record.status === "PAID" && record.paymentMethod === "CASH" && (
            <Button
              size="small"
              danger
              icon={<RollbackOutlined />}
              onClick={() => handleOpenUndo(record.id)}
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
      <Drawer
        title="Danh sách các khoản phí"
        placement="right"
        size={1000}
        onClose={onClose}
        open={!!open}
        destroyOnHidden
      >
        <div style={{ 
          marginBottom: 24, 
          padding: '16px 24px', 
          backgroundColor: '#f5f5f5', 
          borderRadius: '8px',
          display: 'flex',
          gap: '40px'
        }}>
          <Space>
            <IdcardOutlined style={{ color: '#8c8c8c', fontSize: 18 }} />
            <Text type="secondary">Mã học sinh:</Text>
            <Text strong style={{ fontSize: 16 }}>{student?.studentId}</Text>
          </Space>
          <Space>
            <UserOutlined style={{ color: '#8c8c8c', fontSize: 18 }} />
            <Text type="secondary">Họ và tên:</Text>
            <Text strong style={{ fontSize: 16, color: '#1677ff' }}>{student?.studentName}</Text>
          </Space>
        </div>

        <Table
          columns={invoiceColumns}
          dataSource={invoices}
          rowKey="id"
          loading={loading}
          pagination={false}
          bordered
          size="small"
          locale={{ emptyText: "Không có khoản thu nào" }}
        />
      </Drawer>

      <UndoInvoiceModal
        open={undoModalVisible}
        submitting={undoSubmitting}
        onCancel={() => setUndoModalVisible(false)}
        onSubmit={submitUndo}
      />
    </>
  );
};

export default StudentInvoiceDrawer;
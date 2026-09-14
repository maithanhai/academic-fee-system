import { useState, useEffect, useCallback, useMemo } from "react";
import { Card, Typography, Table, Select, Space, Button, message, Alert, Tag, Modal, Statistic } from "antd";
import { EyeOutlined, LockOutlined, DollarOutlined } from "@ant-design/icons";
import academicYearApi from "../../academic/api/academicYearApi";
import feeInvoiceApi from "../api/feeInvoiceApi";
import { toDisplayDate } from "../../../shared/utils/dateUtils"; 
const { Title, Text } = Typography;

const StudentFinancePage = () => {
  const [academicYears, setAcademicYears] = useState([]);
  const [invoices, setInvoices] = useState([]);
  const [selectedYearId, setSelectedYearId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [invoiceDetail, setInvoiceDetail] = useState(null);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [paying, setPaying] = useState(false);
  const [deadline, setDeadline] = useState(0);
  const fetchInitialData = useCallback(async () => {
    try {
      setLoading(true);
      const yearRes = await academicYearApi.getAcademicYearsByStudent();
      const years = yearRes.data?.data || [];
      setAcademicYears(years);
      if (years.length > 0) {
        const activeYear = years.find(y => y.active) || years[0];
        setSelectedYearId(activeYear.id);
      }
    } catch {
      message.error("Lỗi khi tải dữ liệu năm học");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    Promise.resolve().then(() => fetchInitialData());
  }, [fetchInitialData]);

  useEffect(() => {
    if (!selectedYearId) return;
    const fetchInvoices = async () => {
      setLoading(true);
      try {
        const res = await feeInvoiceApi.getInvoicesByStudent(selectedYearId);
        setInvoices(res.data?.data || []);
      } catch {
        message.error("Lỗi khi tải danh sách hóa đơn");
      } finally {
        setLoading(false);
      }
    };
    fetchInvoices();
  }, [selectedYearId]);

  const handleOpenModal = async (invoiceId) => {
    setIsModalVisible(true);
    setLoadingDetail(true);
    try {
      const res = await feeInvoiceApi.getInvoiceByStudent(invoiceId);
      const detail = res.data?.data;
      setInvoiceDetail(detail);

      if (detail?.status === "UNPAID") {
        setDeadline(Date.now() + 15 * 60 * 1000);
      }
    } catch {
      message.error("Lỗi tải chi tiết hóa đơn!");
      setIsModalVisible(false);
    } finally {
      setLoadingDetail(false);
    }
  };

  const handlePayment = () => {
    if (!invoiceDetail) return;
    setPaying(true);
    
    setTimeout(async () => {
      try {
        await feeInvoiceApi.payInvoiceByStudent(invoiceDetail.id);
        message.success("Nhận được thông báo từ ngân hàng: Thanh toán thành công!");
        setIsModalVisible(false);
        
        const refreshRes = await feeInvoiceApi.getInvoicesByStudent(selectedYearId);
        setInvoices(refreshRes.data?.data || []);
      } catch (error) {
        message.error(error.response?.data?.message || "Thanh toán thất bại!");
      } finally {
        setPaying(false);
      }
    }, 2000); 
  };

  const currentYearObj = academicYears.find((y) => y.id === selectedYearId);
  const isYearLocked = currentYearObj ? !currentYearObj.active : false;

  const columns = useMemo(() => [
    {
      title: "TÊN KHOẢN THU",
      dataIndex: "feeName",
      key: "feeName",
      render: (text) => <Text strong>{text}</Text>,
    },
    {
      title: "SỐ TIỀN",
      dataIndex: "amount",
      key: "amount",
      align: "right",
      render: (amount) => (
        <Text style={{  fontWeight: 500 }}>
          {new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount)}
        </Text>
      ),
    },
    {
      title: "TRẠNG THÁI",
      dataIndex: "status",
      key: "status",
      align: "center",
      render: (status) => {
        if (status === "PAID") return <Tag color="success">Đã thanh toán</Tag>;
        if (status === "UNPAID") return <Tag color="error">Chưa thanh toán</Tag>;
        return <Tag color="default">{status}</Tag>;
      }
    },
    {
      title: "THAO TÁC",
      key: "action",
      align: "center",
      render: (_, record) => (
        record.status === "UNPAID" ? (
          <Button
            type="primary"
            size="small"
            icon={<DollarOutlined />}
            disabled={isYearLocked}
            onClick={() => handleOpenModal(record.id)}
          >
            Thanh toán
          </Button>
        ) : (
          <Button
            size="small"
            icon={<EyeOutlined />}
            onClick={() => handleOpenModal(record.id)}
          >
            Xem chi tiết
          </Button>
        )
      ),
    },
  ], [isYearLocked]);

  return (
    <Card
      variant={false}
      title={
        <Title level={3} style={{ margin: 0 }}>
          Học phí của tôi
        </Title>
      }
    >
      <Space orientation="vertical" size="middle" style={{ display: "flex", width: "100%" }}>
        
        {isYearLocked && (
          <Alert
            title={`Năm học ${currentYearObj?.name} đã đóng`}
            type="warning"
            showIcon
            icon={<LockOutlined />}
          />
        )}

        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            flexWrap: "wrap",
            gap: 16,
          }}
        >
          <Space wrap>
            <span style={{ fontWeight: 500, marginRight: 8 }}>Năm học:</span>
            <Select
              value={selectedYearId}
              onChange={setSelectedYearId}
              style={{ width: 250 }}
              loading={loading}
              options={academicYears.map((year) => ({
                value: year.id,
                label: year.name,
              }))}
            />
          </Space>
        </div>

        <Table
          columns={columns}
          dataSource={invoices}
          rowKey="id"
          loading={loading}
          pagination={false}
          bordered
          size="small"
        />
      </Space>

      <Modal
        title={invoiceDetail?.status === "UNPAID" ? "" : "Chi tiết hóa đơn"}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        width={invoiceDetail?.status === "UNPAID" ? 650 : 500} 
        footer={[
          <Button key="back" onClick={() => setIsModalVisible(false)}>
            Đóng
          </Button>,
          invoiceDetail?.status === "UNPAID" && !isYearLocked && (
            <Button
              key="submit"
              type="primary"
              loading={paying}
              onClick={handlePayment}
            >
              Xác nhận đã thanh toán
            </Button>
          ),
        ]}
      >
        {loadingDetail ? (
          <p>Đang tải thông tin...</p>
        ) : invoiceDetail ? (
          <div
            style={{
              display: "flex",
              flexDirection: invoiceDetail.status === "UNPAID" ? "row" : "column",
              gap: 24,
              alignItems: invoiceDetail.status === "UNPAID" ? "flex-start" : "stretch",
            }}
          >
            {invoiceDetail.status === "UNPAID" && (
              <div style={{ flex: 1, textAlign: "center", borderRight: "1px dashed #d9d9d9", paddingRight: 24 }}>
                <Text strong style={{ fontSize: 16 }}>Quét mã để thanh toán</Text>
                
                <div style={{ margin: "12px 0" }}>
                  <Statistic.Timer
                    type="countdown"
                    value={deadline} 
                    format="mm:ss" 
                    style={{ color: '#cf1322', fontSize: 24, fontWeight: 'bold' }} 
                    onFinish={() => {
                      message.warning("Đã hết phiên giao dịch, vui lòng thử lại!");
                      setIsModalVisible(false); 
                    }}
                  />
                  <Text type="secondary" style={{ fontSize: 12 }}>Thời gian giao dịch</Text>
                </div>

                <div style={{ marginTop: 8, marginBottom: 16 }}>
                  <img
                    src={`https://img.vietqr.io/image/970422-0931123456-compact2.png?amount=${invoiceDetail.amount}&addInfo=HP_${invoiceDetail.id}&accountName=TRUONG THPT ABC`}
                    alt="VietQR"
                    style={{ width: "100%", maxWidth: 220, borderRadius: 8, border: "1px solid #f0f0f0" }}
                  />
                </div>
                <Text type="secondary" style={{ fontSize: 12 }}>
                  Sử dụng App ngân hàng hoặc Momo để quét mã
                </Text>
              </div>
            )}

            <div style={{ flex: 1 }}>
              <Space orientation="vertical" size="small" style={{ width: "100%" }}>
                <Text><strong>Mã hóa đơn:</strong> #{invoiceDetail.id}</Text>
                <Text><strong>Học sinh:</strong> {invoiceDetail.studentName}</Text>
                <Text><strong>Khoản thu:</strong> {invoiceDetail.feeName}</Text>
                <Text>
                  <strong>Số tiền: </strong>
                  <Text type="danger" strong style={{ fontSize: 16 }}>
                    {new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(invoiceDetail.amount)}
                  </Text>
                </Text>
                <Text><strong>Hạn nộp:</strong> {toDisplayDate(invoiceDetail.dueDate)}</Text>
                
                {invoiceDetail.status === "UNPAID" && (
                  <div style={{ marginTop: 12, padding: "8px 12px", backgroundColor: "#fffbe6", border: "1px solid #ffe58f", borderRadius: 6 }}>
                    <Text strong style={{ color: "#d48806" }}>Nội dung chuyển khoản:</Text>
                    <div style={{ fontSize: 18, fontWeight: "bold", textAlign: "center", marginTop: 4, letterSpacing: 2 }}>
                      HP_{invoiceDetail.id}
                    </div>
                  </div>
                )}

                {invoiceDetail.status === "PAID" && (
                  <>
                    <Text><strong>Phương thức:</strong> {invoiceDetail.paymentMethod==="BANK_TRANSFER" ? "Chuyển khoản" : "Tiền mặt"}</Text>
                    <Text><strong>Ngày thanh toán:</strong> {toDisplayDate(invoiceDetail.updatedDate)}</Text>
                    <Text><strong>Người xác nhận:</strong> {invoiceDetail.actionByName || "Hệ thống tự động"}</Text>
                  </>
                )}

                {invoiceDetail.undoReason && (
                  <Text type="danger"><strong>Lý do hủy:</strong> {invoiceDetail.undoReason}</Text>
                )}
              </Space>
            </div>
          </div>
        ) : (
          <p>Không có dữ liệu hóa đơn.</p>
        )}
      </Modal>
    </Card>
  );
};

export default StudentFinancePage;
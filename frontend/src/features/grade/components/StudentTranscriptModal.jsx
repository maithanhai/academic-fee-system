import { useState, useEffect, useMemo } from "react";
import { Modal, Table, Descriptions, Tabs, Spin } from "antd";

const GRADE_CONFIG = [
  { type: "MIENG", label: "Điểm Miệng" },
  { type: "PHUT_15", label: "Điểm 15 Phút" },
  { type: "TIET_1", label: "Điểm 1 Tiết" },
  { type: "HOC_KY", label: "Điểm Học Kỳ" },
];

const StudentTranscriptModal = ({ open, onClose, studentId, fetchData }) => {
  const [transcriptData, setTranscriptData] = useState(null);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    if (!open || typeof fetchData !== "function") {
      Promise.resolve().then(() => setTranscriptData(null));
      return;
    }
    let isMounted = true;
    Promise.resolve().then(() => setLoading(true));
    fetchData()
      .then((res) => {
        if (isMounted) {
          setTranscriptData(res.data?.data || res.data || null);
        }
      })
      .catch(() => {
        if (isMounted) setTranscriptData(null);
      })
      .finally(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, [open, fetchData]);
  const columns = useMemo(
    () => [
      {
        title: "Môn học",
        dataIndex: "subjectName",
        key: "subjectName",
        width: 160,
        fixed: "left",
      },
      ...GRADE_CONFIG.map((config) => ({
        title: config.label,
        key: config.type,
        align: "center",
        render: (_, record) => {
          const group = record.examGroups?.find(
            (g) => g.examType === config.type,
          );
          if (!group || !group.scores || group.scores.length === 0) return "";
          return group.scores.map((s) => s.scoreValue).join(", ");
        },
      })),
    ],
    [],
  );
  const tabItems = useMemo(() => {
    if (!transcriptData?.semesters) return [];

    const sortedSemesters = [...transcriptData.semesters].sort((a, b) => {
      if (a.semesterName === "FIRST_SEMESTER") return -1;
      if (b.semesterName === "FIRST_SEMESTER") return 1;
      return 0;
    });
    return sortedSemesters.map((sem, index) => {
      const tabLabel =
        sem.semesterName === "FIRST_SEMESTER"
          ? "Học kì 1"
          : sem.semesterName === "SECOND_SEMESTER"
            ? "Học kì 2"
            : `Học kì ${index + 1}`;
      const sortedSubjects = [...(sem.subjectScores || [])].sort((a, b) =>
        a.subjectName.localeCompare(b.subjectName, "vi"),
      );
      return {
        key: String(sem.semesterId || index),
        label: tabLabel,
        children: (
          <Table
            columns={columns}
            dataSource={sortedSubjects}
            rowKey={(record) => record.subjectId || Math.random().toString()}
            pagination={false}
            bordered
            scroll={{ x: "max-content" }}
            locale={{ emptyText: "Chưa có dữ liệu điểm" }}
          />
        ),
      };
    });
  }, [transcriptData, columns]);
  return (
    <Modal
      title="Kết quả học tập học sinh"
      open={!!open}
      onCancel={onClose}
      footer={null}
      width={1000}
      destroyOnHidden
    >
      <Spin spinning={loading}>
        <div style={{ marginTop: 16 }}>
          <Descriptions size="small" column={2} style={{ marginBottom: 20 }}>
            <Descriptions.Item label="Mã học sinh">
              <span style={{ fontSize: 16, fontWeight: 500 }}>
                {transcriptData?.studentId || studentId || "---"}
              </span>
            </Descriptions.Item>
            <Descriptions.Item label="Họ và tên">
              <span style={{ fontSize: 16, fontWeight: 500, color: "#1677ff" }}>
                {transcriptData?.fullName ||
                  transcriptData?.studentName ||
                  "Đang cập nhật..."}
              </span>
            </Descriptions.Item>
          </Descriptions>
          <Tabs type="card" items={tabItems} />
        </div>
      </Spin>
    </Modal>
  );
};
export default StudentTranscriptModal;

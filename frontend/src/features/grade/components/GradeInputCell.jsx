import { useState, useEffect, useRef, useCallback } from "react";
import { InputNumber, Tooltip, message, Spin } from "antd";
import { UndoOutlined } from "@ant-design/icons";
import gradeApi from "../api/gradeApi";

const GradeInputCell = ({
  initialScore,
  classId,
  subjectId,
  semesterId,
  studentId,
  examType,
  ordinalNumber,
  isLocked,
}) => {
  const [value, setValue] = useState(initialScore);
  const [status, setStatus] = useState("IDLE");
  const timerRef = useRef(null);
  const originalValueRef = useRef(initialScore);

  useEffect(() => {
    return () => {
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, []);

  const saveScore = useCallback(
    async (scoreToSave) => {
      if (timerRef.current) clearTimeout(timerRef.current);

      if (scoreToSave === originalValueRef.current) {
        setStatus("IDLE");
        return;
      }

      setStatus("SAVING");
      try {
        await gradeApi.autoSave(classId, subjectId, {
          classId,
          subjectId,
          semesterId,
          studentId,
          examType,
          ordinalNumber,
          score: scoreToSave,
        });

        setStatus("IDLE");
        originalValueRef.current = scoreToSave;
      } catch (error) {
        console.error(error);
        setStatus("IDLE");
        message.error("Lỗi lưu điểm!");
        setValue(originalValueRef.current);
      }
    },
    [classId, subjectId, semesterId, studentId, examType, ordinalNumber],
  );

  const handleChange = (newValue) => {
    setValue(newValue);
    if (timerRef.current) clearTimeout(timerRef.current);

    if (newValue === null || newValue === undefined) return;

    if (newValue === originalValueRef.current) {
      setStatus("IDLE");
      return;
    }

    setStatus("PENDING");
    timerRef.current = setTimeout(() => {
      saveScore(newValue);
    }, 5000);
  };

  const handlePressEnter = () => {
    if (status === "PENDING") {
      saveScore(value);
    }
  };

  const handleUndo = () => {
    if (timerRef.current) clearTimeout(timerRef.current);
    setValue(originalValueRef.current);
    setStatus("IDLE");
  };

  return (
    <div style={{ position: "relative", display: "inline-block" }}>
      <Tooltip
        title={
          status === "PENDING"
            ? "Đang chờ 5s để lưu. Bấm Enter để lưu ngay hoặc xoay để hoàn tác"
            : ""
        }
      >
        <InputNumber
          value={value}
          onChange={handleChange}
          onPressEnter={handlePressEnter}
          min={0}
          max={10}
          step={0.1}
          size="small"
          controls={false}
          status={status === "PENDING" ? "warning" : ""}
          style={{
            width: 55,
            textAlign: "center",
            paddingRight: status === "PENDING" ? 15 : 0,
          }}
          disabled={status === "SAVING" || isLocked}
        />
      </Tooltip>
      {status === "PENDING" && (
        <UndoOutlined
          onClick={handleUndo}
          style={{
            position: "absolute",
            right: 6,
            top: 5,
            fontSize: 12,
            color: "#faad14",
            cursor: "pointer",
            background: "#fff",
          }}
        />
      )}
      {status === "SAVING" && (
        <Spin size="small" style={{ position: "absolute", right: 6, top: 4 }} />
      )}
    </div>
  );
};

export default GradeInputCell;

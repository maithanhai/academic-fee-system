import { useEffect, useMemo, useRef, useState } from "react";
import { Form, message } from "antd";
import { useNavigate, useParams } from "react-router-dom";
import notificationApi from "../api/notificationApi";

const useNotificationDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const editorRef = useRef(null);
  const isCreateMode = id === "create";
  const [loading, setLoading] = useState(!isCreateMode);
  const [saving, setSaving] = useState(false);
  const [initialData, setInitialData] = useState(null);
  const [contentValue, setContentValue] = useState("");

  useEffect(() => {
    if (isCreateMode) return undefined;
    notificationApi
      .getNotificationById(id)
      .then((response) => {
        const data = response.data.data;
        setInitialData(data);
        form.setFieldsValue(data);
        setContentValue(data.content || "");
      })
      .catch(() => {
        message.error("Không lấy được thông tin thông báo!");
        navigate("/admin/notifications");
      })
      .finally(() => setLoading(false));
    return undefined;
  }, [id, isCreateMode, navigate, form]);

  const submit = async (values) => {
    setSaving(true);
    try {
      const payload = {
        ...values,
        content: values.content === "<p><br></p>" ? "" : values.content,
      };
      if (isCreateMode) {
        await notificationApi.createNotification(payload);
        message.success("Tạo thông báo thành công!");
        navigate("/admin/notifications");
      } else {
        await notificationApi.updateNotification(id, payload);
        message.success("Cập nhật thông báo thành công!");
      }
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi lưu thông báo!");
    } finally {
      setSaving(false);
    }
  };

  const config = useMemo(
    () => ({
      readonly: false,
      placeholder: "Bắt đầu soạn thảo nội dung...",
      height: 400,
      toolbarButtonSize: "small",
      showPlaceholder: false,
      buttons: [
        "bold",
        "italic",
        "underline",
        "strikethrough",
        "|",
        "ul",
        "ol",
        "|",
        "font",
        "fontsize",
        "brush",
        "paragraph",
        "|",
        "image",
        "table",
        "link",
        "|",
        "align",
        "undo",
        "redo",
        "eraser",
      ],
    }),
    [],
  );
  return {
    form,
    id,
    navigate,
    isCreateMode,
    loading,
    saving,
    editorRef,
    contentValue,
    setContentValue,
    submit,
    config,
    initialData,
  };
};

export default useNotificationDetail;

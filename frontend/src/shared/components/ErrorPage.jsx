import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const ErrorPage = ({ status }) => {
  const navigate = useNavigate();

  const errorConfig = {
    403: {
      title: '403',
      subTitle: 'Bạn không có quyền truy cập vào trang này.',
    },
    404: {
      title: '404',
      subTitle: 'Trang bạn tìm kiếm không tồn tại.',
    },
    500: {
      title: '500',
      subTitle: 'Hệ thống gặp sự cố. Vui lòng thử lại sau!',
    }
  };

  const currentError = errorConfig[status] || errorConfig[404];

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f5f5f5' }}>
      <Result
        status={status}
        title={currentError.title}
        subTitle={currentError.subTitle}
        extra={
          <Button type="primary" onClick={() => navigate(-1)}>
            Quay lại trang trước
          </Button>
        }
      />
    </div>
  );
};

export default ErrorPage;
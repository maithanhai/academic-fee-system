import axios from "axios";

const axiosClient = axios.create({
  baseURL: "/api",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

const refreshClient = axios.create({
  baseURL: "/api",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

let refreshPromise = null;

const clearLocalAuth = () => {
  ["userId", "username", "fullname", "role"].forEach((key) =>
    localStorage.removeItem(key),
  );
};

const refreshAccessToken = () => {
  if (!refreshPromise) {
    refreshPromise = refreshClient.post("/auth/refresh").finally(() => {
      refreshPromise = null;
    });
  }
  return refreshPromise;
};

axiosClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const requestUrl = originalRequest?.url || "";
    const isAuthRequest =
      requestUrl.includes("/auth/login") ||
      requestUrl.includes("/auth/refresh") ||
      requestUrl.includes("/auth/logout");

    if (
      error.response?.status === 401 &&
      originalRequest &&
      !originalRequest._retry &&
      !isAuthRequest
    ) {
      originalRequest._retry = true;

      try {
        await refreshAccessToken();
        return axiosClient(originalRequest);
      } catch (refreshError) {
        clearLocalAuth();
        window.location.href = "/login";
        return Promise.reject(refreshError);
      }
    }

    if (error.response?.status === 401 && !isAuthRequest) {
      clearLocalAuth();
      window.location.href = "/login";
    }
    return Promise.reject(error);
  },
);

export default axiosClient;

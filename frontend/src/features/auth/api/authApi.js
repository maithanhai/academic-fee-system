import axiosClient from "../../../shared/api/axiosClient"

const authApi = {
    login: (credentials) => {
        return axiosClient.post("/auth/login", credentials)
    },
    refresh: () => {
        return axiosClient.post("/auth/refresh")
    },
    logout: () => {
        return axiosClient.post("/auth/logout")
    }
}
export default authApi
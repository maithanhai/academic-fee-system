import axiosClient from "../../../shared/api/axiosClient"

const authApi = {
    login: (credentials) => {
        return axiosClient.post("/auth/login", credentials)
    },
    logout: () => {
        return axiosClient.post("/auth/logout")
    }
}
export default authApi
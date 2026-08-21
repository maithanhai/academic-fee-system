import axiosClient from "./axiosClient";

const userApi = {
  password: (data) => {
    return axiosClient.put("users/me/password",data);
  },
  profile:()=>{
    return axiosClient.get("users/me")
  },
  updateProfile: (data)=>{
    return axiosClient.put("users/me/profile",data)
  },
};
export default userApi;

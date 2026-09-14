import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  userId: localStorage.getItem("userId") || null,
  username: localStorage.getItem("username") || null,
  fullname: localStorage.getItem("fullname") || null,
  role: localStorage.getItem("role") || null,

  isAuthenticated: !!localStorage.getItem("userId"),
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state, action) => {
      const {
        userId,
        username,
        fullname,
        role,
      } = action.payload;

      state.userId = userId;
      state.username = username;
      state.fullname = fullname;
      state.role = role;
      state.isAuthenticated = true;

      localStorage.setItem("userId", userId);
      localStorage.setItem("username", username);
      localStorage.setItem("fullname", fullname);
      localStorage.setItem("role", role);
    },
    logout: (state) => {
      state.userId = null;
      state.username = null;
      state.fullname = null;
      state.role = null;
      state.isAuthenticated = false;

      localStorage.removeItem("userId");
      localStorage.removeItem("username");
      localStorage.removeItem("fullname");
      localStorage.removeItem("role");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;

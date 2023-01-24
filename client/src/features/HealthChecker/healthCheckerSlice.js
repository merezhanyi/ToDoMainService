import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetchStatus } from './healthCheckerAPI';

const initialState = {
  value: 'unknown',
  status: 'idle',
};

export const checkStatus = createAsyncThunk(
  "healthCheck/fetchStatus",
  async () => {
    const response = await fetchStatus();
    return response.text();
  }
);

export const healthCheckerSlice = createSlice({
  name: "healthChecker",
  initialState,
  extraReducers: (builder) => {
    builder
      .addCase(checkStatus.pending, (state) => {
        state.status = "loading";
        state.value = "checking...";
      })
      .addCase(checkStatus.fulfilled, (state, action) => {
        state.status = "idle";
        state.value = action.payload;
      });
  },
});

export const selectStatus = (state) => state.healthChecker.value;

export default healthCheckerSlice.reducer;

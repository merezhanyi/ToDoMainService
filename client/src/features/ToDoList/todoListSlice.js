import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import {
  fetchList,
  requestAddItem,
  requestDeleteItem,
  requestUpdateItem,
} from "./todoListAPI";

const initialState = {
  value: [],
  current: {},
  isOpen: false,
  pending: false,
};

export const checkList = createAsyncThunk("todoList/fetchList", async () => {
  const response = await fetchList();
  const data = await response.json();

  return data;
});

export const deleteItem = createAsyncThunk(
  "todoList/deleteItem",
  async (id) => {
    const response = await requestDeleteItem(id);
    const data = await response.json();

    return data;
  }
);

export const toggleItem = createAsyncThunk(
  "todoList/toggleItem",
  async (item) => {
    const response = await requestUpdateItem({ ...item, done: !item.done });
    const data = await response.json();

    return data;
  }
);

export const updateItem = createAsyncThunk(
  "todoList/updateItem",
  async (item) => {
    const response = await requestUpdateItem(item);
    const data = await response.json();

    return data;
  }
);

export const addItem = createAsyncThunk("todoList/addItem", async (item) => {
  const response = await requestAddItem(item);
  const data = await response.json();

  return data;
});

function replaceItem(state, item) {
  return state.map((el) => (el.id === item.id ? item : el));
}

export const todoListSlice = createSlice({
  name: "todoList",
  initialState,
  reducers: {
    toggleOpen: (state, action) => {
      const { item, isOpen } = action.payload;
      state.isOpen = isOpen;
      state.current = isOpen ? item : {};
    },
    setCurrent: (state, action) => {
      state.current = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder
      // .addCase(checkList.pending, (state) => {
      //   state.pending = true;
      // }) <- replaced by "addMatcher"
      .addCase(checkList.fulfilled, (state, action) => {
        state.value = action.payload;
      })
      .addCase(deleteItem.fulfilled, (state, action) => {
        state.value = state.value.filter(
          (item) => item.id !== action.payload.id
        );
      })
      .addCase(toggleItem.fulfilled, (state, action) => {
        state.value = replaceItem(state.value, action.payload);
      })
      .addCase(updateItem.fulfilled, (state, action) => {
        state.value = replaceItem(state.value, action.payload);
      })
      .addCase(addItem.fulfilled, (state, action) => {
        state.value.push(action.payload);
      })
      .addMatcher(
        (action) => action.type.endsWith("pending"),
        (state) => {
          state.pending = true;
        }
      )
      .addMatcher(
        (action) => action.type.endsWith("fulfilled"),
        (state) => {
          state.pending = false;
        }
      );
  },
});

export const { toggleOpen, setCurrent } = todoListSlice.actions;
export const selectIsOpen = (state) => state.todoList.isOpen;

export const selectList = (state) => state.todoList.value;
export const selectCurrent = (state) => state.todoList.current;

export const selectIsPending = state => state.todoList.pending;

export default todoListSlice.reducer;

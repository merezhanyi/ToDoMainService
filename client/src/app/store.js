import { configureStore } from '@reduxjs/toolkit';
import counterReducer from "../features/counter/counterSlice";
import healthCheckerReducer from "../features/HealthChecker/healthCheckerSlice";
import todoListReducer from "../features/ToDoList/todoListSlice";

export const store = configureStore({
  reducer: {
    counter: counterReducer,
    healthChecker: healthCheckerReducer,
    todoList: todoListReducer,
  },
});

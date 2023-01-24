import * as React from "react";
import { useSelector } from "react-redux";

import MuiBackdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";

import { selectIsPending } from "../ToDoList/todoListSlice";

export default function Backdrop() {
  const isPending = useSelector(selectIsPending);

  return (
    <MuiBackdrop
      sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
      open={isPending}
    >
      <CircularProgress color="inherit" />
    </MuiBackdrop>
  );
}

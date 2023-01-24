import * as React from "react";
import { Box, Button, Paper, TextField } from "@mui/material";
import { useDispatch } from "react-redux";
import { addItem } from "./todoListSlice";

export default function ToDoItemAdd(props) {
  const [description, setDescription] = React.useState("");
  const dispatch = useDispatch();
  const handleAdd = () => dispatch(addItem(description));
  return (
    <Box component="form" autoComplete="off">
      <Paper elevation={1} sx={{ px: 2, py: 1 }}>
        <TextField
          required
          variant="standard"
          label="Task's description"
          onChange={(e) => setDescription(e.target.value)}
        />
        <Button onClick={handleAdd} variant="contained" sx={{ ml: 2, mt: 1 }}>
          ADD
        </Button>
      </Paper>
    </Box>
  );
}

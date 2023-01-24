import * as React from "react";
import { useDispatch } from "react-redux";

import { IconButton, Paper, Typography } from "@mui/material";
import { Check, Delete, Edit } from "@mui/icons-material";

import { deleteItem, toggleItem, toggleOpen } from "./todoListSlice";

function ToDoList(props) {
  const { list } = props;
  const dispatch = useDispatch();
  const handleOpen = (item) => dispatch(toggleOpen({ item, isOpen: true }));

  const handleDeleteItem = (id) => {
    dispatch(deleteItem(id));
  };

  const handleCheckItem = (item) => {
    dispatch(toggleItem(item));
  };

  return (
    <Paper elevation={1} sx={{ px: 2, py: 1 }}>
      {list.length ? (
        list.map((item) => (
          <Typography key={item.id}>
            {item.id}:{" "}
            <Typography component="span" color={item.done && "primary"}>
              {item.description}
            </Typography>
            <IconButton onClick={() => handleOpen(item)}>
              <Edit color="primary" />
            </IconButton>
            <IconButton>
              <Check
                onClick={() => handleCheckItem(item)}
                color={item.done ? "primary" : "secondary"}
              />
            </IconButton>
            <IconButton>
              <Delete onClick={() => handleDeleteItem(item.id)} />
            </IconButton>
          </Typography>
        ))
      ) : (
        <Typography>The list is empty 👌🏻</Typography>
      )}
    </Paper>
  );
}

export default ToDoList;

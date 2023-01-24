import * as React from "react";
import { useDispatch, useSelector } from "react-redux";

import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import Divider from "@mui/material/Divider";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import Checkbox from "@mui/material/Checkbox";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import TextField from "@mui/material/TextField";

import {
  selectCurrent,
  selectIsOpen,
  setCurrent,
  toggleOpen,
  updateItem,
} from "./todoListSlice";
import { Box } from "@mui/material";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

export default function ToDoItem() {
  const isOpen = useSelector(selectIsOpen);
  const dispatch = useDispatch();
  const handleClose = () => dispatch(toggleOpen({ isOpen: false }));

  const item = useSelector(selectCurrent);
  const handleUpdate = ({ description, done }) => {
    const currentItem = {...item};
    if (typeof description !== "undefined") {
      currentItem.description = description;
    }
    if (typeof done !== "undefined") {
      currentItem.done = done;
    }
    dispatch(setCurrent(currentItem));
  };

  const handleSave = () => {
    handleClose();
    dispatch(
      updateItem(item)
    );
  };

  return (
    <Dialog
      fullScreen
      open={isOpen}
      onClose={handleClose}
      TransitionComponent={Transition}
    >
      <AppBar sx={{ position: "relative" }}>
        <Toolbar>
          <IconButton
            edge="start"
            color="inherit"
            onClick={handleClose}
            aria-label="close"
          >
            <CloseIcon />
          </IconButton>
          <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
            Edit Item
          </Typography>
          <Button autoFocus color="inherit" onClick={handleSave}>
            save
          </Button>
        </Toolbar>
      </AppBar>
      <Box component="form" autoComplete="off" sx={{ px: 2, py: 4 }}>
        <TextField
          required
          id="outlined-required"
          label="Required"
          defaultValue={item.description}
          onChange={(e) => handleUpdate({ description: e.target.value })}
        />
        <Divider sx={{ py: 1 }} />
        <TextField
          required
          id="outlined-required"
          label="Required"
          defaultValue={item.dateTime}
        />
        <Divider sx={{ py: 1 }} />
        <FormGroup>
          <FormControlLabel
            control={
              <Checkbox
                defaultChecked={item.done}
                onChange={(e) => handleUpdate({ done: e.target.checked })}
              />
            }
            label="Is done?"
          />
        </FormGroup>
      </Box>
    </Dialog>
  );
}

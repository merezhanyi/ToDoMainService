import * as React from "react";
import { useDispatch, useSelector } from "react-redux";

import Typography from "@mui/material/Typography";
import { Button } from "@mui/material";

import { checkList, selectList } from "../features/ToDoList/todoListSlice";
import DrawerAppBar from "../features/DrawerAppBar/DrawerAppBar";
import ToDoList from "../features/ToDoList/ToDoList";
import ToDoItem from "../features/ToDoList/ToDoItem";
import ToDoItemAdd from "../features/ToDoList/ToDoItemAdd";
import Backdrop from "../features/Backdrop/Backdrop";

function ToDoListPage(props) {
  const list = useSelector(selectList);
  const dispatch = useDispatch();
  const fetchList = () => dispatch(checkList());

  let result = false;
  React.useEffect(() => {
    // eslint-disable-next-line react-hooks/exhaustive-deps
    result = fetchList();
  }, [result]);

  return (
    <DrawerAppBar>
      <Typography variant="h1">ToDo List! 📝</Typography>
      <ToDoItemAdd />
      <Button onClick={fetchList}>Refresh the list</Button>
      <ToDoList list={list} />
      <ToDoItem />
      <Backdrop />
    </DrawerAppBar>
  );
}

export default ToDoListPage;

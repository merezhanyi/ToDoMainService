import * as React from "react";
import { useRouteError } from "react-router-dom";
import Typography from "@mui/material/Typography";
import DrawerAppBar from "../features/DrawerAppBar/DrawerAppBar";

function ErrorPage(props) {
  const error = useRouteError();
  return (
    <DrawerAppBar>
      <Typography variant="h1">404! Page not Found 🧐</Typography>
      <Typography>{error.statusText || error.message}</Typography>
    </DrawerAppBar>
  );
}

export default ErrorPage;

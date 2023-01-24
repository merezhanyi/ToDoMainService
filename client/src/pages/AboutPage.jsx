import * as React from "react";
import Typography from "@mui/material/Typography";
import DrawerAppBar from "../features/DrawerAppBar/DrawerAppBar";

function AboutPage(props) {
  return (
    <DrawerAppBar>
      <Typography variant="h1">About ☕️</Typography>
    </DrawerAppBar>
  );
}

export default AboutPage;

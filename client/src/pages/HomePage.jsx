import * as React from "react";
import Typography from "@mui/material/Typography";
import DrawerAppBar from "../features/DrawerAppBar/DrawerAppBar";
import HealthChecker from "../features/HealthChecker/HealthChecker";

function HomePage(props) {
  return (
    <DrawerAppBar>
      <Typography variant="h1">Welcome! 👋🏻</Typography>
      <HealthChecker />
    </DrawerAppBar>
  );
}

export default HomePage;

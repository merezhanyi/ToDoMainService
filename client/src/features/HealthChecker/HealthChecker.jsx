import * as React from "react";
import { Box, Button, Typography } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { checkStatus, selectStatus } from "./healthCheckerSlice";

function HealthChecker(props) {
    const status = useSelector(selectStatus);
    const dispatch = useDispatch();
    const fetchStatus = () => dispatch(checkStatus())

    return (
      <Box>
        <Button onClick={fetchStatus}>Test Server</Button>
        <Typography>status: {status}</Typography>
      </Box>
    );
}

export default HealthChecker;
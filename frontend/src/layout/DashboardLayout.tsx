import { AppBar, Box, CssBaseline, Drawer, Toolbar } from "@mui/material";
import React from "react";
import HeaderContent from "../component/HeaderContent";
import Menu from "../component/Menu";

import styles from "./DashboardLayout.module.css";
import { StyledEngineProvider } from "@mui/styled-engine";

function DashboardLayout(props: any) {
    const drawerWidth = 200;
    const appBarHeight = "80px";
    return (
        <StyledEngineProvider injectFirst>
            <CssBaseline />
            <Box sx={{ display: "flex" }}>
                <AppBar
                    className={styles.appBar}
                    position='fixed'
                    color='transparent'
                    sx={{
                        height: appBarHeight,
                        zIndex: (theme) => theme.zIndex.drawer + 1,
                    }}
                >
                    <HeaderContent />
                </AppBar>
                <Drawer
                    PaperProps={{
                        sx: {
                            backgroundColor: "#262626",
                        },
                    }}
                    variant='permanent'
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        [`& .MuiDrawer-paper`]: {
                            width: drawerWidth,
                            boxSizing: "border-box",
                        },
                    }}
                >
                    <Box sx={{ overflow: "auto", height: "100%" }}>
                        <Toolbar sx={{ height: appBarHeight }} />
                        <Menu />
                    </Box>
                </Drawer>
                <Box
                    component='main'
                    sx={{
                        flexGrow: 1,
                        p: 0,
                        overflow: "hidden",
                    }}
                >
                    <Toolbar sx={{ height: appBarHeight }} />
                    {props.children}
                </Box>
            </Box>
        </StyledEngineProvider>
    );
}

export default DashboardLayout;

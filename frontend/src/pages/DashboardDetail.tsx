import { Box, FormControlLabel, Grid, Switch } from "@mui/material";
import React from "react";
import { Transition } from "react-transition-group";
import styles from "./DashboardDetail.module.css";

//import theme from "../commons/theme";

function DashboardDetail() {
    const [checked, setChecked] = React.useState(false);
    const handleChange = () => {
        setChecked((prev) => !prev);
    };

    const duration = 300;

    // const defaultStyle = {
    //     transition: `width ${duration}ms ease-in-out`,
    // };

    // const defaultStyle1 = {
    //     transition: `width ${duration}ms ease-in-out`,
    //     overflow: "hidden",
    // };

    // const transitionStyles: { [key: string]: any } = {
    //     entering: { width: "100%" },
    //     entered: { width: "100%" },
    //     exiting: { width: "80%" },
    //     exited: { width: "80%" },
    // };

    // const transitionStyles1: { [key: string]: any } = {
    //     entering: { width: "0%" },
    //     entered: { width: "0%" },
    //     exiting: { width: "20%" },
    //     exited: { width: "20%" },
    // };

    const transitionMap: { [key: string]: string } = {
        entering: styles.dashboardMapExtend,
        entered: styles.dashboardMapExtend,
        exiting: "",
        exited: "",
    };

    const transitionChart: { [key: string]: string } = {
        entering: styles.dashboardChartHidden,
        entered: styles.dashboardChartHidden,
        exiting: "",
        exited: "",
    };

    return (
        <div>
            <FormControlLabel
                control={<Switch checked={checked} onChange={handleChange} />}
                label='Show'
            />
            <Transition in={checked} timeout={400}>
                {(state: string) => {
                    return (
                        <Box
                            sx={{
                                display: "flex",
                                height: "100%",
                            }}
                        >
                            <Box
                                className={[
                                    styles.dashboardMap,
                                    transitionMap[state],
                                ].join(" ")}
                                // style={{
                                //     ...defaultStyle,
                                //     ...transitionStyles[state],
                                // }}
                                // width={checked === true ? "100%" : "80%"}
                                sx={{
                                    backgroundColor: "#ff0000",
                                }}
                            >
                                test
                            </Box>
                            <Box
                                // display={checked === true ? "none" : "block"}
                                // width={checked === true ? "0%" : "20%"}
                                // style={{
                                //     ...defaultStyle1,
                                //     ...transitionStyles1[state],
                                // }}
                                // className={styles.dashboardChart}
                                className={[
                                    styles.dashboardChart,
                                    transitionChart[state],
                                ].join(" ")}
                                sx={{
                                    backgroundColor: "#0000ff",
                                }}
                            >
                                sample
                            </Box>
                        </Box>
                    );
                }}
            </Transition>

            {/* <Box
                    item
                    xs={checked === true ? 12 : 8}
                    width
                    style={{
                        transition: theme.transitions.create("width", {
                            easing: theme.transitions.easing.sharp,
                            duration: theme.transitions.duration.leavingScreen,
                        }),
                        backgroundColor: "#ff0000",
                    }}
                >
                    TEST
                </Box>
                <Grid
                    item
                    xs={checked === true ? 0 : 4}
                    style={{
                        transition: theme.transitions.create("width", {
                            easing: theme.transitions.easing.sharp,
                            duration: theme.transitions.duration.leavingScreen,
                        }),
                        backgroundColor: "#0000ff",
                    }}
                >
                    TEST2
                </Grid>
            </Grid> */}
        </div>
    );
}

export default DashboardDetail;

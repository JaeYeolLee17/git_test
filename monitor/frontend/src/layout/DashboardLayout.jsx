import React, { useState } from "react";
import {
    AppShell,
    Burger,
    Drawer,
    Header,
    MediaQuery,
    Navbar,
    Text,
    useMantineTheme,
} from "@mantine/core";
import Menu from "../component/Menu";
import HeaderContent from "../component/HeaderContent";

import styles from "./DashboardLayout.module.css";

function DashboardLayout({ children }) {
    const [opened, setOpened] = useState(false);
    const theme = useMantineTheme();

    return (
        <AppShell
            navbarOffsetBreakpoint='sm'
            fixed
            padding={0}
            navbar={
                <Navbar
                    hidden={true}
                    hiddenBreakpoint='sm'
                    width={{ sm: 200 }}
                    p='md'
                    className={styles.navbar}
                >
                    <Drawer
                        className={styles.drawerNavbar}
                        opened={opened}
                        onClose={() => setOpened(false)}
                        padding='sm'
                        size={200}
                    >
                        <Menu />
                    </Drawer>
                    <MediaQuery smallerThan='sm' styles={{ display: "none" }}>
                        <Menu />
                    </MediaQuery>
                </Navbar>
            }
            header={
                <Header height={80} px='sm'>
                    <div
                        style={{
                            display: "flex",
                            alignItems: "center",
                            height: "100%",
                        }}
                    >
                        <MediaQuery
                            largerThan='sm'
                            styles={{ display: "none" }}
                        >
                            <Burger
                                opened={opened}
                                onClick={() => setOpened((prev) => !prev)}
                                size='sm'
                                color={theme.colors.gray[6]}
                            />
                        </MediaQuery>

                        <HeaderContent />
                    </div>
                </Header>
            }
        >
            {children}
        </AppShell>
    );
}

export default DashboardLayout;

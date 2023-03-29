import React from "react";
import { Link, NavLink } from "react-router-dom";

import * as Common from "../commons/common";

import WeatherWidget from "../component/WeatherWidget";

import styles from "./Menu.module.css";

// import { Plus } from "tabler-icons-react";

import { ReactComponent as Menu01 } from "../assets/images/ico_menu_01_d.svg";
import { ReactComponent as Menu02 } from "../assets/images/ico_menu_02_d.svg";
import { ReactComponent as Menu03 } from "../assets/images/ico_menu_03_d.svg";
import { ReactComponent as Menu04 } from "../assets/images/ico_menu_04_d.svg";
import { ReactComponent as Menu05 } from "../assets/images/ico_menu_05_d.svg";
import { ReactComponent as Menu06 } from "../assets/images/ico_menu_06_d.svg";
import { ReactComponent as Menu07 } from "../assets/images/ico_menu_07_d.svg";
import { ReactComponent as Menu08 } from "../assets/images/ico_menu_08_d.svg";
import { ReactComponent as Menu09 } from "../assets/images/ico_menu_09_d.svg";
import { ReactComponent as Menu10 } from "../assets/images/ico_menu_10_d.svg";

import expandMinus from "../assets/images/ico_minus_w.png";

import { Accordion, AccordionDetails, AccordionSummary } from "@mui/material";
import { useAuthState } from "../provider/AuthProvider";

type menuType = {
    title: string;
    items: {
        title: string;
        link: string;
        icon: string;
        role: string[];
    }[];
};

const menuData: menuType[] = [
    {
        title: "대시보드",
        items: [
            {
                title: "교통현황",
                link: `${Common.PAGE_DASHBOARD}`,
                icon: "Menu01",
                role: [Common.Authority.ROLE_ADMIN, Common.Authority.ROLE_MANAGER, Common.Authority.ROLE_USER],
            },
            {
                title: "교통현황 데이터",
                link: `${Common.PAGE_DASHBOARD_DETAIL}`,
                icon: "Menu02",
                role: [Common.Authority.ROLE_ADMIN, Common.Authority.ROLE_MANAGER, Common.Authority.ROLE_USER],
            },
        ],
    },
    {
        title: "통계",
        items: [
            {
                title: "교통통계",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu03",
                role: [Common.Authority.ROLE_ADMIN, Common.Authority.ROLE_MANAGER, Common.Authority.ROLE_USER],
            },
            {
                title: "교통통계 데이터",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu04",
                role: [Common.Authority.ROLE_ADMIN, Common.Authority.ROLE_MANAGER, Common.Authority.ROLE_USER],
            },
            {
                title: "긴급차량 통계",
                link: `${Common.PAGE_STAT_EMERGENCY}`,
                icon: "Menu05",
                role: [Common.Authority.ROLE_ADMIN, Common.Authority.ROLE_MANAGER, Common.Authority.ROLE_USER],
            },
        ],
    },
    {
        title: "관리",
        items: [
            {
                title: "카메라 관리",
                link: `${Common.PAGE_MANAGEMENT_CAMERA}`,
                icon: "Menu06",
                role: [Common.Authority.ROLE_ADMIN],
            },
            {
                title: "교차로 관리",
                link: `${Common.PAGE_MANAGEMENT_INTERSECTION}`,
                icon: "Menu07",
                role: [Common.Authority.ROLE_ADMIN],
            },
            {
                title: "링크 관리",
                link: `${Common.PAGE_MANAGEMENT_LINK}`,
                icon: "Menu08",
                role: [Common.Authority.ROLE_ADMIN],
            },
            {
                title: "구역 관리",
                link: `${Common.PAGE_MANAGEMENT_REGION}`,
                icon: "Menu08",
                role: [Common.Authority.ROLE_ADMIN],
            },
            {
                title: "긴급차량 관리",
                link: `${Common.PAGE_MANAGEMENT_EMERGENCY}`,
                icon: "Menu09",
                role: [Common.Authority.ROLE_ADMIN],
            },
            {
                title: "사용자 관리",
                link: `${Common.PAGE_MANAGEMENT_USER}`,
                icon: "Menu10",
                role: [Common.Authority.ROLE_ADMIN],
            },
        ],
    },
];

const Components: { [key: string]: any } = {
    Menu01: Menu01,
    Menu02: Menu02,
    Menu03: Menu03,
    Menu04: Menu04,
    Menu05: Menu05,
    Menu06: Menu06,
    Menu07: Menu07,
    Menu08: Menu08,
    Menu09: Menu09,
    Menu10: Menu10,
};

const Menu = () => {
    // pass string array of menu items to be rendered into
    // components
    const userDetails = useAuthState();

    const getImage = (icon: string) => {
        const ComponentName = Components[icon];
        return (
            <span className={styles.iconContainer}>
                <ComponentName />
            </span>
        );
    };
    const menuElements = menuData.map((group) => {
        const itemElements = group.items.map((item) => {
            const checkRole = userDetails?.user && item.role.includes(userDetails?.user.authority);

            if (checkRole === false) return null;

            return (
                <AccordionDetails key={item.title} className={styles.menuListItem} sx={{ marginBottom: "8px" }}>
                    <NavLink
                        style={{ textDecoration: "none" }}
                        to={item.link}
                        className={({ isActive }) => {
                            const names = isActive ? `${styles.menuLinkItem} ${styles.menuLinkItemActive}` : `${styles.menuLinkItem}`;
                            return names;
                        }}
                    >
                        {({ isActive }) => {
                            const textClass = isActive ? `${styles.menuText} ${styles.menuTextActive}` : `${styles.menuText}`;

                            return (
                                <span className={styles.menuListDiv}>
                                    {getImage(item.icon)}
                                    <span className={textClass}>{item.title}</span>
                                </span>
                            );
                        }}
                    </NavLink>
                </AccordionDetails>
            );
        });

        if (itemElements.every((element) => element === null)) return null;

        return (
            <Accordion key={group.title} defaultExpanded={true} disableGutters={true} sx={{ backgroundColor: "#262626", boxShadow: "none" }}>
                <AccordionSummary
                    expandIcon={<img src={expandMinus} />}
                    sx={{
                        borderTop: "1px solid #4d4d4d",
                        borderBottom: "none",
                        marginBottom: "8px",
                        backgroundColor: "#404040",
                        height: "44px",
                        color: "#bfbfbf",
                        fontSize: "14px",
                    }}
                >
                    {/* <ul>{itemElements}</ul> */}
                    {group.title}
                </AccordionSummary>
                {itemElements}
            </Accordion>
        );
    });

    return (
        <aside className={styles.wrapper}>
            <>
                <WeatherWidget />
                {menuElements}
            </>
        </aside>
        // <aside>
        //     {/* <WeatherWidget /> */}
        //     <ul>
        //         <li>
        //             <Link to={Common.PAGE_DASHBOARD}>Dashboard</Link>
        //         </li>
        //         <li>
        //             <Link to={Common.PAGE_DASHBOARD_DETAIL}>
        //                 Dashboard Detail
        //             </Link>
        //         </li>
        //         <li>
        //             <Link to={Common.PAGE_STAT_TRAFFIC}>Stat Traffic</Link>
        //         </li>
        //     </ul>
        // </aside>
    );
};

export default Menu;

import React from "react";
import { Link, NavLink } from "react-router-dom";

import * as Common from "../commons/common";

// import WeatherWidget from "../component/WeatherWidget";

// import styles from "./Menu.module.css";

// import { Plus } from "tabler-icons-react";

// import { ReactComponent as Menu01 } from "../assets/images/navbar/ico_menu_01_d.svg";
// import { ReactComponent as Menu02 } from "../assets/images/navbar/ico_menu_02_d.svg";
// import { ReactComponent as Menu03 } from "../assets/images/navbar/ico_menu_03_d.svg";
// import { ReactComponent as Menu04 } from "../assets/images/navbar/ico_menu_04_d.svg";
// import { ReactComponent as Menu05 } from "../assets/images/navbar/ico_menu_05_d.svg";
// import { ReactComponent as Menu06 } from "../assets/images/navbar/ico_menu_06_d.svg";
// import { ReactComponent as Menu07 } from "../assets/images/navbar/ico_menu_07_d.svg";
// import { ReactComponent as Menu08 } from "../assets/images/navbar/ico_menu_08_d.svg";
// import { ReactComponent as Menu09 } from "../assets/images/navbar/ico_menu_09_d.svg";
// import { ReactComponent as Menu10 } from "../assets/images/navbar/ico_menu_10_d.svg";

const menuData = [
    {
        title: "대시보드",
        items: [
            {
                title: "교통현황",
                link: `${Common.PAGE_DASHBOARD}`,
                icon: "Menu01",
            },
            {
                title: "교통현황 데이터",
                link: `${Common.PAGE_DASHBOARD_DETAIL}`,
                icon: "Menu02",
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
            },
            {
                title: "교통통계 데이터",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu04",
            },
            {
                title: "긴급차량 통계",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu05",
            },
        ],
    },
    {
        title: "관리",
        items: [
            {
                title: "카메라 관리",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu06",
            },
            {
                title: "교차로 관리",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu07",
            },
            {
                title: "구역 관리",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu08",
            },
            {
                title: "긴급차량 관리",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu09",
            },
            {
                title: "사용자 관리",
                link: `${Common.PAGE_STAT_TRAFFIC}`,
                icon: "Menu10",
            },
        ],
    },
];

// const Components = {
//     Menu01,
//     Menu02,
//     Menu03,
//     Menu04,
//     Menu05,
//     Menu06,
//     Menu07,
//     Menu08,
//     Menu09,
//     Menu10,
// };

const Menu = () => {
    // pass string array of menu items to be rendered into
    // components
    // const getImage = (icon) => {
    //     const ComponentName = Components[icon];
    //     return (
    //         <span className={styles.iconContainer}>
    //             <ComponentName />
    //         </span>
    //     );
    // };
    // const menuElements = menuData.map((group) => {
    //     const itemElements = group.items.map((item) => {
    //         return (
    //             <li key={item.title} className={styles.menuListItem}>
    //                 <NavLink
    //                     to={item.link}
    //                     className={({ isActive }) => {
    //                         const names = isActive
    //                             ? `${styles.menuLinkItem} ${styles.menuLinkItemActive}`
    //                             : `${styles.menuLinkItem}`;
    //                         return names;
    //                     }}
    //                 >
    //                     {({ isActive }) => {
    //                         //console.log(isActive);
    //                         const textClass = isActive
    //                             ? `${styles.menuText} ${styles.menuTextActive}`
    //                             : `${styles.menuText}`;

    //                         return (
    //                             <span className={styles.menuListDiv}>
    //                                 {getImage(item.icon)}
    //                                 <span className={textClass}>
    //                                     {item.title}
    //                                 </span>
    //                             </span>
    //                         );
    //                     }}
    //                 </NavLink>
    //             </li>
    //         );
    //     });

    //     return (
    //         <Accordion.Item key={group.title} label={group.title}>
    //             <ul>{itemElements}</ul>
    //         </Accordion.Item>
    //     );
    // });

    return (
        // <aside className={styles.wrapper}>
        //     <Accordion
        //         multiple
        //         iconPosition='right'
        //         icon={<Plus size={16} color='white' />}
        //         initialState={{ 0: true, 1: true, 2: true }}
        //         classNames={{
        //             item: `${styles.accordionItem}`,
        //             control: `${styles.accordionItemControl}`,
        //             label: `${styles.accordionItemLabel}`,
        //             contentInner: `${styles.accordionConnentInner}`,
        //         }}
        //     >
        //         {menuElements}
        //     </Accordion>
        // </aside>
        <aside>
            {/* <WeatherWidget /> */}
            <ul>
                <li>
                    <Link to={Common.PAGE_DASHBOARD}>Dashboard</Link>
                </li>
                <li>
                    <Link to={Common.PAGE_DASHBOARD_DETAIL}>
                        Dashboard Detail
                    </Link>
                </li>
                <li>
                    <Link to={Common.PAGE_STAT_TRAFFIC}>Stat Traffic</Link>
                </li>
            </ul>
        </aside>
    );
};

export default Menu;

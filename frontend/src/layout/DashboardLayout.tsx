import React from "react";
import HeaderContent from "../component/HeaderContent";
import Menu from "../component/Menu";

function DashboardLayout(props: any) {
    return (
        <div>
            <HeaderContent />
            <Menu />
            {props.children}
        </div>
    );
}

export default DashboardLayout;

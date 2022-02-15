import React from "react";
import { useNavigate } from "react-router-dom";

const Header = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("currentUser");
        navigate("/login");
    };

    return (
        <div style={{ color: "red", border: "solid 1px", background: "blue" }}>
            <h1>Header</h1>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
};

export default Header;

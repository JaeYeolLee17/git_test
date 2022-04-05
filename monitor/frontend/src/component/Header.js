import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuthDispatch } from "../provider/AuthProvider";

const Header = () => {
    const navigate = useNavigate();
    const dispatch = useAuthDispatch();

    const handleLogout = () => {
        //localStorage.removeItem("currentUser");
        dispatch({ type: "LOGOUT" });
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

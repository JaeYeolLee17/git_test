import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter as Router } from "react-router-dom";
import { AuthProvider } from "./provider/AuthProvider";

import { transitions, positions, Provider as AlertProvider } from "react-alert";
import AlertTemplate from "react-alert-template-basic"; // TODO: change custom alert popup

const options = {
    // you can also just use 'bottom center'
    position: positions.TOP_CENTER,
    timeout: 5000,
    // you can also just use 'scale'
    transition: transitions.SCALE,
};

ReactDOM.render(
    <AuthProvider>
        <AlertProvider template={AlertTemplate} {...options}>
            <Router>
                <App />
            </Router>
        </AlertProvider>
    </AuthProvider>,
    document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

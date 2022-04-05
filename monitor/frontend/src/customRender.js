import React from "react";
import { render } from "@testing-library/react";
//import { MemoryRouter } from "react-router-dom";
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
const Wrapper = ({ children }) => {
    return (
        <AuthProvider>
            <AlertProvider template={AlertTemplate} {...options}>
                {children}
            </AlertProvider>
        </AuthProvider>
    );
};

const customRender = (ui, options) =>
    render(ui, { wrapper: Wrapper, ...options });

// re-export everything
export * from "@testing-library/react";

// override render method
export { customRender as render };

import { render, screen } from "./customRender";
import { MemoryRouter, Router, useLocation } from "react-router-dom";
import App from "./App";
import { createMemoryHistory } from "history";
import * as Common from "./commons/common";
import { AuthProvider } from "./provider/AuthProvider";
import { transitions, positions, Provider as AlertProvider } from "react-alert";
import AlertTemplate from "react-alert-template-basic"; // TODO: change custom alert popup
import userEvent from "@testing-library/user-event";

test("redirect", () => {
    const history = createMemoryHistory();
    history.push("/");
    render(
        <Router location={history.location} navigator={history}>
            <App />
        </Router>
    );

    expect(history.location.pathname).toBe(Common.PAGE_LOGIN);
});

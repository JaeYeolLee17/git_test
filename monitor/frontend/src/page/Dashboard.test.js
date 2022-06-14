import { render, screen } from "../customRender";
import * as Common from "../commons/common";
import { createMemoryHistory } from "history";
import { Router } from "react-router-dom";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import Dashboard from "./Dashboard";
import DashboardMap from "../component/DashboardMap";
import { useAuthState } from "../provider/AuthProvider";

describe("dashboard page", () => {
    // test("sample", () => {
    //     const history = createMemoryHistory();
    //     history.push(Common.PAGE_LOGIN);
    //     render(
    //         <Router location={history.location} navigator={history}>
    //             <DashboardMap />
    //         </Router>
    //     );
    //     //screen.debug();
    // });
});

describe("dashboardd page", () => {
    test("sample2", () => {
        const Sample = () => {
            const userDetail = useAuthState();
            return <div>Sample</div>;
        };
        const history = createMemoryHistory();
        history.push(Common.PAGE_LOGIN);
        render(<Sample />);

        //screen.debug();
    });
});

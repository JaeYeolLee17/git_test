import "./App.css";

import { Routes, Route, Navigate } from "react-router-dom";
import { useAuthState } from "./provider/AuthProvider";

import Dashboard from "./page/Dashboard";
import DashboardDetail from "./page/DashboardDetail";
import Login from "./page/Login";

import NotFound from "./page/NotFound";

import * as Common from "./commons/common";
import StatTraffic from "./page/StatTraffic";

import DashboardTest from './page/DashboardTest'

const PrivateRoute = ({ children }) => {
    const userDetails = useAuthState();
    return userDetails.user ? children : <NotFound />;
};

const PrivateAdminRoute = ({ children }) => {
    const userDetails = useAuthState();
    const checkRole =
        userDetails.user && userDetails.user.authority === "ROLE_ADMIN";

    //console.log("userDetails", userDetails);
    return checkRole ? children : <NotFound />;
};

function App() {
    return (
        <div>
            <Routes>
                <Route path='*' element={<NotFound />} />
                <Route path='/' element={<Navigate to={Common.PAGE_LOGIN} />} />

                <Route path={Common.PAGE_LOGIN} element={<Login />}></Route>
                <Route
                    path={Common.PAGE_DASHBOARD}
                    element={
                        <PrivateRoute>
                            <Dashboard />
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_DASHBOARD_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardDetail />
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_STAT_TRAFFIC}
                    element={
                        <PrivateRoute>
                            <StatTraffic />
                        </PrivateRoute>
                    }
                ></Route>
                <Route 
                    path={"/dashboard/test"}
                    element={
                        <PrivateRoute>
                            <DashboardTest />
                        </PrivateRoute>
                    }
                />
            </Routes>
        </div>
    );
}

export default App;

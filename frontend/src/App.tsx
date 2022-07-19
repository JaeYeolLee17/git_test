import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import * as Common from "./commons/common";

import "./App.css";

import { useAuthState } from "./provider/AuthProvider";
import NotFound from "./pages/NotFound";
import Dashboard from "./pages/Dashboard";
import DashboardDetail from "./pages/DashboardDetail";
import StatTraffic from "./pages/StatTraffic";
import Login from "./pages/Login";
import DashboardLayout from "./layout/DashboardLayout";
import ManagementCamera from "./pages/ManagementCamera";
import CameraDetail from "./pages/CameraDetail";

// declare global {
//     interface Window {
//         kakao: any;
//     }
// }

// type Props = {
//   children: React.ReactElement<any>;
//   // any props that come into the component
// }

const PrivateRoute = (props: any) => {
    const userDetails = useAuthState();
    return userDetails?.user ? props.children : <NotFound />;
};

const PrivateAdminRoute = (props: any) => {
    const userDetails = useAuthState();
    const checkRole =
        userDetails?.user && userDetails?.user.authority === "ROLE_ADMIN";

    return checkRole ? props.children : <NotFound />;
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
                            <DashboardLayout>
                                <Dashboard />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_DASHBOARD_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <DashboardDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_CAMERA}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementCamera />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_CAMERA_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <CameraDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_STAT_TRAFFIC}
                    element={
                        <PrivateRoute>
                            <DashboardLayout>
                                <StatTraffic />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
            </Routes>
        </div>
    );
}

export default App;

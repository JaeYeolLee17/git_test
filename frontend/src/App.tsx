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
import ManagementIntersection from "./pages/ManagementIntersection";
import ManagementRegion from "./pages/ManagementRegion";
import ManagementEmergency from "./pages/ManagementEmergency";
import ManagementUser from "./pages/ManagementUser";
import CameraDetail from "./pages/CameraDetail";
import IntersectionDetail from "./pages/IntersectionDetail";
import RegionDetail from "./pages/RegionDetail";
import EmergencyDetail from "./pages/EmergencyDetail";
import UserDetail from "./pages/UserDetail";
import StatEmergency from "./pages/StatEmergency";
import ManagementLink from "./pages/ManagementLink";
import LinkDetail from "./pages/LinkDetail";

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

    if (props.role !== undefined && props.role.length > 0) {
        const checkRole =
            userDetails?.user &&
            props.role.includes(userDetails?.user.authority);
        return checkRole ? props.children : <NotFound />;
    }

    return userDetails?.user ? props.children : <NotFound />;
};

// const PrivateAdminRoute = (props: any) => {
//     const userDetails = useAuthState();
//     const checkRole =
//         userDetails?.user && userDetails?.user.authority === "ROLE_ADMIN";

//     return checkRole ? props.children : <NotFound />;
// };

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
                        <PrivateRoute
                            role={[
                                Common.Authority.ROLE_ADMIN,
                                Common.Authority.ROLE_MANAGER,
                                Common.Authority.ROLE_USER,
                            ]}
                        >
                            <DashboardLayout>
                                <Dashboard />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_DASHBOARD_DETAIL}
                    element={
                        <PrivateRoute
                            role={[
                                Common.Authority.ROLE_ADMIN,
                                Common.Authority.ROLE_MANAGER,
                                Common.Authority.ROLE_USER,
                            ]}
                        >
                            <DashboardLayout>
                                <DashboardDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_STAT_TRAFFIC}
                    element={
                        <PrivateRoute
                            role={[
                                Common.Authority.ROLE_ADMIN,
                                Common.Authority.ROLE_MANAGER,
                                Common.Authority.ROLE_USER,
                            ]}
                        >
                            <DashboardLayout>
                                <StatTraffic />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_STAT_EMERGENCY}
                    element={
                        <PrivateRoute
                            role={[
                                Common.Authority.ROLE_ADMIN,
                                Common.Authority.ROLE_MANAGER,
                                Common.Authority.ROLE_USER,
                            ]}
                        >
                            <DashboardLayout>
                                <StatEmergency />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_CAMERA}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementCamera />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_CAMERA_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <CameraDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_INTERSECTION}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementIntersection />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_INTERSECTION_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <IntersectionDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_LINK}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementLink />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_LINK_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <LinkDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementRegion />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <RegionDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementRegion />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <RegionDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_EMERGENCY}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementEmergency />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_EMERGENCY_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <EmergencyDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_USER}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <ManagementUser />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_USER_DETAIL}
                    element={
                        <PrivateRoute role={[Common.Authority.ROLE_ADMIN]}>
                            <DashboardLayout>
                                <UserDetail />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
            </Routes>
        </div>
    );
}

export default App;

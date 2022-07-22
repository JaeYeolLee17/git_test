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
                    path={Common.PAGE_MANAGEMENT_INTERSECTION}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementIntersection />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_INTERSECTION_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <IntersectionDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementRegion />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <RegionDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementRegion />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_REGION_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <RegionDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_EMERGENCY}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementEmergency />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_EMERGENCY_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <EmergencyDetail />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_USER}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <ManagementUser />
                            </DashboardLayout>
                        </PrivateAdminRoute>
                    }
                ></Route>
                <Route
                    path={Common.PAGE_MANAGEMENT_USER_DETAIL}
                    element={
                        <PrivateAdminRoute>
                            <DashboardLayout>
                                <UserDetail />
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
                <Route
                    path={Common.PAGE_STAT_EMERGENCY}
                    element={
                        <PrivateRoute>
                            <DashboardLayout>
                                <StatEmergency />
                            </DashboardLayout>
                        </PrivateRoute>
                    }
                ></Route>
            </Routes>
        </div>
    );
}

export default App;

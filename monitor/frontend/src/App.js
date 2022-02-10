import "./App.css";

import { Routes, Route, Navigate } from "react-router-dom";
import { useAuthState } from "./provider/AuthProvider";

import Dashboard from "./page/Dashboard";
import DashboardDetail from "./page/DashboardDetail";
import Login from "./page/Login";

import NotFound from "./page/NotFound";

const PrivateRoute = ({ children }) => {
    const userDetails = useAuthState();
    return userDetails.user ? children : <NotFound />;
};

const PrivateAdminRoute = ({ children }) => {
    const userDetails = useAuthState();
    const checkRole =
        userDetails.user && userDetails.user.authority === "ROLE_ADMIN";

    console.log("userDetails", userDetails);
    return checkRole ? children : <NotFound />;
};

function App() {
    return (
        <div>
            <Routes>
                <Route path='*' element={<NotFound />} />
                <Route path='/' element={<Navigate to='/login' />} />

                <Route path='/login' element={<Login />}></Route>
                <Route
                    path='/dashboard'
                    element={
                        <PrivateRoute>
                            <Dashboard />
                        </PrivateRoute>
                    }
                ></Route>
                <Route
                    path='/dashboard/detail/all'
                    element={
                        <PrivateAdminRoute>
                            <DashboardDetail />
                        </PrivateAdminRoute>
                    }
                ></Route>
            </Routes>
        </div>
    );
}

export default App;

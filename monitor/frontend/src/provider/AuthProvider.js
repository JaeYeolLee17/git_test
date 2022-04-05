import React, { useContext, useReducer } from "react";

const AuthStateContext = React.createContext();
const AuthDispatchContext = React.createContext();

export function useAuthState() {
    const context = useContext(AuthStateContext);
    if (context === undefined) {
        throw new Error("useAuthState must be used within a AuthProvider");
    }

    return context;
}

export function useAuthDispatch() {
    const context = useContext(AuthDispatchContext);
    if (context === undefined) {
        throw new Error("useAuthDispatch must be used within a AuthProvider");
    }

    return context;
}

export const AuthProvider = ({ children }) => {
    const [user, dispatch] = useReducer(AuthReducer, initialState);

    return (
        <AuthStateContext.Provider value={user}>
            <AuthDispatchContext.Provider value={dispatch}>
                {children}
            </AuthDispatchContext.Provider>
        </AuthStateContext.Provider>
    );
};

let user = localStorage.getItem("currentUser")
    ? JSON.parse(localStorage.getItem("currentUser")).user
    : null;
let token = localStorage.getItem("currentUser")
    ? JSON.parse(localStorage.getItem("currentUser")).token
    : null;

export const initialState = {
    user: null || user,
    token: null || token,
    errorMessage: null,
};

export const AuthReducer = (initialState, action) => {
    switch (action.type) {
        case "LOGIN_SUCCESS":
            localStorage.setItem("currentUser", JSON.stringify(action.payload));
            return {
                ...initialState,
                user: action.payload.user,
                token: action.payload.token,
            };
        case "LOGOUT":
            localStorage.removeItem("currentUser");
            return {
                ...initialState,
                user: null,
                token: null,
            };

        case "LOGIN_ERROR":
            localStorage.removeItem("currentUser");
            return {
                ...initialState,
                errorMessage: action.error,
            };

        default:
            throw new Error(`Unhandled action type: ${action.type}`);
    }
};

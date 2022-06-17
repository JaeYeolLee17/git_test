import React, { useContext, useReducer, Dispatch } from "react";

type UserInfo = {
    [key: string]: any;
};

type AuthState = {
    user: UserInfo | null;
    token: string | null;
    errorMessage: string | null;
};

type LoginInfo = {
    user: UserInfo;
    token: string;
};

type AuthAction =
    | { type: "LOGIN_SUCCESS"; payload: LoginInfo }
    | { type: "LOGOUT" }
    | { type: "LOGIN_ERROR"; error: string };

type AuthDispatch = Dispatch<AuthAction>;

const AuthStateContext = React.createContext<AuthState | null>(null);
const AuthDispatchContext = React.createContext<AuthDispatch | null>(null);

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

export const AuthProvider = (props: any) => {
    const [user, dispatch] = useReducer(AuthReducer, initialState);

    return (
        <AuthStateContext.Provider value={user}>
            <AuthDispatchContext.Provider value={dispatch}>
                {props.children}
            </AuthDispatchContext.Provider>
        </AuthStateContext.Provider>
    );
};

const initUser = localStorage.getItem("currentUser")
    ? JSON.parse(localStorage.getItem("currentUser") || "{}").user
    : null;
const initToken = localStorage.getItem("currentUser")
    ? JSON.parse(localStorage.getItem("currentUser") || "{}").token
    : null;

export const initialState: AuthState = {
    user: null || initUser,
    token: null || initToken,
    errorMessage: null,
};

export const AuthReducer = (
    initialState: AuthState,
    action: AuthAction
): AuthState => {
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
            throw new Error("Unhandled action type");
    }
};

import React, { useRef, useState, useEffect } from "react";
import { useAuthDispatch } from "../provider/AuthProvider";

import axios from "axios";

import { useNavigate } from "react-router-dom";

import * as Request from "../commons/request";
import * as Common from "../commons/common";

import * as Utils from "../utils/utils";

import { useAsyncAxios } from "../utils/customHooks";

const Login = () => {
    const dispatch = useAuthDispatch();
    const userRef = useRef();

    const [user, setUser] = useState("");
    const [pwd, setPwd] = useState("");

    useEffect(() => {
        userRef.current.focus();
        localStorage.removeItem("currentUser");
    }, []);

    const navigate = useNavigate();

    const requestLogin = async (url, option) => {
        const response = await Utils.utilAxios().post(url, option);
        return response.data;
    };

    const {
        loading,
        error: errorLogin,
        data: resultLogin,
        execute: login,
    } = useAsyncAxios(requestLogin);

    useEffect(() => {
        if (resultLogin === null) return;

        console.log(JSON.stringify(resultLogin));
        dispatch({ type: "LOGIN_SUCCESS", payload: resultLogin });

        // JWT
        const accessToken = resultLogin?.data?.token;
        axios.defaults.headers.common[
            "Authorization"
        ] = `Bearer ${accessToken}`;

        localStorage.setItem("currentUser", JSON.stringify(resultLogin));
        navigate(Common.PAGE_DASHBOARD);
    }, [resultLogin]);

    useEffect(() => {
        if (errorLogin === false || errorLogin === null) return;

        dispatch({ type: "LOGIN_ERROR", error: "Login failed" });
        localStorage.removeItem("currentUser");
    }, [errorLogin]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const sha256 = require("sha256");

        const result = await login(
            Request.LOGIN_URL,
            JSON.stringify({ userId: user, password: sha256(pwd) })
        );

        //console.log("result", result);

        setUser("");
        setPwd("");
    };

    return (
        <>
            <section>
                <h1>Sign In</h1>
                <form onSubmit={handleSubmit}>
                    <label htmlFor='username'>Username:</label>
                    <input
                        type='text'
                        id='username'
                        ref={userRef}
                        autoComplete='off'
                        onChange={(e) => setUser(e.target.value)}
                        value={user}
                        required
                    />

                    <label htmlFor='password'>Password:</label>
                    <input
                        type='password'
                        id='password'
                        onChange={(e) => setPwd(e.target.value)}
                        value={pwd}
                        required
                    />
                    <button>Sign In</button>
                </form>
            </section>
        </>
    );
};

export default Login;

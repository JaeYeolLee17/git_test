import React, { useRef, useState, useEffect } from "react";
import { useAuthDispatch } from "../provider/AuthProvider";

import axios from "axios";

import { useNavigate } from "react-router-dom";

import * as Request from "../request";

import * as Utils from "../utils/utils";

const Login = () => {
    const dispatch = useAuthDispatch();
    const userRef = useRef();
    const errRef = useRef();

    const [user, setUser] = useState("");
    const [pwd, setPwd] = useState("");
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        userRef.current.focus();
        localStorage.removeItem("currentUser");
    }, []);

    useEffect(() => {
        setErrMsg("");
    }, [user, pwd]);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await Utils.utilAxios().post(
                Request.LOGIN_URL,
                JSON.stringify({ userId: user, password: pwd })
            );

            //console.log(JSON.stringify(response?.data));
            //console.log(JSON.stringify(response));
            const result = response?.data?.result;
            //console.log("result", result);

            //const accessToken = response?.data?.token;
            //const roles = response?.data?.user?.authority;
            //console.log("accessToken", accessToken);
            //console.log("roles", roles);
            //setAuth({ user, pwd, roles, accessToken });
            setUser("");
            setPwd("");
            setSuccess(true);

            if (result === "ok") {
                dispatch({ type: "LOGIN_SUCCESS", payload: response.data });

                // JWT
                const accessToken = response?.data?.token;
                axios.defaults.headers.common[
                    "Authorization"
                ] = `Bearer ${accessToken}`;

                localStorage.setItem(
                    "currentUser",
                    JSON.stringify(response.data)
                );
                navigate("/dashboard");
            } else {
                dispatch({ type: "LOGIN_ERROR", error: "Login failed" });
                localStorage.removeItem("currentUser");
            }
            //navigate("/dashboard");
        } catch (err) {
            if (!err?.response) {
                setErrMsg("No Server Response");
            } else if (err.response?.status === 400) {
                setErrMsg("Missing Username or Password");
            } else if (err.response?.status === 401) {
                setErrMsg("Unauthorized");
            } else {
                setErrMsg("Login Failed");
            }
            errRef.current.focus();
        }
    };

    return (
        <>
            {success ? (
                <section>
                    <h1>You are logged in!</h1>
                    <br />
                    {/* <p>
                        <a href='#'>Go to Home</a>
                    </p> */}
                </section>
            ) : (
                <section>
                    <p
                        ref={errRef}
                        className={errMsg ? "errmsg" : "offscreen"}
                        aria-live='assertive'
                    >
                        {errMsg}
                    </p>
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
            )}
        </>
    );
};

export default Login;

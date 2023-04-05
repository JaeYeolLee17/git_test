import React, { useRef, useState, useEffect } from "react";
import { useAuthDispatch } from "../provider/AuthProvider";

import axios from "axios";

import { useNavigate } from "react-router-dom";

import * as Request from "../commons/request";
import * as Common from "../commons/common";

import * as Utils from "../utils/utils";

import { useAsyncAxios } from "../utils/customHooks";

import "../assets/font/font.css";
import styles from "./Login.module.css";
import loginIcon from "../assets/images/login_ico.png";
import daeguLogo from "../assets/images/logo_daegu.png";

const Login = () => {
    const dispatch = useAuthDispatch();
    const userRef = useRef<HTMLInputElement>(null);

    const [user, setUser] = useState<string>("");
    const [pwd, setPwd] = useState<string>("");

    useEffect(() => {
        if (userRef === null) return;
        if (userRef.current === undefined) return;
        userRef.current?.focus();
        //Utils.removeLocalStorage(Common.CONTEXT_AUTH);
        if (dispatch !== null) dispatch({ type: "LOGOUT" });
    }, []);

    const navigate = useNavigate();

    const requestLogin = async (url: string, option: Record<string, string>) => {
        const response = await Utils.utilAxios().post(url, option);
        return response.data;
    };

    const { loading, error: errorLogin, data: resultLogin, execute: login } = useAsyncAxios(requestLogin);

    useEffect(() => {
        if (resultLogin === null) return;

        if (dispatch !== null)
            dispatch({
                type: "LOGIN_SUCCESS",
                payload: {
                    user: resultLogin.user,
                    token: resultLogin.token,
                },
            });

        // JWT
        const accessToken = resultLogin?.token;
        axios.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;

        Utils.setLocalStorage(Common.CONTEXT_AUTH, JSON.stringify(resultLogin));
        navigate(Common.PAGE_DASHBOARD);
    }, [resultLogin]);

    useEffect(() => {
        if (errorLogin === null) return;

        if (dispatch !== null) dispatch({ type: "LOGIN_ERROR", error: "Login failed" });
        //Utils.removeLocalStorage(Common.CONTEXT_AUTH);
    }, [errorLogin]);

    const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const sha256 = require("sha256");

        let result;
        if (process.env.REACT_APP_NEXT_DEV === "false") {
            result = await login(Request.LOGIN_URL, JSON.stringify({ userId: user, password: sha256(pwd) }));
        } else {
            result = await login(Request.LOGIN_URL, JSON.stringify({ username: user, password: pwd }));
        }

        //console.log("result", result);

        setUser("");
        setPwd("");
    };

    return (
        <div className={styles.page}>
            <section className={styles.left}>
                <div className={styles.appNameWrapper}>
                    <div className={styles.appName}>
                        대구광역시 교통모니터링 시스템
                    </div>
                </div>
                <div className={styles.logos}>
                    <img src={daeguLogo} alt="daegu" />
                </div>
            </section>

            <section className={styles.right}>
                <div className={styles.loginCard}>
                    <img src={loginIcon} alt="login-icon" />
                    <h1>로그인하기</h1>
                    <p>등록된 아이디ㆍ비밀번호를 입력하여 로그인해 주세요.</p>
                    <form onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <label htmlFor="username">아이디</label>
                            <input
                                type="text"
                                id="username"
                                ref={userRef}
                                autoComplete="off"
                                onChange={(e) => setUser(e.target.value)}
                                value={user}
                                placeholder="아이디를 입력해주세요."
                                required
                            />
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="password">비밀번호</label>
                            <input
                                type="password"
                                id="password"
                                onChange={(e) => setPwd(e.target.value)}
                                value={pwd}
                                placeholder="비밀번호를 입력해주세요."
                                required
                            />
                        </div>
                        <button className={styles.loginBtn}>로그인</button>
                    </form>
                </div>
            </section>
        </div>
    );
};

export default Login;

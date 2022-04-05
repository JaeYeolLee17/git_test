import { render, screen } from "../customRender";
import Login from "./Login";
import * as Common from "../commons/common";
import { createMemoryHistory } from "history";
import { Router } from "react-router-dom";
import userEvent from "@testing-library/user-event";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import * as Request from "../commons/request";
import { act } from "@testing-library/react";

describe("login page", () => {
    beforeEach(() => {
        const history = createMemoryHistory();
        history.push(Common.PAGE_LOGIN);
        render(
            <Router location={history.location} navigator={history}>
                <Login />
            </Router>
        );
    });

    test("input username", () => {
        const username = screen.getByLabelText(/아이디/i);
        expect(username).toBeTruthy();
        expect(username.required).toBeTruthy();
    });

    test("input password", () => {
        const password = screen.getByLabelText(/비밀번호/i);
        expect(password).toBeTruthy();
        expect(password.required).toBeTruthy();
    });

    test("button login", () => {
        const buttonLogin = screen.getByRole("button", { name: /로그인/i });
        expect(buttonLogin).toBeTruthy();
    });
});

describe("login axios", () => {
    test("call login api - navigate to dashboard", async () => {
        const mock = new MockAdapter(axios);
        mock.onPost(Request.LOGIN_URL).reply(200, {
            result: "ok",
            user: {
                userId: "admin",
                password: null,
                username: "최고관리자",
                email: null,
                phone: null,
                authority: "ROLE_ADMIN",
            },
            token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjQ4NjE5NjkyLCJleHAiOjE2NDg3MDYwOTJ9.d9B55c3k7wAynEPqWlIEbjzmzc6SvdQ8atCwjLcMarM",
        });

        const history = createMemoryHistory();
        history.push(Common.PAGE_LOGIN);
        render(
            <Router location={history.location} navigator={history}>
                <Login />
            </Router>
        );

        const buttonLogin = screen.getByRole("button", { name: /로그인/i });
        await act(async () => {
            userEvent.click(buttonLogin);
        });

        expect(history.location.pathname).toEqual(Common.PAGE_DASHBOARD);
    });

    test("get user from localStorage", () => {
        expect(JSON.parse(localStorage.getItem("currentUser")).user).toEqual({
            userId: "admin",
            password: null,
            username: "최고관리자",
            email: null,
            phone: null,
            authority: "ROLE_ADMIN",
        });
    });

    test("get token from localStorage", () => {
        expect(JSON.parse(localStorage.getItem("currentUser")).token).toEqual(
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjQ4NjE5NjkyLCJleHAiOjE2NDg3MDYwOTJ9.d9B55c3k7wAynEPqWlIEbjzmzc6SvdQ8atCwjLcMarM"
        );
    });
});

// test("login axios", async () => {
//     const mock = new MockAdapter(axios);
//     mock.onPost(Request.LOGIN_URL).reply(200, {
//         result: "ok",
//         user: {
//             userId: "admin",
//             password: null,
//             username: "최고관리자",
//             email: null,
//             phone: null,
//             authority: "ROLE_ADMIN",
//         },
//         token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjQ4NjE5NjkyLCJleHAiOjE2NDg3MDYwOTJ9.d9B55c3k7wAynEPqWlIEbjzmzc6SvdQ8atCwjLcMarM",
//     });

//     const history = createMemoryHistory();
//     history.push(Common.PAGE_LOGIN);
//     render(
//         <Router location={history.location} navigator={history}>
//             <Login />
//         </Router>
//     );

//     const buttonLogin = screen.getByRole("button", { name: /로그인/i });
//     await act(async () => {
//         userEvent.click(buttonLogin);
//     });

//     expect(JSON.parse(localStorage.getItem("currentUser")).user).toEqual({
//         userId: "admin",
//         password: null,
//         username: "최고관리자",
//         email: null,
//         phone: null,
//         authority: "ROLE_ADMIN",
//     });
//     expect(JSON.parse(localStorage.getItem("currentUser")).token).toEqual(
//         "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjQ4NjE5NjkyLCJleHAiOjE2NDg3MDYwOTJ9.d9B55c3k7wAynEPqWlIEbjzmzc6SvdQ8atCwjLcMarM"
//     );
//     expect(history.location.pathname).toEqual(Common.PAGE_DASHBOARD);
// });

describe("login axios error 1", () => {
    test("call login api error", async () => {
        const mock = new MockAdapter(axios);
        mock.onPost(Request.LOGIN_URL).reply(405, {});

        const history = createMemoryHistory();
        history.push(Common.PAGE_LOGIN);
        render(
            <Router location={history.location} navigator={history}>
                <Login />
            </Router>
        );

        const buttonLogin = screen.getByRole("button", { name: /로그인/i });
        await act(async () => {
            userEvent.click(buttonLogin);
        });

        expect(history.location.pathname).toEqual(Common.PAGE_LOGIN);
    });

    test("get user from localStorage", () => {
        expect(localStorage.getItem("currentUser")).toBeNull();
    });
});

describe("login axios error 2", () => {
    test("call login api error", async () => {
        const mock = new MockAdapter(axios);
        mock.onPost(Request.LOGIN_URL).reply(200, { result: "fail" });

        const history = createMemoryHistory();
        history.push(Common.PAGE_LOGIN);
        render(
            <Router location={history.location} navigator={history}>
                <Login />
            </Router>
        );

        const buttonLogin = screen.getByRole("button", { name: /로그인/i });
        await act(async () => {
            userEvent.click(buttonLogin);
        });

        expect(history.location.pathname).toEqual(Common.PAGE_LOGIN);
    });

    test("get user from localStorage", () => {
        expect(localStorage.getItem("currentUser")).toBeNull();
    });
});

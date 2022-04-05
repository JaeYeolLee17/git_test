import { render, screen } from "../customRender";
import { useAuthDispatch, useAuthState } from "../provider/AuthProvider";
import { useEffect } from "react";

const userInfo = {
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
};

const Sample = ({ type }) => {
    const dispatch = useAuthDispatch();
    const userDetail = useAuthState();

    useEffect(() => {
        dispatch({ type: type, payload: userInfo });
    }, [type]);

    return (
        <div>
            <div data-testid='userId'>{userDetail.user?.userId}</div>
            <div data-testid='username'>{userDetail.user?.username}</div>
            <div data-testid='authority'>{userDetail.user?.authority}</div>
            <div data-testid='token'>{userDetail.token}</div>
        </div>
    );
};

describe("AuthProvider Login", () => {
    beforeEach(() => {
        render(<Sample type='LOGIN_SUCCESS' />);
    });

    test("userId", () => {
        const element = screen.getByTestId("userId");
        expect(element.innerHTML).toEqual(userInfo.user.userId);
    });

    test("username", () => {
        const element = screen.getByTestId("username");
        expect(element.innerHTML).toEqual(userInfo.user.username);
    });

    test("authority", () => {
        const element = screen.getByTestId("authority");
        expect(element.innerHTML).toEqual(userInfo.user.authority);
    });

    test("token", () => {
        const element = screen.getByTestId("token");
        expect(element.innerHTML).toEqual(userInfo.token);
    });
});

describe("AuthProvider Logout", () => {
    beforeEach(() => {
        render(<Sample type='LOGOUT' />);
    });

    test("userId", () => {
        const element = screen.getByTestId("userId");
        expect(element.innerHTML).toEqual("");
    });

    test("username", () => {
        const element = screen.getByTestId("username");
        expect(element.innerHTML).toEqual("");
    });

    test("authority", () => {
        const element = screen.getByTestId("authority");
        expect(element.innerHTML).toEqual("");
    });

    test("token", () => {
        const element = screen.getByTestId("token");
        expect(element.innerHTML).toEqual("");
    });
});

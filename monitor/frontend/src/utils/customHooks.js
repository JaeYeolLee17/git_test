import { useReducer, useEffect, useRef, useCallback } from "react";
import { useAlert } from "react-alert";

export const useInterval = (callback, delay) => {
    const savedCallback = useRef();

    // Remember the latest function.
    useEffect(() => {
        savedCallback.current = callback;
    }, [callback]);

    // Set up the interval.
    useEffect(() => {
        function tick() {
            savedCallback.current();
        }
        if (delay !== null) {
            let id = setInterval(tick, delay);
            return () => clearInterval(id);
        }
    }, [delay]);
};

const initialAxiosState = {
    loading: false,
    data: null,
    error: null,
};

const ayncAxiosReducer = (state, action) => {
    switch (action.type) {
        case "LOADING":
            return {
                loading: true,
                data: null,
                error: null,
            };
        case "SUCCESS":
            return {
                loading: false,
                data: action.data,
                error: null,
            };
        case "ERROR":
            return {
                loading: false,
                data: null,
                error: action.error,
            };
        case "CLEAR":
            return initialAxiosState;
        default:
            throw new Error(`Unhandled action type: ${action.type}`);
    }
};

export const useAsyncAxios = (callback, immediate = false) => {
    const alert = useAlert();
    const [state, dispatch] = useReducer(ayncAxiosReducer, initialAxiosState);

    const execute = useCallback(
        async (...args) => {
            dispatch({ type: "LOADING" });
            try {
                const data = await callback(...args);
                if (data.result === "ok") {
                    dispatch({ type: "SUCCESS", data });
                    return true;
                } else {
                    alert.error(data.message);
                    dispatch({ type: "ERROR", error: data });
                }
            } catch (e) {
                // TODO: alert error with e message
                dispatch({ type: "ERROR", error: e });
            }
        },
        [callback]
    );

    useEffect(() => {
        immediate && execute();
        return () => dispatch({ type: "CLEAR" });
    }, []);

    return { ...state, execute };
};

import { useReducer, useEffect, useRef, useCallback } from "react";
//import { useAlert } from "react-alert";

export const useInterval = (callback: () => void, delay: number) => {
    const savedCallback = useRef<() => void>();

    // Remember the latest function.
    useEffect(() => {
        savedCallback.current = callback;
    }, [callback]);

    // Set up the interval.
    useEffect(() => {
        function tick() {
            if (savedCallback.current !== undefined) savedCallback.current();
        }
        if (delay !== null) {
            const id: NodeJS.Timer = setInterval(tick, delay);
            return () => clearInterval(id);
        }
    }, [delay]);
};

type AxiosData = {
    [key: string]: any;
};

type AxiosState = {
    loading: boolean;
    data: AxiosData | null;
    error: AxiosData | null;
};

const initialAxiosState: AxiosState = {
    loading: false,
    data: null,
    error: null,
};

type AxiosAction =
    | { type: "LOADING" }
    | { type: "SUCCESS"; data: AxiosData }
    | { type: "ERROR"; error: AxiosData }
    | { type: "CLEAR" };

const ayncAxiosReducer = (state: AxiosState, action: AxiosAction) => {
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
            throw new Error("Unhandled action type");
    }
};

export const useAsyncAxios = (
    callback: (...args: any[]) => AxiosData,
    immediate = false
) => {
    //const alert = useAlert();
    const [state, dispatch] = useReducer(ayncAxiosReducer, initialAxiosState);

    const execute = useCallback(
        async (...args: any[]) => {
            dispatch({ type: "LOADING" });
            try {
                const data = await callback(...args);
                if (data.result === "ok") {
                    dispatch({ type: "SUCCESS", data });
                    return true;
                } else {
                    //alert.error(data.message);
                    dispatch({ type: "ERROR", error: data });
                }
            } catch (e: any) {
                // TODO: alert error with e message
                dispatch({ type: "ERROR", error: e.message });
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

/// <reference types="react-scripts" />
declare namespace NodeJS {
    interface ProcessEnv {
        readonly REACT_APP_NEXT_DEV: boolean;

        readonly REACT_APP_API_URI: string;
        readonly REACT_APP_API_PREFIX: string;
        readonly REACT_APP_PROXY_FOR_API_URI: string;

        readonly REACT_APP_STREAM_HIGH_RESOLUTION: boolean;
        readonly REACT_APP_STREAM_URI: string;
        readonly REACT_APP_STREAM_WS_URI: string;
    }
}

/// <reference types="react-scripts" />
declare namespace NodeJS {
    interface ProcessEnv {
        readonly REACT_APP_NEXT_DEV: string;

        readonly REACT_APP_DEV_URI: string;
        readonly REACT_APP_DEV_API_URI: string;

        readonly REACT_APP_PROD_URI: string;
        readonly REACT_APP_PROD_API_URI: string;

        readonly REACT_APP_PROD_PROXY_URI: string;
        readonly REACT_APP_PROD_PROXY_API_URI: string;

        readonly REACT_APP_PROD_INTERNAL_URI: string;
        readonly REACT_APP_PROD_INTERNAL_API_URI: string;

        readonly REACT_APP_API_PREFIX: string;
        readonly REACT_APP_PROXY_FOR_API_URI: string;

        readonly REACT_APP_STREAM_HIGH_RESOLUTION: string;
        readonly REACT_APP_STREAM_URI: string;
        readonly REACT_APP_STREAM_WS_URI: string;
    }
}

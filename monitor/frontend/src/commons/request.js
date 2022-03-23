const DEFAULT_URL =
    process.env.REACT_APP_API_URI + process.env.REACT_APP_API_PREFIX + "/v1/";

export const LOGIN_URL = DEFAULT_URL + "login";

export const CAMERA_URL = DEFAULT_URL + "cameras";

export const REGIONS_LIST_URL = DEFAULT_URL + "regions";

export const INTERSECTIONS_LIST_URL = DEFAULT_URL + "intersections";

export const STAT_MFD_URL = DEFAULT_URL + "stat/mfd";
export const STAT_LINK_URL = DEFAULT_URL + "stat/link";
export const STAT_DAILY_URL = DEFAULT_URL + "stat/daily";

export const AVL_URL = DEFAULT_URL + "avl";

export const TSI_URL = DEFAULT_URL + "tsi";

export const USERS_URL = DEFAULT_URL + "users";

export const STREAM_URL_START = "/start";
export const STREAM_URL_STOP = "/stop";

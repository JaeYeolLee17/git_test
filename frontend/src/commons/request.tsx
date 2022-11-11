const DEFAULT_URL =
    process.env.REACT_APP_API_URI + process.env.REACT_APP_API_PREFIX;

export const LOGIN_URL = DEFAULT_URL + "login";

export const CAMERA_URL = DEFAULT_URL + "camera";
export const CAMERA_LIST_URL = DEFAULT_URL + "cameras";

export const INTERSECTION_URL = DEFAULT_URL + "intersection";
export const INTERSECTION_LIST_URL = DEFAULT_URL + "intersections";

export const LINK_URL = DEFAULT_URL + "link";
export const LINK_LIST_URL = DEFAULT_URL + "links";

export const REGION_URL = DEFAULT_URL + "region";
export const REGIONS_LIST_URL = DEFAULT_URL + "regions";

export const EMERGENCY_URL = DEFAULT_URL + "avl/car";
export const EMERGENCY_LIST_URL = DEFAULT_URL + "avl/cars";

export const USER_URL = DEFAULT_URL + "user";
export const USER_LIST_URL = DEFAULT_URL + "users";

export const STAT_MFD_URL = DEFAULT_URL + "data/stats/mfd";
export const STAT_LINK_URL = DEFAULT_URL + "data/stats/link";
export const STAT_DAILY_URL = DEFAULT_URL + "data/stats/daily";
export const STAT_PERIOD_URL = DEFAULT_URL + "data/stats/period";

export const AVL_URL = DEFAULT_URL + "avl";
export const AVL_STAT_LIST_URL = DEFAULT_URL + "avl/stat/list";
export const AVL_STAT_PERIOD_URL = DEFAULT_URL + "avl/stat/period";
export const AVL_TRACK_URL = DEFAULT_URL + "avl/track";

export const TSI_URL = DEFAULT_URL + "tsi";

export const USERS_URL = DEFAULT_URL + "users";

export const STREAM_URL_START = "/start";
export const STREAM_URL_STOP = "/stop";

export const WEATHER_URL = DEFAULT_URL + "weather";

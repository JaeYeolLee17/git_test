export const trafficColorNormal = "#00e025";
export const trafficColorSlowly = "#ff8800";
export const trafficColorBusy = "#df0900";
export const trafficColorBorder = "#ffffff";

export const trafficSpeedNormal = 30;
export const trafficSpeedBusy = 25;

export type ChartMfdElement = {
    time: string;
    x: number;
    y: number;
};

export type ChartMfdData = {
    name: string;
    data: ChartMfdElement[];
};

export type GpsPosition = {
    lat: number;
    lng: number;
};

export type ChartElement = {
    x: number;
    y: number | string;
};

export type ChartData = {
    name: string;
    data: ChartElement[];
};

export type ChartStatData = {
    loading: boolean;
    name?: string;
    series: ChartData[];
    option: Record<string, any>;
};

export type RegionInfo = {
    regionNo: string;
    regionName?: string;
    gps?: GpsPosition[];
};

export type SelectorRegion = {
    selectedRegionNo: string;
    onChangedRegionList?: (listRegions: any[]) => void;
    onChangedCurrentRegion: (currentRegionInfo: RegionInfo) => void;
};

export type IntersectionInfo = {
    intersectionNo: string;
    intersectionName?: string;
    gps?: GpsPosition;
    region?: RegionInfo;
};

export type SelectorIntersection = {
    currentRegionInfo: RegionInfo;
    selectedIntersectionNo: string;
    onChangedIntersectionList?: (listIntersections: any[]) => void;
    onChangedCurrentIntersection?: (
        currentIntersectionInfo: IntersectionInfo
    ) => void;
};

export type StreamInfo = {
    cameraNo: string;
    port: string;
};

export const Authority = {
    ROLE_ADMIN: "ROLE_ADMIN",
    ROLE_MANAGER: "ROLE_MANAGER",
    ROLE_USER: "ROLE_USER",
    ROLE_DATA: "ROLE_DATA",
    ROLE_CAMERAADMIN: "ROLE_CAMERAADMIN",
    ROLE_CAMERA: "ROLE_CAMERA",
};

// Page Link
export const PAGE_LOGIN = "/login";
export const PAGE_DASHBOARD = "/dashboard/summary";
export const PAGE_DASHBOARD_DETAIL = "/dashboard/detail/all";
export const PAGE_STAT_TRAFFIC = "/stats/traffic";
export const PAGE_STAT_EMERGENCY = "/stats/emergency";
export const PAGE_MANAGEMENT_CAMERA = "/management/camera";
export const PAGE_MANAGEMENT_CAMERA_DETAIL = "/management/camera/detail";
export const PAGE_MANAGEMENT_INTERSECTION = "/management/intersection";
export const PAGE_MANAGEMENT_INTERSECTION_DETAIL =
    "/management/intersection/detail";
export const PAGE_MANAGEMENT_REGION = "/management/region";
export const PAGE_MANAGEMENT_REGION_DETAIL = "/management/region/detail";
export const PAGE_MANAGEMENT_LINK = "/management/link";
export const PAGE_MANAGEMENT_LINK_DETAIL = "/management/link/detail";
export const PAGE_MANAGEMENT_EMERGENCY = "/management/emergency";
export const PAGE_MANAGEMENT_EMERGENCY_DETAIL = "/management/emergency/detail";
export const PAGE_MANAGEMENT_USER = "/management/user";
export const PAGE_MANAGEMENT_USER_DETAIL = "/management/user/detail";

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
    regionId: string;
    regionName?: string;
    gps?: GpsPosition[];
};

export type SelectorRegion = {
    selectedRegionId: string;
    onChangedRegionList?: (listRegions: any[]) => void;
    onChangedCurrentRegion: (currentRegionInfo: RegionInfo) => void;
};

export type IntersectionInfo = {
    intersectionId: string;
    intersectionName?: string;
    gps?: GpsPosition;
    region?: RegionInfo;
};

export type SelectorIntersection = {
    currentRegionInfo: RegionInfo;
    selectedIntersectionId: string;
    onChangedIntersectionList?: (listIntersections: any[]) => void;
    onChangedCurrentIntersection?: (
        currentIntersectionInfo: IntersectionInfo
    ) => void;
};

export type StreamInfo = {
    cameraId: string;
    port: string;
};

// Page Link
export const PAGE_LOGIN = "/login";
export const PAGE_DASHBOARD = "/dashboard/summary";
export const PAGE_DASHBOARD_DETAIL = "/dashboard/detail/all";
export const PAGE_STAT_TRAFFIC = "/stats/traffic";
export const PAGE_MANAGEMENT_CAMERA = "/management/camera";
export const PAGE_MANAGEMENT_CAMERA_DETAIL = "/management/camera/detail";

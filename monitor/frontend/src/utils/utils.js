export const utilGetCamera = (listCamera, cameraId) => {
    let camera = listCamera.filter((element) => element.cameraId === cameraId);
    return camera[0];
};

export const utilGetInstsectionCameras = (listCamera, cameraId) => {
    let cameraData = utilGetCamera(listCamera, cameraId);
    let cameras = listCamera.filter(
        (camera) =>
            cameraData.intersection.intersectionId ===
            camera.intersection.intersectionId
    );
    return cameras;
};

export const utilIsPremakeIntersection = (intersectionId) => {
    if (
        intersectionId === "I0001" ||
        intersectionId === "I0002" ||
        intersectionId === "I0003" ||
        intersectionId === "I0004" ||
        intersectionId === "I0005"
    ) {
        return true;
    }

    return false;
};

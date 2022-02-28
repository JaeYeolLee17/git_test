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

export const utilFormatDateYYYYMMDD000000 = (date) => {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let format =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " 00:00:00";

    return format;
};

export const utilFormatDateYYYYMMDDHHmm00 = (date) => {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();

    let format =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " " +
        (hour < 10 ? "0" + hour : hour) +
        ":" +
        (minute < 10 ? "0" + minute : minute) +
        ":00";

    return format;
};

export const utilLeadingZeros = (n, digits) => {
    var zero = "";
    n = n.toString();

    if (n.length < digits) {
        for (var i = 0; i < digits - n.length; i++) zero += "0";
    }
    return zero + n;
};

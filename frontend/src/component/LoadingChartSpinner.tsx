import React from "react";
import styles from "./LoadingChartSpinner.module.css";

function LoadingChartSpinner({ loading }: { loading: boolean }) {
    let showClass = styles.chartLoadingSpinnerFadeEnd;
    if (loading) {
        showClass = styles.chartLoadingSpinnerFade;
    }

    return (
        <div
            className={[styles.chartLoadingSpinnerOverlay, showClass].join(" ")}
        >
            <div className={styles.chartLoadingSpinner}></div>
        </div>
    );
}

export default LoadingChartSpinner;

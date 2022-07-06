import React from "react";

import styles from "./ToggleImageButton.module.css";

type ToggleButtonType = {
    bOn: boolean;
    text?: string;
    imageOff?: string;
    imageOn?: string;
    tooltip?: string;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
};

function ToggleImageButton({
    bOn,
    text,
    imageOff,
    imageOn,
    tooltip,
    onClick,
}: ToggleButtonType) {
    const classNames = [
        styles.btnMapToggle,
        styles.btnMapTooltip,
        bOn ? styles.btnMapTogglePressed : "",
    ].join(" ");
    return bOn ? (
        <button className={classNames} onClick={onClick}>
            {imageOn !== undefined ? <img src={imageOn} /> : null}
            {tooltip !== undefined ? (
                <span className={styles.btnMapTooltipText}>{tooltip}</span>
            ) : null}
        </button>
    ) : (
        <button className={classNames} onClick={onClick}>
            {imageOff !== undefined ? <img src={imageOff} /> : null}
            {tooltip !== undefined ? (
                <span className={styles.btnMapTooltipText}>{tooltip}</span>
            ) : null}
        </button>
    );
}

export default ToggleImageButton;

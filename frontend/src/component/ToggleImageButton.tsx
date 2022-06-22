import React from "react";

type ToggleButtonType = {
    bOn: boolean;
    text: string;
    image?: string;
    tooltip?: string;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
};

function ToggleImageButton({
    bOn,
    text,
    image,
    tooltip,
    onClick,
}: ToggleButtonType) {
    return bOn ? (
        <button style={{ backgroundColor: "#1f5499" }} onClick={onClick}>
            {text}
        </button>
    ) : (
        <button style={{ backgroundColor: "#ffffff" }} onClick={onClick}>
            {text}
        </button>
    );
}

export default ToggleImageButton;

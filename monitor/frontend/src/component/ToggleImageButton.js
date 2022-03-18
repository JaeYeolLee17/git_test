import React from "react";

function ToggleImageButton({ bOn, text, image, tooltip, onClick }) {
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

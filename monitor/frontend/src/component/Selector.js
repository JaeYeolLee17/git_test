import React from "react";

function Selector({ list, selected, onChange }) {
    return (
        <select onChange={onChange} value={selected.value}>
            {list.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.innerHTML}
                </option>
            ))}
        </select>
    );
}

export default Selector;

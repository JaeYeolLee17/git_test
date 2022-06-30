import React from "react";

export type SelectorItemType = { value: string; innerHTML: string };

export type SelectorType = {
    list: SelectorItemType[];
    selected: SelectorItemType;
    onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
};

function Selector({ list, selected, onChange }: SelectorType) {
    return (
        <select onChange={onChange} value={selected?.value}>
            {list.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.innerHTML}
                </option>
            ))}
        </select>
    );
}

export default Selector;

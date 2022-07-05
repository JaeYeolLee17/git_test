import React, { ReactNode } from "react";

import Select, { SelectChangeEvent } from "@mui/material/Select";
import { FormControl, InputLabel, MenuItem } from "@mui/material";

export type SelectorItemType = { value: string; innerHTML: string };

export type SelectorType = {
    list: SelectorItemType[];
    selected: SelectorItemType;
    onChange: (e: SelectChangeEvent) => void;
    className?: string;
};

function Selector({ list, selected, onChange, className }: SelectorType) {
    return (
        // <select onChange={onChange} value={selected?.value}>
        //     {list.map((option) => (
        //         <option key={option.value} value={option.value}>
        //             {option.innerHTML}
        //         </option>
        //     ))}
        // </select>

        <FormControl fullWidth>
            <Select
                className={className}
                value={selected?.value}
                onChange={onChange}
            >
                {list.map((option) => (
                    <MenuItem key={option.value} value={option.value}>
                        {option.innerHTML}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
}

export default Selector;

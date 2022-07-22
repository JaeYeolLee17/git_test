import React from "react";
import SearchIcon from "@mui/icons-material/Search";

import styles from "./SearchButton.module.css";
import { Box } from "@mui/material";

function SearchButton({ onSearch }: { onSearch?: () => void }) {
    return (
        <Box>
            <button
                type='button'
                className={styles.searchBtn}
                onClick={onSearch}
            >
                <SearchIcon />
                <span>검색</span>
            </button>
        </Box>
    );
}

export default SearchButton;

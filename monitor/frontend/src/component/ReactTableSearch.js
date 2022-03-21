import React from "react";

function ReactTableSearch({ onSubmit }) {
    const handleSubmit = (event) => {
        event.preventDefault();
        onSubmit(event.target.elements.filter.value);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input name='filter' />
            <button>Search</button>
        </form>
    );
}

export default ReactTableSearch;

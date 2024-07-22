import "./style.scss"
import React from "react";
import {Row} from 'react-bootstrap';
import RecipeCard from "../recipe-card/RecipeCard";
import PaginationComponent from "../pagination/PaginationComponent";
import SpinnerComponent from "../spinner/SpinnerComponent";

export default function RecipeList({searchResults, searchQuery, pageSize, page, setPage, title}) {
    const recipes = searchResults?.results || [];
    const totalResults = searchResults?.totalResults;

    if (!recipes?.length && searchQuery) {
        return <h2 className="text-muted bg-light p-3">No results for: "{searchQuery}"</h2>;
    }

    if (!recipes?.length) {
        return <SpinnerComponent/>
    }

    return (
        <div className="container d-flex flex-wrap justify-content-center">
            <Row>
                <h2 className={`text-muted mt-5 mb-3`}>{title}</h2>
            </Row>
            <Row className="w-100">
                <div className="recipe-list d-flex justify-content-center flex-wrap">
                    {recipes.map((recipe) => (<RecipeCard key={recipe.id} recipe={recipe}/>))}
                </div>
            </Row>
            <Row>
                {totalResults &&
                    <PaginationComponent pageSize={pageSize}
                                         total={totalResults}
                                         page={page}
                                         setPage={setPage}
                    />
                }
            </Row>
        </div>
    );
};

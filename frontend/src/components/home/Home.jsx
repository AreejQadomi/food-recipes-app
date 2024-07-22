import React, {useEffect, useState} from 'react';
import ErrorComponent from "../error/ErrorComponent";
import RecipeList from '../recipe-list/RecipeList';
import SearchBox from '../search-box/SearchBox';
import {fetchRecipes, fetchRandomRecipes} from '../../services/api';
import {DEFAULT_PAGE, DEFAULT_PAGE_SIZE} from '../../services/api-properties';

export default function Home() {
    const [searchResults, setSearchResults] = useState([]);
    const [randomRecipes, setRandomRecipes] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [page, setPage] = useState(DEFAULT_PAGE);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchRecipes(searchQuery, page);
                setSearchResults(data);
            } catch (error) {
                setError(error.message);
            }
        };
        searchQuery ? fetchData() : getRandomRecipes();
    }, [page]);

    const handleSearch = async (query) => {
        try {
            const data = await fetchRecipes(query);
            setSearchQuery(query);
            setSearchResults(data);
            setPage(1); // reset pagination on new search
        } catch (error) {
            setError(error.message);
        }
    };

    const getRandomRecipes = async () => {
        try {
            const data = await fetchRandomRecipes();
            setRandomRecipes(data)
        } catch (error) {
            setError(error.message);
        }
    };

    if (error) {
        return <ErrorComponent errorMessage={error}/>;
    }

    return (
        <div className="App">
            <SearchBox onSearch={handleSearch}/>
            {searchQuery ?
                <RecipeList searchResults={searchResults} searchQuery={searchQuery}
                            pageSize={DEFAULT_PAGE_SIZE}
                            page={page} setPage={setPage}
                            title={`Explore different recipes for: "${searchQuery}"`}
                />

                : <RecipeList searchResults={randomRecipes} title="Check out these recipes!"/>
            }
        </div>
    );
}


import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Home from './components/home/Home';
import RecipeDetail from "./components/recipe-detail/RecipeDetail";
import Header from "./components/header/Header";

export default function App() {
    return (
        <>
            <Header/>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/recipes/:recipeId" element={<RecipeDetail/>}/>
                </Routes>
            </BrowserRouter>
        </>
    );
}

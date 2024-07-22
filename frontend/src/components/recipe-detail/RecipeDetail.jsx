import React, {useState, useEffect} from "react";
import {Card, Col, Row} from 'react-bootstrap';
import {fetchRecipeById} from "../../services/api";
import {useParams} from "react-router-dom";
import ErrorComponent from "../error/ErrorComponent";
import SpinnerComponent from "../spinner/SpinnerComponent";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUserGroup, faClock, faNutritionix} from '../../font-awesome/icons';
import "./style.scss"

export default function RecipeDetail() {
    const {recipeId} = useParams();
    const [recipe, setRecipe] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const recipe = await fetchRecipeById(recipeId);
                setRecipe(recipe);
            } catch (error) {
                setError(error.message);
            }
        };
        fetchRecipe();
    }, [recipeId]);

    if (error) {
        return <ErrorComponent errorMessage={error}/>;
    }

    if (!recipe) {
        return <SpinnerComponent/>
    }

    return (
        <div className="container">
            <h2 className="display-6 mt-4">{recipe.title}</h2>
            <Row>
                <Card className="d-flex flex-col flex-wrap flex-md-row px-0 recipe-detail">
                    <Col md={5} lg={7} className="align-content-center">
                        <Card.Img className="w-100 h-100" src={recipe.image} alt={recipe.title}/>
                    </Col>
                    <Col md={7} lg={5}>
                        <Card.Title className="pt-3 px-3">Summary</Card.Title>
                        <Card.Body>
                            <Card.Text dangerouslySetInnerHTML={{__html: recipe.summary}}>
                            </Card.Text>
                        </Card.Body>
                    </Col>
                    <Col className="w-100">
                        <Card.Footer
                            className="d-flex flex-col flex-wrap justify-content-evenly pt-3 mt-auto w-100">
                            <p><FontAwesomeIcon icon={faUserGroup} className="me-2"/>
                                <strong>Servings:</strong> {recipe.servings}</p>
                            <p><FontAwesomeIcon icon={faClock} className="me-2"/> <strong>Preparation
                                Time:</strong> {recipe.preparationTime} mins</p>
                            <p><strong>
                                <FontAwesomeIcon icon={faNutritionix} className="me-2"/>
                                Total Calories:</strong> {recipe.totalCalories}</p>
                        </Card.Footer>
                    </Col>
                </Card>
            </Row>
            <Row>
                <Card className="recipe-detail my-4">
                    <Card.Body className="d-flex flex-col flex-wrap flex-md-row">
                        <Col xs={12} md={9} className="pe-md-4">
                            <Card.Title>Instructions</Card.Title>
                            <Card.Text
                                dangerouslySetInnerHTML={{__html: recipe.instructions}}></Card.Text>
                        </Col>
                        <Col className="pt-4 pt-md-0">
                            <Card.Title>Ingredients</Card.Title>
                            <ul>
                                {recipe.ingredients?.map((ingredient, index) => (
                                    <li key={index}>{ingredient.amount}{"/"}{ingredient.unit} - {ingredient.name}</li>
                                ))}
                            </ul>
                        </Col>
                    </Card.Body>
                </Card>
            </Row>
        </div>
    );
}
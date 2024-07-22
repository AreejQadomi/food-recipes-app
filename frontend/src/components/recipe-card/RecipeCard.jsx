import "./style.scss"
import React from "react";
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUserGroup, faClock, faLink} from "../../font-awesome/icons";
import {getRecipeInformationPath} from "../../services/api-properties";

export default function RecipeCard({recipe}) {

    const onRecipeClick = (recipeId) => {
        // window.location.href = `/recipe/${recipeId}`;
        window.location.href = getRecipeInformationPath(recipeId);
    };

    return (
        <Card className="mx-auto my-2 m-md-2">
            <div className="card-body-wrapper" onClick={() => onRecipeClick(recipe.id)}>
                <div className="card-img-wrapper">
                    <Card.Img variant="top" src={recipe.image} alt={recipe.title}/>
                </div>
                <Card.Body>
                    <Card.Title>{recipe.title}</Card.Title>
                </Card.Body>
            </div>
            <ListGroup className="list-group-flush">
                <ListGroup.Item>
                    <FontAwesomeIcon className="me-1" icon={faUserGroup}/>
                    {recipe.servings} (people)
                </ListGroup.Item>
                <ListGroup.Item>
                    <FontAwesomeIcon className="me-1" icon={faClock}/>
                    {recipe.readyInMinutes} (minutes)</ListGroup.Item>
            </ListGroup>
            <Card.Footer>
                <FontAwesomeIcon className="me-1" icon={faLink}/>
                <Card.Link target="_blank" href={recipe.spoonacularSourceUrl}>View Source
                    URL</Card.Link>
            </Card.Footer>
        </Card>
    );
}

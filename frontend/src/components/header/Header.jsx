import React from "react";
import {Container, Row} from 'react-bootstrap';

export default function Header() {
    return (
        <header className="py-4 text-center">
            <Container>
                <Row className="align-items-center">
                    <a href="/"
                       className="text-muted link-underline-light link-underline-opacity-0">
                        <h1 className="display-4">Food Recipes</h1>
                    </a>
                    <p className="lead">Search your favourite recipes</p>
                </Row>
            </Container>
        </header>
    );
}
import React, {useEffect, useState} from "react";
import {Form, FormControl, Button, Col, Row} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMagnifyingGlass} from '../../font-awesome/icons';
import './style.scss'

export default function SearchBox({onSearch}) {
    const [query, setQuery] = useState('');
    const [lastQuery, setLastQuery] = useState('');
    const [isDisabled, setIsDisabled] = useState(true);

    const handleInputChange = (event) => {
        const value = event.target.value;
        setQuery(value);
    };

    useEffect(() => {
        if (query.length >= 3 && query.length <= 50) {
            setIsDisabled(false);
        } else {
            setIsDisabled(true);
        }
    }, [query]);

    const handleSearch = (e) => {
        e.preventDefault();

        const query = e.target.elements.query.value.trim();
        // prevent search if query length is less than 3 or greater than 50
        // or if the query is the same as the previous one (to avoid unnecessary API calls)
        if (query !== lastQuery) {
            setLastQuery(query);
            onSearch(query);
        }
    };

    return (<div className='search-box text-center pb-4 px-3 px-sm-0'>
        <Row className="m-auto">
            <Form className="search-form d-flex justify-content-center" onSubmit={handleSearch}>
                <Col xs={10} sm={6} md={4} className="me-1 pe-0 p-sm-0">
                    <FormControl type="search" name="query" placeholder="Search for a recipe ..."
                                 aria-label="Search" value={query} onChange={handleInputChange}/>
                </Col>
                <Col xs={1} sm="auto" className="m-0 p-0">
                    <Button variant="outline-light" className="bg-success"
                            type="submit" disabled={isDisabled}><FontAwesomeIcon
                        icon={faMagnifyingGlass}/></Button>
                </Col>
            </Form>
        </Row>
    </div>);
}

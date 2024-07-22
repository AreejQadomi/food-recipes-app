import React, {useEffect, useState} from "react";
import Pagination from 'react-bootstrap/Pagination';

export default function PaginationComponent({pageSize, total, page, setPage}) {
    const [paginationItems, setPaginationItems] = useState([]);
    const [firstDisplayedItem, setFirstDisplayedItem] = useState(1);
    const [activePage, setActivePage] = useState(page);
    const SHOW_ELLIPSIS_AFTER = 5;
    const MAX_NUMBER_OF_PAGES = 90;
    const numberOfPages = Math.min(Math.ceil(total / pageSize), MAX_NUMBER_OF_PAGES);

    const getPaginationItems = () => {
        const items = [];
        if (activePage > 1) {
            items.push(<Pagination.First key="first" onClick={() => handlePageClick(1)}/>);
            items.push(<Pagination.Prev key="prev" onClick={() => handlePageClick(activePage - 1)}/>)
        }

        if (numberOfPages - firstDisplayedItem + 1 <= SHOW_ELLIPSIS_AFTER) {
            for (let pageNumber = firstDisplayedItem; pageNumber <= numberOfPages; pageNumber++) {
                items.push(<Pagination.Item key={pageNumber} active={pageNumber === activePage} onClick={() => handlePageClick(pageNumber)}>
                    {pageNumber}
                </Pagination.Item>);
            }
        } else {
            const hiddenItem = Math.min(numberOfPages, firstDisplayedItem + SHOW_ELLIPSIS_AFTER);
            for (let pageNumber = firstDisplayedItem; pageNumber < hiddenItem; pageNumber++) {
                items.push(<Pagination.Item key={pageNumber} active={pageNumber === activePage} onClick={() => handlePageClick(pageNumber)}>
                    {pageNumber}
                </Pagination.Item>);
            }

            if (activePage === hiddenItem - 1) {
                items.push (<Pagination.Ellipsis key="ellipsis" onClick={() => handleEllipsisClick()}/>);
            }
        }

        if (activePage < numberOfPages) {
            items.push(<Pagination.Next key="next" onClick={() => handlePageClick(activePage + 1)}/>)
            items.push(<Pagination.Last key="last" onClick={() => handlePageClick(numberOfPages)}/>)
        }

        return items;
    };

    useEffect(() => {
        let newItems = [];
        newItems = getPaginationItems();
        setPaginationItems(() => newItems);
    }, [activePage, firstDisplayedItem]);

    const handleEllipsisClick = () => {
        setFirstDisplayedItem(() => activePage);
    }

    const handlePageClick = (pageNumber) => {
        setActivePage(pageNumber);
        setPage(pageNumber);

        if (pageNumber === 1) {
            setFirstDisplayedItem(1);
        } else if (pageNumber === numberOfPages) {
            setFirstDisplayedItem(numberOfPages - SHOW_ELLIPSIS_AFTER + 1);
        } else if (pageNumber < firstDisplayedItem) {
            setFirstDisplayedItem(firstDisplayedItem - SHOW_ELLIPSIS_AFTER + 1);
        } else if (pageNumber >= firstDisplayedItem + SHOW_ELLIPSIS_AFTER) {
            setFirstDisplayedItem(activePage);
        }
    };

    return (
        <div className="py-3">
            <Pagination>{paginationItems}</Pagination>
        </div>
    );
};

import Alert from 'react-bootstrap/Alert';

export default function ErrorComponent({errorMessage}) {
    return (
        <div className="error">
            <Alert variant="danger">
                <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
                <p>
                    {errorMessage}
                </p>
            </Alert>
        </div>
    );
}


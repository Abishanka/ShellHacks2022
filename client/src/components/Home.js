import React, {useState} from 'react';
import * as boot from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';

const Home = () => {
    const [query, setQuery] = useState("Search for street id ...");
    const [tableData, setTableData] = useState([]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const ops = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({param: query})
        };
        fetch('http://localhost:3000/query', ops)
            .then(response => response.json())
            .then(data => this.setTableData())
    }

    return (
        <>
            <boot.Card bg='dark' key ='dark' text='white' style={{ width: '100%', borderRadius: '0px'}} className='mb-5'>
                <h1>Search Engine</h1>
            </boot.Card>
            <boot.Row>
                <boot.Col lg='3' />
                <boot.Col lg='6' style={{justifyContent: 'center', textAlign: 'center', fontSize: '4vh'}}>
                    <boot.Form>
                        <boot.FormGroup className='mb-3'>
                            <boot.Row className='d-flex align-items-end' >
                                    <boot.Col lg='10'>
                                        <boot.FormControl value={query} onChange={(e) => setQuery(e.target.value)} style={{height: '5vh'}}/>
                                    </boot.Col>
                                    <boot.Col lg='2'>
                                        <boot.Button className='bg-dark' style={{borderColor: 'black', fontSize: '2.5vh'}} size='sm'>
                                            Search
                                        </boot.Button>
                                    </boot.Col>
                            </boot.Row>
                        </boot.FormGroup>
                    </boot.Form>
                </boot.Col>
            </boot.Row>
        </>
    );
};

export default Home;
import React, {useState} from 'react';
import * as boot from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '../custom.css'

const Home = () => {
    let mock = [{
        security_id: "123",
        cusip: "23",
        sedol: "23",
        isin: "32",
        ric: "23",
        bloomberg: "23",
        bbg: "23",
        symbol: "",
        root_symbol: "",
        bb_yellow: "",
        spn: "23"
    },
    {
        security_id: "562",
        cusip: "23",
        sedol: "23",
        isin: "32",
        ric: "23",
        bloomberg: "23",
        bbg: "23",
        symbol: "",
        root_symbol: "",
        bb_yellow: "",
        spn: "23"
    },
    
    ]



    const [query, setQuery] = useState("");
    let tableTemplate = {
        security_id: "",
        cusip: "",
        sedol: "",
        isin: "",
        ric: "",
        bloomberg: "",
        bbg: "",
        symbol: "",
        root_symbol: "",
        bb_yellow: "",
        spn: ""
    }
    const [tableData, setTableData] = useState([tableTemplate]);
    const [tableState, setTableState] = useState(false);

    const handleChange = async (e) => {
        e.preventDefault();
        setQuery(e.target.value)
        const ops = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({param: query}),
            mode: 'cors'
        };
        fetch('http://localhost:8080/query', ops)
            .then(response => response.json())
            .then(data => setTableData())
    }

    return (
        <>
            <boot.Card bg='dark' key ='dark' text='white' style={{ width: '100%', borderRadius: '0px', paddingTop: '10px', paddingBottom: '10px', paddingLeft: '7px'}} className='mb-5'>
                <boot.Card.Body>
                <a href='#' style={{color: 'white'}}>Home</a>  &nbsp; &nbsp; <a href='#' style={{color: 'white'}}>About</a>
                </boot.Card.Body>
            </boot.Card>
            <boot.Row>
                <boot.Col lg='3' />
                <boot.Col lg='6' style={{textAlign: 'center', fontSize: '2vh'}}>
                    <boot.Form onSubmit={handleChange} className='mt-3'>
                        <boot.Row className='d-flex align-items-end' >
                            <boot.Col lg='2' />
                            <boot.Col lg='8'>
                                <boot.FormGroup className='mb-3'>
                                    <boot.FormControl className='cursor' class="rq-form-element" value={query} onChange={(e) => handleChange(e)} style={{height: '5vh', borderRadius: '50px'}}/>
                                    <boot.FormText>Search for any "Street ID", for any security ...</boot.FormText>
                                </boot.FormGroup>
                            </boot.Col>
                        </boot.Row>
                        <boot.Row>
                            <boot.Col lg='2' />
                            <boot.Col lg='8'>
                                <boot.Button type='submit' variant='dark'>Download "Security" Search</boot.Button>
                            </boot.Col>
                        </boot.Row>
                    </boot.Form>
                </boot.Col>
            </boot.Row>

            <br />
            <br />

            <boot.Row>
                <boot.Col style={{margin: '0.5vh'}}> 
                <boot.Table id='result' className="table table-dark"> 
                    <thead>
                        <tr>
                            <th>Select</th>
                            <th>symbol</th>
                            <th>security_id</th>
                            <th>cusip</th>
                            <th>sedol</th>
                            <th>isin</th>
                            <th>ric</th>
                            <th>bloomberg</th>
                            <th>bbg</th>
                            <th>root_symbol</th>
                            <th>bb_yellow</th>
                            <th>spn</th>
                        </tr>
                    </thead>
                    <tbody>         
                        {tableData.length > 1 && tableData.map(val => 
                            <tr> 
                                <td>{}</td>   
                                <td>{val.symbol}</td>
                                <td>{val.security_id}</td>
                                <td>{val.cusip}</td>
                                <td>{val.sedol}</td>
                                <td>{val.isin}</td>
                                <td>{val.ric}</td>
                                <td>{val.bloomberg}</td>
                                <td>{val.bbg}</td>
                                <td>{val.root_symbol}</td>
                                <td>{val.bb_yellow}</td>
                                <td>{val.spn}</td>
                            </tr>
                        )}
                    </tbody>
                </boot.Table>
                </boot.Col>
            </boot.Row>
        </>
    );
};

export default Home;
import {useEffect, useState} from "react";
// import {api} from "./apiRequest";
import useAxiosPrivate from "./hooks/useAxiosPrivate";
import DataView from "./DataView";
import "./Table.css"

const Data = ({type}) => {
    const [stocks, setStocks] = useState([]);
    const privateApi = useAxiosPrivate();
    useEffect(() => {
        const controller = new AbortController();
        fetchSymbols(controller.signal, type, privateApi).then(r => {
            setStocks(r);
            console.log(r)
        })
        return () => controller.abort();
    }, [])

    return (
        <div className="regContainer">
            <table class="table_container">
                <thead>
                <tr>
                    <th><h1>Name</h1></th>
                    <th><h1>Price</h1></th>
                    <th><h1>Quantity</h1></th>
                    <th><h1></h1></th>
                </tr>
                </thead>
                <tbody>
                {stocks.map((name) =>
                    <DataView key={name} name={name} assetType={type}/>)}
                </tbody>
            </table>
        </div>
    )
}

const fetchSymbols = async (signal, type, privateApi) => {
    try {
        const url = "/api/v1/asset/" + type;
        const response = await privateApi.get(url, {signal});
        if (response.status < 300) {
            return response.data;
        }
    } catch (err) {
        console.log(err);
        return [];
    }
}


export default Data;
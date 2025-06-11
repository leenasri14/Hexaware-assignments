import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ViewDoctors = () => {
    const [doctors, setDoctors] = useState([]);

    useEffect(() => {
        const fetchDoctors = async () => {
            const response = await axios.get('http://127.0.0.1:8000/doctorsdetail/');
            setDoctors(response.data);
        };
        fetchDoctors();
    }, []);

    return (
        <div>
            <h2>Doctors List</h2>
            <ul>
                {doctors.map((doctor) => (
                    <li key={doctor.id}>
                        ID: {doctor.id}, Name: {doctor.name}, Specialization: {doctor.specialization}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ViewDoctors;

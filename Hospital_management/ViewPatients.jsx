import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ViewPatients = () => {
    const [patients, setPatients] = useState([]);

    useEffect(() => {
        const fetchPatients = async () => {
            const response = await axios.get('http://127.0.0.1:8000/patients/');
            setPatients(response.data);
        };
        fetchPatients();
    }, []);

    return (
        <div>
            <h2>Patients List</h2>
            <ul>
                {patients.map((patient) => (
                    <li key={patient.id}>
                        ID: {patient.id}, Name: {patient.name}, Age: {patient.age}, Diagnosis: {patient.diagnosis}, Admission Date: {patient.admission_date}, Doctor ID: {patient.doctor.id}, Doctor Name: {patient.doctor.name}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ViewPatients;

import React, { useState } from 'react';
import axios from 'axios';

const AddPatient = () => {
    const [name, setName] = useState('');
    const [age, setAge] = useState('');
    const [diagnosis, setDiagnosis] = useState('');
    const [admissionDate, setAdmissionDate] = useState('');
    const [doctorId, setDoctorId] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://127.0.0.1:8000/patients/', {
                name,
                age: parseInt(age),
                diagnosis,
                admission_date: admissionDate,
                doctor_id: parseInt(doctorId),
            });
            alert(`Patient added with ID: ${response.data.id}`);
        } catch (error) {
            alert('Error adding patient: ' + error.response.data.detail);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Add Patient</h2>
            <input
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Age"
                value={age}
                onChange={(e) => setAge(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Diagnosis"
                value={diagnosis}
                onChange={(e) => setDiagnosis(e.target.value)}
                required
            />
            <input
                type="date"
                placeholder="Admission Date"
                value={admissionDate}
                onChange={(e) => setAdmissionDate(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Doctor ID"
                value={doctorId}
                onChange={(e) => setDoctorId(e.target.value)}
                required
            />
            <button type="submit">Add Patient</button>
        </form>
    );
};

export default AddPatient;

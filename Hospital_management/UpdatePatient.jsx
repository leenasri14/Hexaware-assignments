import React, { useState } from 'react';
import axios from 'axios';

const UpdatePatient = () => {
    const [patientId, setPatientId] = useState('');
    const [name, setName] = useState('');
    const [age, setAge] = useState('');
    const [diagnosis, setDiagnosis] = useState('');
    const [admissionDate, setAdmissionDate] = useState('');
    const [doctorId, setDoctorId] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.put(`http://127.0.0.1:8000/patients/${patientId}/`, {
                name,
                age: parseInt(age),
                diagnosis,
                admission_date: admissionDate,
                doctor_id: parseInt(doctorId),
            });
            alert('Patient updated successfully');
        } catch (error) {
            alert('Error updating patient: ' + error.response.data.detail);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Update Patient</h2>
            <input
                type="number"
                placeholder="Patient ID"
                value={patientId}
                onChange={(e) => setPatientId(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <input
                type="number"
                placeholder="Age"
                value={age}
                onChange={(e) => setAge(e.target.value)}
            />
            <input
                type="text"
                placeholder="Diagnosis"
                value={diagnosis}
                onChange={(e) => setDiagnosis(e.target.value)}
            />
            <input
                type="date"
                placeholder="Admission Date"
                value={admissionDate}
                onChange={(e) => setAdmissionDate(e.target.value)}
            />
            <input
                type="number"
                placeholder="Doctor ID"
                value={doctorId}
                onChange={(e) => setDoctorId(e.target.value)}
            />
            <button type="submit">Update Patient</button>
        </form>
    );
};

export default UpdatePatient;

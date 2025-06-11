import React, { useState } from 'react';
import axios from 'axios';

const AddDoctor = () => {
    const [name, setName] = useState('');
    const [specialization, setSpecialization] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://127.0.0.1:8000/doctors/', {
                name,
                specialization,
            });
            alert(`Doctor added with ID: ${response.data.id}`);
        } catch (error) {
            alert('Error adding doctor: ' + error.response.data.detail);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Add Doctor</h2>
            <input
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Specialization"
                value={specialization}
                onChange={(e) => setSpecialization(e.target.value)}
                required
            />
            <button type="submit">Add Doctor</button>
        </form>
    );
};

export default AddDoctor;

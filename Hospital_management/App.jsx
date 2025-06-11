import React from 'react';
import AddDoctor from './components/AddDoctor';
import AddPatient from './components/AddPatient';
import ViewDoctors from './components/ViewDoctors';
import ViewPatients from './components/ViewPatients';
import UpdatePatient from './components/UpdatePatient';

const App = () => {
    return (
        <div>
            <h1>Hospital Management System</h1>
            <AddDoctor />
            <AddPatient />
            <ViewDoctors />
            <ViewPatients />
            <UpdatePatient />
        </div>
    );
};

export default App;

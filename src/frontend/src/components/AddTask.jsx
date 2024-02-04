import React, { useState } from 'react'
import axios from 'axios';

function AddTask({ onTaskAdded }) {

    const [task, setTask] = useState('');

    const handleInputChange = (e) => {
        setTask(e.target.value);
    }
    
    const handleSumbit = async (e) => {
        e.preventDefault();

        try{
            const response = await axios.post('http://localhost:8080/api/tasks', {
                task: task
            });
            console.log('task added');
            setTask('');
              // Notify the parent component that a new task has been added
            if (onTaskAdded) {
                onTaskAdded(response.data); // Pass the added task data to the parent component
            }
        } catch(error){
            console.error( 'Error adding task: ', error);
        }
    }
  return (
    <div>
        <form onSubmit={ handleSumbit }>
            <input 
                type="text" 
                value={ task }
                onChange={ handleInputChange }
                placeholder='task'    
            />

            <button type="submit">add</button>
        </form>
    </div>
  )
}

export default AddTask
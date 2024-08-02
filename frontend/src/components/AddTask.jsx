import React, { useState } from 'react'
import axios from 'axios';

function AddTask({ onTaskAdded }) {

    const [task, setTask] = useState('');

    const handleInputChange = (e) => {
        setTask(e.target.value);
    }
    
    const handleSumbit = async (e) => {
        e.preventDefault();
        const BASE_URL = `http://${import.meta.env.VITE_SERVER_IP}:${import.meta.env.VITE_SERVER_PORT}/api/tasks`;
        if (!task.trim()) {  // Check if the task is empty or contains only whitespace
            console.error('Task cannot be empty');
            return;  // Exit the function if the task is empty
        }

        try{
            const response = await axios.post(BASE_URL, {
                description: task
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
                placeholder='add task'    
            />

            <button type="submit">add</button>
        </form>
    </div>
  )
}

export default AddTask
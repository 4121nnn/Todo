import React, { useEffect, useState } from 'react'
import axios from 'axios'
import AddTask from './AddTask';

function TaskList() {
    const [tasks, setTasks] = useState([]);
    const BASE_URL = `http://${import.meta.env.VITE_SERVER_IP}:${import.meta.env.VITE_SERVER_PORT}/api/tasks`;

    useEffect(() => {
        const fetchTasks = async () => {
            try{
                const response = await axios.get(BASE_URL);
                setTasks(response.data);

            }catch(error){
                console.error('Error fetching tasks:', error );
            }
        };
        fetchTasks();
    }, []);

    function handleTaskCheck(taskId) {
        // Update the task's completed state in the front-end state
        setTasks((prevTasks) =>
          prevTasks.map((task) =>
            task.id === taskId ? { ...task, completed: !task.completed } : task
          )
        );
      
        // Send a POST request to the backend API to persist the change
        axios
           .post(`${BASE_URL}/${taskId}`)
          .then(() => {
            console.log('Task completion updated successfully!');
          })
          .catch((error) => {
            console.error('Error updating task completion:', error);
          });
      }


    

    
        const buttonClickHandle = (taskId) => {
            // Send a DELETE request to the backend API to delete the task
            axios
                .delete(`${BASE_URL}/${taskId}`)
                .then(() => {
                    console.log('Task deleted successfully!');
                    setTasks((prevTasks) => prevTasks.filter((task) => task.id !== taskId));
                })
                .catch((error) => {
                    console.error('Error deleting task:', error);
                    // Optionally, handle the error by displaying a user-friendly message
                });
        };
       
        const handleTaskAdded = (newTask) => {
            // Update the state with the newly added task
            setTasks((prevTasks) => [...prevTasks, newTask]);
        };

  return (
    <div className='container'>
         <AddTask onTaskAdded={handleTaskAdded} />
        <h4>task list:</h4>
        <ul>
       
            {tasks.map((task) => (
                <li key={task.id}>
                    <label className={task.completed ? 'completed' : ''}>
                        <input type="checkbox"
                            checked={task.completed}
                            onChange={() => handleTaskCheck(task.id)}
                            className={task.completed ? 'checked' : ''}
                        />
                        {task.task}
                        <span className='date'> {task.createdDate} </span>
                    </label>
                    <button onClick={() => buttonClickHandle(task.id)} className="settings-btn other-functions">
                        <i className="fas fa-trash"></i>  
                    </button>
                    
                </li>
                
            ))}
        </ul>

    </div>

  );
}

export default TaskList
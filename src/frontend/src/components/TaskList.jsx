import React, { useEffect, useState } from 'react'
import axios from 'axios'
import TinyPopupMenu from 'tiny-popup-menu';
import AddTask from './AddTask';

function TaskList() {
    const [tasks, setTasks] = useState([]);
    useEffect(() => {
        const fetchTasks = async () => {
            try{
                const response = await axios.get('http://localhost:8080/api/tasks');
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
          .post(`http://localhost:8080/api/tasks/${taskId}`)
          .then(() => {
            console.log('Task completion updated successfully!');
          })
          .catch((error) => {
            console.error('Error updating task completion:', error);
            // Optionally, handle the error by displaying a user-friendly message
          });
      }


    

    
        const buttonClickHandle = (taskId) => {
            // Send a DELETE request to the backend API to delete the task
            axios
                .delete(`http://localhost:8080/api/tasks/${taskId}`)
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
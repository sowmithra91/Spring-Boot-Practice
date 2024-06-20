import React from 'react'
import axios from 'axios';

function Home() {

    const handleApiCall = () => {
        
axios.get('http://localhost:9090/user', {
    withCredentials: true, // Include cookies in the request
  })
  .then(response => {
    console.log(response.data);
  })
  .catch(error => {
    console.log("Errrorrrrr")
    console.log(error)
      window.location.href = 'http://localhost:9090/oauth2/authorization/github';
  });
    };


    return (
        <div>
            <div>Home</div>
            <div>
                <button onClick={handleApiCall}>Login</button>
            </div>
        </div>
    )
}

export default Home
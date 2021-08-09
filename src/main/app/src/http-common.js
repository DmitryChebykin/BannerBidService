import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8084/",
  headers: {
    "Content-type": "application/json"
  }
});
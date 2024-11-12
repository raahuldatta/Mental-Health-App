<template>
  <div class="chat-container">
    <h1>MindMate: Your Mental Health Companion</h1>
    <div class="messages">
      <div v-for="(message, index) in messages" :key="index" :class="getMessageClass(message)">
        <p>{{ message.role }}: {{ message.content }}</p>
      </div>
    </div>
    <div class="input-area">
      <input
        v-model="userMessage"
        type="text"
        placeholder="Type your message here..."
        @keyup.enter="sendMessage"
      />
      <button @click="sendMessage">Send</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      userMessage: "",
      messages: [], // Store the full conversation history
    };
  },
  methods: {
    async sendMessage() {
      if (this.userMessage.trim() !== "") {
        // Add the user's message to the chat history
        this.messages.push({ role: "You", content: this.userMessage });

        try {
          // Prepare the message history for the API request
          const apiMessages = this.messages.map((msg) => {
            return {
              role: msg.role === "You" ? "user" : "assistant",
              content: msg.content,
            };
          });

          // Send the conversation history to the backend
          const response = await axios.post("http://localhost:8080/api/chat", {
            messages: apiMessages, // Send the full message history
          });

          // Add the bot's response to the chat history
          this.messages.push({ role: "Bot", content: response.data.response });
        } catch (error) {
          console.error("Error sending message to backend:", error);
          this.messages.push({ role: "Bot", content: "Sorry, something went wrong." });
        }

        // Clear input field
        this.userMessage = "";
      }
    },
    getMessageClass(message) {
      return message.role === "You" ? "message user" : "message bot";
    },
  },
};
</script>

<style scoped>
.chat-container {
  background-color: cyan;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  border: 2px solid #007bff;
  max-width: 600px;
  width: 100%;
  margin: 20px auto;
}

h1 {
  font-family: 'Arial', sans-serif;
  font-size: 1.8rem;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.messages {
  height: 300px;
  margin-bottom: 20px;
  padding: 10px;
  overflow-y: auto;
  border-radius: 10px;
  background-color: #f7f7f7;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message {
  padding: 10px;
  margin: 5px 0;
  border-radius: 10px;
  max-width: 80%;
  text-align: left;
}

.message.user {
  background-color: black;
  color: white;
  align-self: flex-end;
}

.message.bot {
  background-color: yellow;
  color: black;
  align-self: flex-start;
}

.input-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

input {
  width: 80%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  color: black;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}

.messages p {
  margin: 0;
  word-wrap: break-word;
}

.chat-container {
  display: flex;
  flex-direction: column;
}
</style>

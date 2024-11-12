<template>
  <div id="app">
    <div class="logo-container">
      <img src="@/assets/image.png" alt="MindMate Logo" class="logo" />
    </div>
    <ChatComponent />
  </div>
</template>

<script>
import ChatComponent from "./components/ChatComponent.vue";

export default {
  name: "App",
  components: {
    ChatComponent,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin-top: 20px;
  background: url('@/assets/background.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  flex-direction: column; 
}

.logo-container {
  margin-bottom: 20px;
}

.logo {
  width: 150px;
  height: auto;
  border-radius: 50%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>

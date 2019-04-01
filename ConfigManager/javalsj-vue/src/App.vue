<template>
  <div class="all">
    <div id="app">
      <Home></Home>

    </div>
    <!-- <LoginForm/> -->
    <!-- <ManagerHome></ManagerHome> -->
  </div>
</template>

<script>
import ManagerHome from './components/ManagerHome'
import Home from './components/Home'

export default {
  name: 'App',
  components: {
    'ManagerHome':ManagerHome,
    'Home':Home
  }
}
</script>

<style>
 
  

  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    /* text-align: center; */
    color: #2c3e50;
    /* margin-top: 60px; */
    /* margin-left: 100px ; */
    width:1500px;
    height:457px;
    MARGIN-RIGHT: auto; 
    MARGIN-LEFT: auto;
    background: #f8f8f9;
    /* border:1px solid; */
    /* border-radius:25px; */
  }
  .all{
    width: 100%;
    
  }
 
</style>

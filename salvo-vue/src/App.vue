<template>
  <div id='app' class='container'>
    <router-view/>
    <Footer />
    <Loading />
    <PopUp />
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import Loading from '@/components/Loading.vue';
import PopUp from '@/components/PopUp.vue';

export default {
  name: 'App',
  components: {
    Footer,
    Loading,
    PopUp
  }
};
</script>

<style>
:root {
  --layer-background: 0;
  --layer-middle: 1;
  --layer-foreground: 2;
  --layer-ui: 4;
  --layer-popup: 5;
  --spacer-height: 10vh;

  --cell-size: 1em;

  --color-01: whitesmoke;
  --color-02: lightSkyBlue;
  --color-03: dodgerBlue;
  --background-app: linear-gradient(to bottom left, var(--color-02), var(--color-03));
}

html {
  box-sizing: border-box;
  width: 100vw;
  height: 100vh;
  background-image: var(--background-app);
}

body, .container {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
}

html, body {
  position: absolute;
  right: 0;
  left: 0;
  top: 0;
  bottom: 0;
  margin: 0;
  padding: 0;
  overflow-x: hidden !important;
  
  z-index: var(--layer-background);
}

.view {
  box-sizing: border-box;
  position: relative;
  right: 0;
  top: 0;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;

  z-index: var(--layer-middle);
}

li {
  list-style-type: none;
}

.container-intro {
  position: relative;
  width: 100%;
  height: 25%;
}

.container-body {
  position: relative;
  width: 100%;
  height: 75%;
}

.container-half {
  box-sizing: border-box;
  position: relative;
  width: 50%;
  height: 50%;
}

.content-left { 
  position: relative;
  top: 0;
  right: 0;
}
</style>
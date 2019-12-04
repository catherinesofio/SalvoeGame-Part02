<template>
  <div id='app' class='container'>
    <BackgroundPattern />
    <Footer />
    <router-view/>
    <!--<Loading />
    <PopUp />-->
  </div>
</template>

<script>
import BackgroundPattern from '@/components/BackgroundPattern.vue';
import Footer from '@/components/Footer.vue';
import Loading from '@/components/Loading.vue';
import PopUp from '@/components/PopUp.vue';

export default {
  name: 'App',
  components: {
    BackgroundPattern,
    Footer,
    Loading,
    PopUp
  }
};
</script>

<style>
:root {
  /* BACKGROUND: 0-4 */
  --layer-background: 0;
  --layer-background-pattern: 1;
  /* MIDDLE: 5-9 */
  --layer-middle: 5;
  --layer-middle-footer: 9;
  /* FOREGROUND: 10-14 */
  --layer-foreground: 10;
  /* NAV: 15-19 */
  --layer-ui: 15;
  /* POPUPS: 20-24 */
  --layer-popup: 20;

  --spacer-height: 10vh;

  --cell-size: 1em;

  --margin: 0.5em;
  --padding: 1.5em;
  --border-radius: 1em;

  --text-size-01: 2em;
  --text-size-02: 1.5em;
  --text-size-03: 1em;

  --color-font: #ffffff;
  /*--color-font: #EEF1E6;*/
  --color-00: #fff8ab;
  --color-01: #fff9aa;
  --color-02: #ffd5B8;
  --color-03: #FF756D;
  --color-04: #B88BAD;
  --color-05: #ACECD5;
  --color-06: #799FCB;
  --color-07: #85DE77;

  --box-shadow: 0em 0em 1em var(--color-02);

  --border-width: 0.2em;

  --background-app: linear-gradient(to bottom, var(--color-01), var(--color-02));
  --background-panel: linear-gradient(to bottom left, var(--color-00), var(--color-01));
  --background-footer: linear-gradient(to bottom right, var(--color-03), var(--color-02));

  --background-pattern: url('../public/assets/pattern-paw.png');
}

html {
  box-sizing: border-box;
  width: 100vw;
  height: 100vh;
  color: var(--color-font);
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
}

li {
  list-style-type: none;
}

p {
  font-size: var(--text-size-03);
}

a {
  text-decoration: none;
  color: var(--color-05);
}

button {
  margin-right: var(--margin);
  padding: calc(var(--padding) / 2);
  text-transform: uppercase;
  font-weight: bold;
  color: var(--color-font);
  background-color: var(--color-05);
  border-radius: var(--border-radius);
  border-width: calc(var(--border-width) / 2);
  border-color: var(--color-05);
  border-style: solid;
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

/*.container-intro {
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
}*/
</style>
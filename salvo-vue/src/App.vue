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
@import url('https://fonts.googleapis.com/css?family=Calistoga&display=swap');

@import url('https://fonts.googleapis.com/css?family=Solway&display=swap');

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

  --color-00: #ffffff;
  --color-01: #fff8ab;
  --color-02: #fff9aa;
  --color-04: #ffd5B8;
  --color-05: #FF756D;
  --color-06: #E0BBE4;
  --color-07: #B88BAD;
  --color-08: #ACECD5;
  --color-09: #799FCB;
  --color-10: #85DE77;
  --color-11: #77DD77;

  --box-shadow: 0em 0em 1em var(--color-4);

  --border-width: 0.2em;

  --background-app: linear-gradient(to bottom, var(--color-02), var(--color-04));
  --background-panel: linear-gradient(to bottom left, var(--color-01), var(--color-02));
  --background-footer: linear-gradient(to bottom right, var(--color-05), var(--color-04));

  --background-pattern: url('../public/assets/pattern-paw.png');
}

html {
  box-sizing: border-box;
  width: 100vw;
  height: 100vh;
  color: var(--color-00);
}

html * {
  font-family: solway;
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
  color: var(--color-08);
}

button {
  margin-right: var(--margin);
  padding: calc(var(--padding) / 2);
  text-transform: uppercase;
  font-weight: bold;
  color: var(--color-00);
  background-color: var(--color-08);
  border-radius: var(--border-radius);
  border-width: calc(var(--border-width) / 2);
  border-color: var(--color-00);
  border-style: solid;
  outline: none;
}

button:active {
  background-color: var(--color-09);
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

  z-index: var(--layer-foreground);
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
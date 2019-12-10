<template>
  <div id='app' class='container'>
    <BackgroundPattern />
    <Footer />
    <router-view/>
    <Loading />
    <PopUp />
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
  --layer-middle-nav: 5;
  --layer-middle: 6;
  --layer-middle-footer: 9;
  /* FOREGROUND: 10-14 */
  --layer-foreground: 10;
  --layer-foreground-cells: 11;
  --layer-foreground-gizmo: 12;
  --layer-foreground-ships: 13;
  --layer-foreground-btn: 14;
  /* NAV: 15-19 */
  --layer-ui: 15;
  /* POPUPS: 20-24 */
  --layer-notification: 20;
  --layer-log: 21;
  --layer-popup: 22;

  --spacer-height: 10vh;

  --cell-size: calc(100vw / 15);
  --gizmo-width: 1;
  --gizmo-height: 1;

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
  overflow: hidden;
  scroll-behavior: smooth;
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

button, .btn {
  margin-right: var(--margin);
  padding: calc(var(--padding) / 2);
  text-transform: uppercase;
  font-weight: bold;
  color: var(--color-00);
  background-color: var(--color-08);
  border-radius: var(--border-radius);
  border-width: calc(var(--border-width) / 1.5);
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

  z-index: var(--layer-middle);
}

.view-panel {
  margin: calc(var(--margin) * 2);
  margin-top: 0;
  padding: var(--padding);
  height: 65vh;
  background-color: var(--color-04);
  overflow: hidden;
  overflow-y: scroll;
  border-bottom-left-radius: var(--border-radius);
  border-bottom-right-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  border-style: solid;
  border-color: var(--color-00);
  border-width: var(--border-width);
  border-top-width: 0;
}

.view-panel h1 {
  width: 100%;
  margin: 0;
  padding: 0;
  font-weight: bold;
  font-size: var(--text-size-01);
}

.fixed {
  position: sticky;
  bottom: 0;
}

.td-join {
  display: flex;
  align-items: center;
}

.custom-table, #leaderboards {
  box-sizing: border-box;
  width: calc(100vw - (var(--margin) * 11));
  margin: 0;
  margin-top: var(--margin);
  margin-bottom: var(--margin);
  text-align: center;
  border-spacing: 0;
}

.custom-table th, .custom-table td {
  margin: 0;
  padding: calc(var(--padding) / 2);
}

.custom-table th {
  background-color: var(--color-08);
  border-width: 0;
  border-top-width: calc(var(--border-width) / 2);
  border-bottom-width: calc(var(--border-width) / 2);
  border-color: var(--color-00);
  border-style: solid;
}

.custom-table td {
  background-color: var(--color-06);
  border-width: 0;
  border-bottom-width: calc(var(--border-width) / 2);
  border-color: var(--color-00);
  border-style: solid;
}

.custom-table tbody tr td:first-child, .custom-table thead th:first-child {
  border-left-width: calc(var(--border-width) / 2);
  border-color: var(--color-00);
  border-style: solid;
}

.custom-table tbody tr td:last-child, .custom-table thead th:last-child {
  border-right-width: calc(var(--border-width) / 2);
  border-color: var(--color-00);
  border-style: solid;
}

.custom-table thead th:first-child {
  border-top-left-radius: var(--border-radius);
}

.custom-table thead th:last-child {
  border-top-right-radius: var(--border-radius);
}

.custom-table tbody tr:last-child td:first-child {
  border-bottom-left-radius: var(--border-radius);
}

.custom-table tbody tr:last-child td:last-child {
  border-bottom-right-radius: var(--border-radius);
}

#leaderboards td {
  padding: calc(var(--padding) / 2);
}

#leadearboads tbody tr:nth-child(1) td {
  background-color: var(--color-11);
  padding: 0;
  padding: calc(var(--padding) / 0.75);
}

#leadearboads tbody tr:nth-child(2) td {
  background-color: var(--color-02);
  padding: 0;
  padding: calc(var(--padding) / 1);
}

#leadearboads tbody tr:nth-child(3) td {
  background-color: var(--color-05);
  padding: 0;
  padding: calc(var(--padding) / 1.25);
}
</style>
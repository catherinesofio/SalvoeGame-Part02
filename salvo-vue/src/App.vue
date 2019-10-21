<template>
  <div id='app'>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
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
}

li {
  list-style-type: none;
}
</style>
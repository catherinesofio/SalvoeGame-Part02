<template>
    <div id='background'>
        <div id='background-pattern'></div>
    </div>    
</template>

<style>
:root {
    --animation-speed: 3s;
    --background-size: 5em;
}

#background, #background-pattern {
    box-sizing: border-box;
    position: absolute;
    width: 100%;
    height: 100%;
}   

#background {
    background-image: var(--background-app);
    z-index: var(--layer-background);
}

#background-pattern {
    background-image: var(--background-pattern);
    background-repeat: repeat;
    background-size: var(--background-size);
    mix-blend-mode: soft-light;
    z-index: var(--layer-background-pattern);
    -webkit-animation: slide var(--animation-speed) linear infinite;
    animation: slide var(--animation-speed) linear infinite;
}

@-webkit-keyframes slide {
    from { background-position: 0em 0em; }
    to { background-position: var(--background-size) calc(var(--background-size) * -1); }
}
</style>
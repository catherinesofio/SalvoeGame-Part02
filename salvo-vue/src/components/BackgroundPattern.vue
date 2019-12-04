<template>
    <div id='background'>
        <div id='background-pattern'></div>
    </div>    
</template>

<style>
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
    background-size: 5em;
    mix-blend-mode: soft-light;
    z-index: var(--layer-background-pattern);
}
</style>
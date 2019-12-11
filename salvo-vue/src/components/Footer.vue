<template>
    <footer>
        <p>MADE BY <a href='https://www.linkedin.com/in/catalina-sofio-avogadro/'>CATHERINE SOFIO</a>
        <br>
        <span>Music by <a href='https://www.youtube.com/watch?v=uM8snXJnkD0'>Franco Calabro</a></span></p>
    </footer>
</template>

<style>
footer, footer * {
    z-index: var(--layer-middle-footer);
}

footer {
    position: absolute;
    bottom: 0;
    width: 100vw;
    font-weight: bold;
    text-align: center;
    background-color: var(--color-05);
    box-shadow: var(--box-shadow);
    overflow: hidden;
}

footer span {
    margin: 0;
    padding: 0;
    font-size: calc(var(--text-size-03) / 1.25);
}
</style>
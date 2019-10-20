<template>
    <nav>
        <NavTop :user="user" />
        <NavBottom />
    </nav>
</template>

<script>
import NavTop from "@/components/NavTop.vue";
import NavBottom from "@/components/NavBottom.vue";

export default {
    props: ['user'],
    components: {
        NavTop,
        NavBottom
    }
}
</script>
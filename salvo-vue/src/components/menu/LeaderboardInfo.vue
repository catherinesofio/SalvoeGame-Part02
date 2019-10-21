<template>
    <tr>
        <td>{{ this.position }}</td>
        <td>{{ this.username }}</td>
        <td>{{ this.points }}</td>
    </tr>
</template>

<script>
export default {
    props: ['position', 'username', 'points']
};
</script>
<template>
<div>

    <div v-if="config.nodeType==='title'" class="title">
        <h4>[{{config.title}}]</h4>
    </div>
    <div v-else class="property">
        <p>{{config.title}}=<span class="value">{{config.nodeContent}}</span></p>
    </div>
</div>
</template>

<script>
export default {
    name:"previewComponent",
    data(){
        return{

        }
    },
    methods:{

    },
    props:["config"]
}
</script>

<style scoped>
    .title{
        color: blue;
        font-size: 20px;
    }
    .property{
        font-size: 20px;
    }
    .value{
        color: green;
    }
</style>

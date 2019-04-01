<template>
    <div>
        <previewComponent v-for="item in configData" :key="item.name" :config="item">
            {{item}}
        </previewComponent>
    </div>
</template>

<script>
import previewComponent from '../../components/preview/ConfigPreviewComponent'
export default {
    name:'configPreview',
    data(){
        return{
            configData:[]
        }
    },methods:{
        
    },

    components:{
        'previewComponent':previewComponent,
    }
}
</script>

<style>

</style>

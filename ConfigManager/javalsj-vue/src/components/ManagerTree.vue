<template>
<div class="treeCom">
    <Tree :data="data1" @on-select-change="nodeChange"></Tree>
</div>
</template>
<script>
      export default {
        name:"managerTree",
        data () {
            return {
                data1: []
            }
        },
        mounted:function(){
            this.getTreeInfo();
        },methods:{
            getTreeInfo(t){
                this.$http.get("http://127.0.0.1:8011/allNodes")
                .then((res)=>{
                    console.log(res)
                    if(this.data1.length<1){
                        this.data1.push(res.data);
                    }else{
                        this.data1.splice(0,1,res.data);
                    }
                })
            },
            nodeChange(n){
                this.$emit("transferCurrent",n)
            }
        }
    }
</script>
<style scoped>
    .treeCom{
        width: 250px;
    }
</style>

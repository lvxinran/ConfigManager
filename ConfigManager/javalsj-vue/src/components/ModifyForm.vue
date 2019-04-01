<template>
    <div class="allForm">
        <Form :model="currentNode" label-position="left"  :label-width="150" class="formStyle">
            <FormItem label="当前节点路径:"> 
                <Input v-model="currentNode.path" readonly  />
            </FormItem>
            <FormItem label="当前节点子节点个数:">
                <Input v-model="currentNode.childrenCount" readonly  />
            </FormItem>
            <FormItem label="当前节点种类:">
                <Input v-model="currentNode.nodeTypeTitle" readonly id="modifyNodeInfo"/>
            </FormItem>
            <FormItem label="当前节点标题:">
                <Input v-model="currentNode.title" readonly id="modifyNodeInfo"/>
            </FormItem>
            <FormItem label="属性值:" v-if="currentNode.nodeType==='property'" readonly>
                <Input v-model="currentNode.nodeContent" id="addNodeInfo" readonly />
            </FormItem>
        </Form>
    </div>
</template>
<script>
export default {
    name:'ModifyForm',
    data(){
        return{
            show :true,
           
            /**
             * 添加=0;修改=1
             */
            operatorType:0,
            addNode:'',
            oldTitle:'',
            nodeType:'',
            nodeContent:''
        }
    },
    props:["currentNode","addInfo"],
    
    methods: {

    }

}
</script>
<style scoped>
    .allForm{
        /* width: 500px; */
        height: 500px;
        min-width: 500px;
    }
    .formStyle{
        margin-top: 20px;
    }
    .submit{
        margin-left: 50px;
        margin-top: 50px;
    }
    Input{
        width: 50px;
    }
</style>

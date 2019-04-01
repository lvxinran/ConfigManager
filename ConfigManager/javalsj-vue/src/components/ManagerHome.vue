<template>
    <div class="home">
        <!-- <h1 class="configCenterTitle">JJ统一配置中心</h1> -->
        <div class="operateNode">
            <Button type="info" icon="ios-add" disabled id="addNodeButton" @click="addToTree">添加节点</Button>
            <Button type="success" icon="ios-build-outline" disabled id="modifyNodeButton" @click="modifyNode">修改节点</Button>
            <Button type="error" icon="ios-close-circle-outline" disabled id="removeNodeButton" @click="removeNode">移除节点</Button>
            <Button type="info" icon="ios-list-box-outline" id="previewNodeButton" disabled @click="previewConfig">文件预览</Button>
            <Upload ref="upload"
            :before-upload="handleUpload"
            :format="['ini','txt']"
            :show-upload-list="false"
            :data="currentNode"
            action="http://localhost:8011/upload" 
            class="uploadButton">
                <Button icon="ios-cloud-upload-outline" disabled id="uploadButton">上传文件</Button>
            </Upload>
                <Progress :percent="progress" status="active" v-show="progress!=0" class="progress"></Progress>
            <div  v-if="file !== null" class="uploadInfo">上传文件: {{ file.name }} <Button type="text" @click="upload" :loading="loadingStatus" >{{ loadingStatus ? 'Uploading' : '点击上传' }}</Button></div>
        </div>
        <div class="floatclear"></div>
    <Modal
        v-model="removeModal"
        title="移除节点"
        @on-ok="ok"
        @on-cancel="cancel">
        <p>是否要删除{{currentNode.title}}及下属节点？</p>
    </Modal>
    <Modal v-model="addModal" draggable scrollable title="添加节点" @on-ok="handleAddSubmit()">
        <Form :model="currentNode" label-position="left"  :label-width="80" class="formStyle">
        <FormItem label="添加节点:">
                <Input v-model="addInfo.nodeTitle" id="addNodeInfo" placeholder="请输入添加节点名称"/>
            </FormItem>
            <FormItem label="节点种类:">
                <Select v-model="addInfo.nodeType" style="width:200px" ref="nodeTypeInput">
                    <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
            </FormItem>
            <FormItem label="属性值:" v-if="addInfo.nodeType==='property'" >
                <Input v-model="addInfo.nodeContent" id="addNodeInfo"  placeholder="请输入属性值"/>
            </FormItem>
        </Form>
    </Modal>
    <Modal v-model="modifyModal" draggable scrollable title="修改节点" @on-ok="handleModifySubmit()">
        <Form :model="currentNode" label-position="left"  :label-width="80" class="formStyle">
        <FormItem label="节点标题:">
                <Input v-model="modifyInfo.nodeTitle" id="addNodeInfo" placeholder="请输入添加节点名称"/>
            </FormItem>
            <FormItem label="节点种类:">
                <Select v-model="modifyInfo.nodeType" style="width:200px" ref="nodeTypeInput" disabled>
                    <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
            </FormItem>
            <FormItem label="属性值:" v-if="modifyInfo.nodeType==='property'" >
                <Input v-model="modifyInfo.nodeContent" id="addNodeInfo"  placeholder="请输入属性值"/>
            </FormItem>
        </Form>
    </Modal>
    <Modal v-model="previewModal" scrollable title="文件预览">
        
            <Card :bordered="false">
                <p slot="title">{{currentNode.title}}</p>
                <Scroll height = "600">
                    <configPreview ref="preview"></configPreview>
              </Scroll>
            </Card>
      
        
    </Modal>
    <div class="tree_view">
        <Scroll class="tree_scroll" height ="700">
            <managerTree class="treeComponent" @transferCurrent="setCurrent" ref="nodeTree"/>
        </Scroll>
    </div>
        <modifyForm class="formComponent" 
            :currentNode="currentNode" 
            :addInfo="addInfo" 
            @refreshNodeTree="refreshTree" ref="operForm" />
    </div>
</template>
<script>
import managerTree from '../components/ManagerTree'
import modifyForm from "../components/ModifyForm"
import configPreview from '../components/preview/ConfigPreview'
import { setTimeout } from 'timers';
import { constants } from 'fs';
export default {
    name:"ManagerHome",

    data(){
        return{
            previewModal:false,
            addModal:false,
            modifyModal:false,
            removeModal: false,
            file: null,
            loadingStatus: false,
            currentNode:{
                title:'',
                path:'',
                childrenCount:'',
                nodeType:'',
                nodeTypeTitle:'',
                nodeContent:''
            },
             cityList: [
                    {
                        value: 'dir',
                        label: '文件夹'
                    },
                    {
                        value: 'file',
                        label: '配置文件'
                    },
                    {
                        value: 'title',
                        label: '文件内标题'
                    },
                    {
                        value: 'property',
                        label: '配置文件属性'
                    }
                ],
            addInfo:{
                nodeTitle:'',
                nodeType:'',
                nodeContent:''
            },
            modifyInfo:{
                nodeTitle:'',
                nodeType:'',
                nodeContent:''
            },
            progress:0
                // nodeType:'',
                // nodeContent:''
            

        }
    },
    methods:{
        handleReachBottom(){

        },
        previewConfig(){
            this.$http.get('http://127.0.0.1:8011/showPreview',{params:{'path':this.currentNode.path}})
            .then((res)=>{
                this.$refs.preview.configData = res.data;
            })
            this.previewModal = true;
        },
        ok () {
            var _this = this;
            this.$http.delete('http://127.0.0.1:8011/node',{params:{'path':this.currentNode.path}})
            .then((res)=>{
                if(res.data){
                    _this.currentNode={
                        title:'',
                        path:'',
                        childrenCount:'',
                        nodeType:'',
                        nodeTypeTitle:'',
                        nodeContent:''
                    };
                    this.refreshTree();
                }
            })
        },
        cancel () {
        },
        setCurrent(n){
            if(n.length>0){
                // if(n[0].content!=null){
                //     this.content = n[0].content;
                // }
                this.setButtonStatu(true);
                this.currentNode = n[0];
                switch(this.currentNode.nodeType){
                    case 'dir': this.currentNode.nodeTypeTitle = '文件夹';
                        break;
                    case 'file':this.currentNode.nodeTypeTitle = '配置文件';
                    break;
                    case 'title':this.currentNode.nodeTypeTitle = '文件内标题';
                    break;
                    case 'property':this.currentNode.nodeTypeTitle = '配置文件属性';
                    break;
                    default:break;
                };
                if(n[0].children!=null){
                        this.currentNode.childrenCount = n[0].children.length;
                }else{
                    if(this.currentNode.nodeType==='property'){
                    var addNodeButton = document.getElementById("addNodeButton");
                    addNodeButton.setAttribute("disabled","disabled")

                    }
                
                this.currentNode.childrenCount = 0;
                }
            }else{
                this.setButtonStatu(false);
                this.currentNode={
                    title:'',
                    path:'',
                    childrenCount:'',
                    nodeTypeTitle:''
                }
            }
            
        },
        handleAddSubmit(){
            var _this = this;
            this.$http.post("http://127.0.0.1:8011/node",
            {parentNodePath:this.currentNode.path,
                     currentTitle:this.addInfo.nodeTitle,
                     nodeType:this.addInfo.nodeType,
                     content:this.addInfo.nodeContent
             },
      {emulateJSON: true})
            .then(
                (res)=>{
                    if(res.data){
                        this.addInfo={
                            nodeTitle:'',
                            nodeType:'',
                            nodeContent:''
                        }
                        _this.refreshTree();
                        this.$Message.success('添加成功！')
                    }else{
                        this.$Message.error('此节点已存在，添加失败！');
                    }
                }
            )   
        },
        handleModifySubmit(){
            var _this = this;
            var modifyObject={
                'nodePath':this.currentNode.path,
                'oldTitle':this.currentNode.title,
                'modifyTitle':this.modifyInfo.nodeTitle,
                'content':this.modifyInfo.nodeContent
            }
            this.$http.put("http://127.0.0.1:8011/node",
                modifyObject)
            .then((res)=>{
                if(res.data){   
                        _this.refreshTree();
                    }
            })
        },
        setButtonStatu(able){
            var button1 = document.getElementById("addNodeButton");
            var button2 = document.getElementById("modifyNodeButton");
            var buttonRemove = document.getElementById("removeNodeButton");

            if(able){
                button1.removeAttribute("disabled");
                button2.removeAttribute("disabled");
                buttonRemove.removeAttribute("disabled");

            }else{
                buttonRemove.setAttribute("disabled","disabled");
                button1.setAttribute("disabled","disabled");
                button2.setAttribute("disabled","disabled");
            }
        },
        modifyNode(){
            this.modifyModal = true;
            this.modifyInfo={
                nodeTitle:this.currentNode.title,
                nodeType:this.currentNode.nodeType,
                nodeContent:this.currentNode.nodeContent
            }

        },
        addToTree(){
            this.addModal = true;
        },
        refreshTree(){
            this.$refs.nodeTree.getTreeInfo();
            this.setButtonStatu(true)
        },
        removeNode(){
            this.removeModal=true;
        },
        handleUpload (file) {
            this.file = file;
            return false;
        },
        upload () {
            var _this = this;
            this.$http.get("http://127.0.0.1:8011/upLoadCheck",{params:{
                'path':this.currentNode.path,'title':this.file.name
            }}).then((res)=>{
                if(res.data){
                    this.$Message.error('此文件已存在，上传失败！')
                    _this.file = null;
                    _this.loadingStatus = false;
                    return;
                }
            })

            this.loadingStatus = true;
            this.progress=0;
            this.$refs.upload.post(this.file);

            var interval = window.setInterval(() => {
                if(_this.progress===100){
                    _this.file = null;
                    _this.loadingStatus = false;
                    _this.refreshTree();
                    window.clearInterval(interval)
                }
                _this.$http.get("http://127.0.0.1:8011/uploadProcess").then((res)=>{
                _this.progress = Math.round(res.data);
            })}, 500)
        },
    },
    components: {
    'managerTree':managerTree,
    'modifyForm':modifyForm,
    'configPreview':configPreview
  },watch:{
     currentNode(val){
         if(val.nodeType==="file"){
            document.getElementById("previewNodeButton").removeAttribute("disabled");
         }else{
            document.getElementById("previewNodeButton").setAttribute("disabled","disabled");

         }
         if(val.nodeType==="dir"){
            document.getElementById("uploadButton").removeAttribute("disabled");
         }else{
            document.getElementById("uploadButton").setAttribute("disabled","disabled");

         }
     },
     progress(val){
         var _this = this;
         if(val===100){
            setTimeout(function(){
                _this.progress=0;
            },5000)
         }
     }
  }
}
</script>
<style scoped>
    /* .floatclear{
        clear: both;
    } */
    .treeComponent{ 
        /* border: 1px solid; */
        /* margin-top: 50px; */
        float: left;
        width: 300px;
    }
    .formComponent{
        margin-top: 50px;
        margin-left: 150px;
        float: left;
        /* border: 1px solid; */
    }
    .configCenterTitle{ 
        /* border: 1px solid; */    
        width: 50%;
        float: left;    
    }
    .operateNode{   
        width:60%;
        /* border: 1px solid; */
        float: right;
    }   
    .uploadButton{  
        margin-right: 5px;
        float: left;
    }
    .progress{
        width: 300px;
    }
    .tree_view{
        height: 100%;
        width: 300px;
        float: left;
    }
    .home{
        height: 100%;
    }
</style>    

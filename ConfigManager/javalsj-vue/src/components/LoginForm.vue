<template>
    <div class="config_operation">
        <Button class="button" @click="viewConfig()">查询所有配置文件信息</Button>
        <!-- <Button class="button" @click="volidate()">更改配置文件</Button> -->
        <!-- <Button type="error" class="button" >DELETE</Button> -->
        <ConfigInfo @viewConfig='viewConfig' v-if='infoShow' :msg="configs" @transferConfig='transferConfig'></ConfigInfo>
        <ModifyForm :currentConfig="currentConfig" @aftermodify="viewConfig"></ModifyForm>
    </div>
</template>
<script>
import ConfigInfo from '../components/ConfigInfo'
import ModifyForm from '../components/ModifyForm'
export default {
  name: 'LoginForm',
  data () {
    return { 
      configs:[],
      infoShow :true,
      currentConfig:{
        userNm:'',
        userPw:'',
        path:'',
        nodePriority:0
      }
    }
  },
  methods: {
    viewConfig () {
      console.log('查询配置')
      this.$http.get('http://127.0.0.1:8000/viewAllConfig')
        .then((res) => {
          this.configs = res.data;
        })
    },
    transferConfig(row){
      this.currentConfig={
        userNm:row.userNm,
        userPw:row.userPw,
        path:row.path,
        nodePriority:row.nodePriority
      }
    },
    volidate(){
      console.log(this.configs)
    }
  },
  components:{
    'ConfigInfo':ConfigInfo,
    'ModifyForm':ModifyForm
  }
}
</script>
<style scoped>
    .button {
        margin-top: 10px;
        height: 50px;
        width: 50%;
    }
    .config_operation {
        margin:0 auto;
        margin-top: 50px;
        height: 1000px;
        width: 800px;

    }
</style>

<template>
    <div>
        <h1>此为配置详情</h1>
        <Table :row-class-name="rowClassName" border :columns="columns1" :data="msg"></Table>
    </div>
</template>
<script>
export default {
    name:'ConfigInfo',
    data(){
        return{
            columns1: [
                
                        {
                                    title: 'path',
                                    key: 'path'
                        },
                        {
                                    title: 'userNm',
                                    key: 'userNm'
                        },
                        {
                                    title: 'userPw',
                                    key: 'userPw'
                        },
                        {
                            title:'nodePriority',
                            key:'nodePriority'
                        },{
                        title: 'Action',
                        key: 'action',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {   
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.modify(params.row)
                                        }
                                    }
                                }, '更改此节点下所有配置'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.row)
                                        }
                                    }
                                }, '删除此节点下所有配置')
                            ]);
                        }
                    }
                
                ],
        }
    },
    props:['msg'],
     methods: {
            rowClassName (row) {
                if (row.nodePriority === 2) {
                    return 'demo-table-info-row';
                } else if (row.nodePriority === 3) {
                    return 'demo-table-error-row';
                }
                return '';
            },
            modify (row) {
                this.$emit('transferConfig',row)
            },
            remove (row) {
                this.$http.get("http://127.0.0.1:8000/deleteOneConfig",{params:{'path':row.path}})
                .then((res) => {
                    if(res.data){
                        this.success(false);
                    }else{
                        this.error(false);  
                    }
                    this.$emit('viewConfig');
                })
                                // this.msg.splice(index, 1);
            },
            success (nodesc) {
                this.$Notice.success({
                    title: '操作成功',
                    desc: nodesc ? '' : 'Here is the notification description. Here is the notification description. '
                });
            },
            error (nodesc) {
                this.$Notice.error({
                    title: '操作失败',
                    desc: nodesc ? '' : 'Here is the notification description. Here is the notification description. '
                });
            }
        }
}
</script>
<style scoped>
    li{
        font-size: 12px;
        color: #657180;
        list-style: none;
    }
    .ivu-table .demo-table-info-row td{
        font-size: 20px;
        font-weight:bold;
    }
    .ivu-table .demo-table-error-row td{
        font-size: 18px;
        font-weight:200;
    }
</style>
    
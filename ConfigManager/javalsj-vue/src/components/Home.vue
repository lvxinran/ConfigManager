<style scoped>
    .layout-con{
        height: 100%;
        width: 100%;
    }
    .menu-item span{
        display: inline-block;
        overflow: hidden;
        width: 69px;
        text-overflow: ellipsis;
        white-space: nowrap;
        vertical-align: bottom;
        transition: width .2s ease .2s;
    }
    .menu-item i{
        transform: translateX(0px);
        transition: font-size .2s ease, transform .2s ease;
        vertical-align: middle;
        font-size: 16px;
    }
    .collapsed-menu span{
        width: 0px;
        transition: width .2s ease;
    }
    .collapsed-menu i{
        transform: translateX(5px);
        transition: font-size .2s ease .2s, transform .2s ease .2s;
        vertical-align: middle;
        font-size: 22px;
    }
</style>
<template>
    <div class="layout">
        <Layout :style="{minHeight: '100vh'}">
            <Sider collapsible :collapsed-width="78" v-model="isCollapsed" :style="{postion:'fixed'}">
                <Menu active-name="Home" theme="dark" width="auto" :class="menuitemClasses" 
                @on-select="select_menu">
                    <MenuItem name="config_center" disabled>
                        <Icon type="ios-navigate"></Icon>
                        <span>配置中心</span>
                    </MenuItem>
                    <MenuItem name="Home">
                        <Icon type="ios-home-outline"></Icon>
                        <span>首页</span>
                    </MenuItem>
                    <MenuItem name="Upload">
                        <Icon type="ios-cloud-upload-outline"></Icon>
                        <span>上传文件</span>
                    </MenuItem>
                </Menu>
            </Sider>
            <Layout>
                <Header :style="{background: '#fff', boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)'}">
                    <h1>JJ统一配置中心</h1>
                </Header>
                <Content :style="{padding: '0 16px 16px'}">
                    <Breadcrumb :style="{margin: '16px 0'}">
                        <BreadcrumbItem>{{currentMenu}}</BreadcrumbItem>
                        <!-- <BreadcrumbItem>Components</BreadcrumbItem>
                        <BreadcrumbItem>Layout</BreadcrumbItem> -->
                    </Breadcrumb>
                    <Card>
                        
                        <div style="height: 700px">
                            <ManagerHome v-show="currentMenu=='Home'"></ManagerHome>
                        </div>
                    </Card>
                </Content>
            </Layout>
        </Layout>
    </div>
</template>


<script>
import ManagerHome from "../components/ManagerHome"
export default {
        name:'Home',
        data () {
            return {
                currentMenu:'Home',
                isCollapsed: false
            };
        },
        methods:{
            select_menu(menu){
                this.currentMenu = menu;
            }
        },
        computed: {
            menuitemClasses: function () {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        components:{
            'ManagerHome':ManagerHome
        }
    }
</script>

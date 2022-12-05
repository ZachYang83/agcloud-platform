/*
* @Author: gdf
* @Date:   2019/3/4
* @Last Modified by:   anchen
* @Last Modified time: $ $
*/
// require(['jquery','vue','ELEMENT','common'], function($,Vue,ELEMENT,common) {
//     Vue.use(ELEMENT);
var util = {};
util.treeTableXcode = function(data, data2){
    for(var i=0;i<data.length;i++){
        var item = data[i];
        if(item.data.funcId==data2.funcId){
            data2.parentFuncId = item.data.parentFuncId;
            return data2;
        }
        if(item.children && item.children.length > 0){
            util.treeTableXcode(item.children,data2);
        }
    }
};
var vm = new Vue({
    el: '#managementPage',
    data: {
        active:'module',
        treeActive: 'user',
        loading: true,  // body loading
        fullHeight: document.documentElement.clientHeight, // 全屏高度
        treeData1: [], // 左侧组织架构树结构数据
        treeData2: [], // 左侧组织架构树结构数据
        treeData3: [], // 左侧组织架构树结构数据
        treeData4: [], // 左侧组织架构树结构数据
        rightItemData: [], // 右侧下级部门列表
        tableData: [], // 右侧应用详细表格
        tableData2: [], // 右侧应用列表
        tableData3: [], // 右侧应用列表
        parentId: '', // 组织架构pId
        id: '',  // 组织架构id
        level:1,
        display:'none',
        expands:[],
        filterText: '', // 左侧树 搜索框value
        filterText1: '', // 左侧树 搜索框value
        searchUserOrgInfo: false,
        leftOrgTree:true,
        treeLeftHeight: document.documentElement.clientHeight - 100, // 左侧树 的高度
        defaultProps: {},
        formData1:{
            funcCode:'',
            funcType:''
        },
        formData2:{
            roleId:'',
            appId:'',
            roleDeleted:0
        },//角色授权表单
        formData3:{

        },//自建应用表单
        addIconAndEdit:true,
        roleAuthor1:true,//角色授权表单
        moduleLabel:'新增模块',
        funcLabel:'新增功能',
        isAdmin:0,
        setPic:false,
        setPic2:false,
        notAllowed:false,
        allowed:false,
        allowed2:false,
        btnTitle:'选中模块新增下级模块，不选中则新增顶级模块',
        pageSize: 10, // 右侧部门人员分页信息
        page: 1, // 右侧部门人员分页信息
        total: 0, // 右侧部门人员分页信息
        pageSize2: 10, // 右侧部门人员分页信息
        page2: 1, // 右侧部门人员分页信息
        total2: 0, // 右侧部门人员分页信息
        pageSize3: 10, // 右侧部门人员分页信息
        page3: 1, // 右侧部门人员分页信息
        total3: 0, // 右侧部门人员分页信息
        showPagination: false, // 右侧部门人员是否显示分页
        treeShowNode: [], // 左侧组织架构树结构展开的节点
        funName: '', // 应用name
        leftSelId: '', // 左侧树右键选中id
        leftSelPid: '', // 左侧树右键选中pid
        leftSelNode: '', // 左侧树右键选中节点
        leftSelData: '', // 左侧树右键选中pid
        multipleSelection: [], // 模块功能选中状态集合
        multipleSelection2: [], // 应用选中状态集合
        multipleSelection3: null, // 角色选中状态集合
        currentRow:'',
        selectedIDs: [], // 部门人员选中状态id数组
        orgType: '',
        leftOrgTree: true,
        sidebarActive: 'baseInfo',
        showRightSlider: false,
        url:'',
        isCloudSoft:'',
        iconCss:false,
        iconCss2:false,
        softCss:false,
        appSoftId:'',
        creatBtn:true,
        setparentBtn:true,
        tmn:true,
        edit:true,
        currentFuncId:'',
        netId:[],//网络netId集合
        filterText:'',
        currentNetId:'',//当前网络netId
        addAndEdit:true,
        setParent:false,
        Soft:false,
        radio2:"",
        addSoft:false,
        items:{},//父级集合
        moOrFunc:'',
        currFuncData:[],//当前编辑应用或模块数据(表格内的编辑按钮获取的数据)
        currTypeData:{},//当前编辑应用或模块数据(选择框选中的数据)
        appData : {},//当前树节点选中的应用数据
        dialogTitie:'新增',
        dialogEditTable:false,
        funcIds:[],//多选模块集合
        funcTypes:[],//多选模块集合
        isTopModule:false,//是否是新增顶级模块
        code:'',//table字段编号
        name:'',//table字段名称
        callType:'',//调用类型
        invokeUrl:'',//访问路径
        funASoft:false,
        funASoftUrl:false,
        isToolBar:false,
        option1:[],//微件类型
        option4:[],//所属终端
        delBtn:true,//删除按钮是否显示
        keyword:"",//查询关键字
        keyword2:"",//查询关键字
        appList:true,//是否显示应用列表
        apps:false,//是否显示应用下的模块等
        curLevel1Id:'',
        isAdd:true,
        isAddSoftBtn:false,
        self:false,//是否显示同步云应用按钮和删除按钮
        synCloudApp:true,
        synApp:false,
        currentType:'',//当前新增类型
        lastNodeLabel:'',//最后一个树节点的label
        num:0,
        node:false,
        local:"",//编辑暂存名称
        SaAbtn:true,//保存并新增按钮
        editSoftStatus:false,//是否编辑应用
        icons1:[],
        icons2:[],
        icons3:[],
        icons4:[],
        isIcon:true,
        editBtn:false,
        rules:{
            funcCode: [
                { required: true, message: '此项必填'}
            ],
            funcName: [
                { required: true, message: '此项必填'}
            ],
            pagePosition: [
                { required: true, message: '此项必填'}
            ],
            widgetType: [
                { required: true, message: '此项必填'}
            ],
            funcInvokeUrl: [
                { required: true, message: '此项必填'}
            ],
            netTmnId: [
                { required: true, message: '此项必填'}
            ],
            isCreateMenu: [
                { required: true, message: '此项必填'}
            ],
            isImgIcon: [
                { required: true, message: '此项必填'}
            ]
        },
        rules2:{
            roleCode: [
                { required: true, message: '此项必填'}
            ],
            roleName: [
                { required: true, message: '此项必填'}
            ],
        },
        rules3:{
            softName: [
                { required: true, message: '此项必填'}
            ],
            softCode: [
                { required: true, message: '此项必填'}
            ],
            softWebContext: [
                { required: true, message: '此项必填'}
            ],
            softInnerUrl: [
                { required: true, message: '此项必填'}
            ],
            protocolHeader: [
                { required: true, message: '此项必填'}
            ],
            isActive: [
                { required: true, message: '此项必选'}
            ],
            isSingleUrl: [
                { required: true, message: '此项必选'}
            ],
            autoSwitch: [
                { required: true, message: '此项必选'}
            ],
            useLoadBalence: [
                { required: true, message: '此项必选'}
            ],
            softStatus: [
                { required: true, message: '此项必选'}
            ],
        }
    },
    mounted: function () {
        var that = this;
        window.addEventListener("resize", function() {
            return (() => {
                window.fullHeight = document.documentElement.clientHeight
            that.fullHeight = window.fullHeight;
            that.treeLeftHeight = that.fullHeight - 100;
        })();
        });
    },
    created: function() {
        this.getNet();
        var _this = this;
        $(document).bind("contextmenu",function(e){
            return false;
        });
        $(document).click(function(e){
            e.stopPropagation();
            var silder = $(".right-sidebar");
            var scrollable = $(".scrollable");
            var btn = $(".btn");
            var dialogType = $(".dialogType");
            if(scrollable.eq(0).is(":hidden")){
                scrollable = scrollable.eq(1);
            }else{
                scrollable = scrollable.eq(0);
            }
            if( !$(e.target).hasClass("right-sidebar") && dialogType.has(e.target).length==0 && silder.has(e.target).length ==0 && e.target.localName!="button" && scrollable.has(e.target).length==0||$(e.target).hasClass("el-table__body-wrapper")){
                _this.showRightSlider = false;
            }
        })
    },
    methods:{

        treeClick:function(item,index){
            if(item.open){
                this.collapse(item,index);
            }else{
                this.expand(item,index);
            }
        },
        expand:function(item,index){
            if(!item.children){
                return index;
            }
            //展开
            for(var i=0;item.children && i<item.children.length;i++){
                var child = item.children[i];
                this.tableData.splice(++index,0,child);
                if(child.children && child.children.length > 0 && child.open){
                    index = this.expand(child,index);
                }
            }
            item.open = true;
            return index;
        },
        collapse:function(item,index){
            if(!item.children){
                return index;
            }
            //收缩
            item.open = false;
            var len = 0;
            for(var i=index+1;i<this.tableData.length;i++){
                var xcode = this.tableData[i].xcode;
                if(xcode.startsWith(item.xcode+"-")){
                    len ++;
                }else{
                    break;
                }
            }
            this.tableData.splice(index+1,len);
        },
        tableRowClassName:function({row, rowIndex}) {
            if (row.level%2==0) {
                return 'treeBg';
            }
            return '';
        },
        tabCilck:function(){
            var _this = this;
            this.getTreeData();
            this.filterText1="";
        },
        getNet:function(){
            var _this =this;
            request('opus-admin',{
                type:'get',
                url:ctx + 'rest/opu/rs/net/opusNet',
                data:{},
            },function (data) {
                _this.netId.push(data[1].netId);
                _this.netId.push(data[0].netId);
                _this.getTreeData();
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
        },
        getTreeData:function(){
            var _this = this;
            this.loading = true;
            if(this.treeActive=="user"){
                this.isAdmin= 0;
                this.currentNetId = this.netId[0];
            }else{
                this.isAdmin= 1;
                this.currentNetId = this.netId[1];
            }
            request('opus-admin',{
                type:'get',
                url:ctx + 'rest/opu/rs/appSoft/tree',
                data:{
                    isAdmin:_this.isAdmin
                },
            },function (data) {
                var arr1 = new Array();
                var arr2 = new Array();
                arr1.push(data[0]);
                arr2.push(data[1]);
                if(data[0].type == "cloudSoft"){
                    _this.treeData1 = arr1;
                    _this.treeData2 = arr2;
                }else{
                    _this.treeData1 = arr2;
                    _this.treeData2 = arr1;
                }
                // 判断最后一个树节点
                if( _this.treeData2[0].children.length==0){
                    _this.lastNodeLabel = _this.treeData2[0].label;
                }else{
                    _this.lastNodeLabel = _this.treeData2[0].children[_this.treeData2[0].children.length-1].label;
                }

                if(!_this.node){
                    var node = {
                        data:{
                            "id":_this.treeData1[0].id
                        }
                    }
                    // 初次加载选中第一个节点
                    _this.$nextTick(function () {
                        _this.clickTreeNode(_this.treeData1[0],node);
                        $(_this.$refs.treeLeft1.$el).children().addClass("is-current")
                    })
                }else{
                    var node = {
                        data:{
                            "id":_this.treeData2[0].id
                        }
                    }
                    // 初次加载选中第一个节点
                    _this.$nextTick(function () {
                        _this.clickTreeNode(_this.treeData2[0],node);
                        $(_this.$refs.treeLeft2.$el).children().addClass("is-current")
                        _this.node=false;
                    })
                }
                setTimeout(function(){
                    _this.$refs.treeLeft1.filter(_this.filterText1);
                    _this.$refs.treeLeft2.filter(_this.filterText1);
                }, 250)
                _this.loading = false;
                _this.getSelectOption();
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
        },
        // 点击云应用或自建应用
        getAppList:function(){
            var _this =this;
            request('opus-admin',{
                type:'get',
                url:this.url,
                data:{
                    isAdmin:this.isAdmin,
                    keyword:this.keyword,
                    isCloudSoft:this.isCloudSoft
                },
            },function (data) {
                _this.tableData2 = data;
                _this.loading= false;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
        },
        // 点击app
        getAppFunc:function(){
            var _this =this;
            this.loading = true;
            request('opus-admin',{
                type:'get',
                url:this.url,
                data:{
                    keyword:this.keyword2,
                    appSoftId : this.appSoftId
                },
            },function (data) {
                _this.tableData=data;
                _this.loading= false;
                _this.$nextTick(function(){
                    var els =$('.el-table__expand-icon')
                    for (var i = 0; i < els.length; i++) {
                        els[i].click()
                    }
                })
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
        },
        getSelectOption:function(){
            var _this =this;
            // 获取终端
            request('opus-admin',{
                type:'get',
                url:ctx + 'rest/opu/rs/net/tmn',
                data:{
                    netId :  this.currentNetId
                },
            },function (data) {
                _this.option4 = data;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
            // 获取微件类型
            request('opus-admin',{
                type:'get',
                url:ctx + 'app/widget/type',
                data:{
                    typeCode:'OPU_RS_WIDGET_TYPE'
                },
            },function (data) {
                _this.option1 = data;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
        },
        roleControl:function(data){ // 树节点点击事件
            var _this = this;
            this.appData = data;
            // this.keyword = '';
            // this.keyword2 = '';
            this.active = "module";
            this.editBtn =true;
            if(this.treeActive == "user"){
                this.funName = data.softName;
            }else{
                this.funName = data.softName;
            }
            this.level = 1;
            this.url = ctx + 'app/func/list/tree';
            this.appSoftId = data.appSoftId;
            this.appList = false;
            this.apps = true;
            this.getAppFunc();
            $(".tabs").removeClass('hide');

            if($(_this.$refs.treeLeft1.$el).children().hasClass('is-current')){
                $(_this.$refs.treeLeft1.$el).children().removeClass("is-current")
            }
            if($(_this.$refs.treeLeft2.$el).children().hasClass('is-current')){
                $(_this.$refs.treeLeft2.$el).children().removeClass("is-current")
            }
            if(this.curLevel1Id=="cloudSoft-1"){
                this.$refs.treeLeft1.setCurrentKey(data.appSoftId);
                this.$refs.treeLeft2.setCurrentKey(null);
            }else{
                this.$refs.treeLeft2.setCurrentKey(data.appSoftId);
                this.$refs.treeLeft1.setCurrentKey(null);
            }
        },
        tabClick:function(val){
            if(val.name=="role"){
                this.getRole();
                $(".tabs").addClass('hide');
                this.editBtn =false;
                this.filterText = '';
            }else{
                this.editBtn =true;
                $(".tabs").removeClass('hide');
            }
        },
        clickTreeNode:function(data, node){ // 树节点点击事件
            var _this = this;
            this.showRightSlider=false;
            this.keyword = '';
            this.level = 1;

            // if(this.treeActive == "user"){
            //   var name = '用户端─';
            // }else{
            //   var name = '管理端─'
            // }

            if(data.type=="APP_SOFT"){
                if(data.data){
                    this.funName = data.data.softName;
                }else{
                    this.funName = data.label;
                }
                this.appData = data;
                this.url = ctx + 'app/func/list/tree';
                this.appSoftId = data.data.appSoftId;
                this.getAppFunc();
                this.appList = false;
                this.apps = true;
                this.active ='module';
                this.editBtn =true;
                $(".tabs").removeClass('hide');

            }else{
                this.editBtn =false;
                this.url = ctx + 'rest/opu/rs/appSoft/list';
                if(data.type=="cloudSoft"){
                    this.isCloudSoft = 1;
                    this.funName = '云应用';
                    this.self=false;
                    this.synCloudApp=true;
                    this.synApp=false;
                }else{
                    this.isCloudSoft = 0;
                    this.funName = '自建应用';
                    this.self=true;
                    this.synCloudApp=false;
                    this.synApp=true;
                }
                this.appList = true;
                this.apps = false;
                this.curLevel1Id = data.id;
                this.getAppList();
                $(".tabs").addClass('hide');

            }
            if($(_this.$refs.treeLeft1.$el).children().hasClass('is-current')){
                if((node.parent && node.parent.level==0 && node.parent.data[0].id=="cloudSoft-1") || node.data.id=="cloudSoft-1") return;
                $(_this.$refs.treeLeft1.$el).children().removeClass("is-current")
            }
            if($(_this.$refs.treeLeft2.$el).children().hasClass('is-current')){
                if((node.parent && node.parent.level==0 && node.parent.data[0].id=="notCloudSoft-1") || node.data.id=="notCloudSoft-1") return;
                $(_this.$refs.treeLeft2.$el).children().removeClass("is-current")
            }
            if(node.parent && node.parent.level==0 && node.parent.data[0].id=="cloudSoft-1"){
                this.$refs.treeLeft1.setCurrentKey(data.id);
                // this.$refs.treeLeft2.setCurrentKey(null);
                $(_this.$refs.treeLeft2.$el).children().children(".el-tree-node__children").children(".is-current").removeClass("is-current")
            }else if(node.parent && node.parent.data.id=="cloudSoft-1"){
                this.$refs.treeLeft1.setCurrentKey(data.id);
                // this.$refs.treeLeft2.setCurrentKey(null);
                $(_this.$refs.treeLeft2.$el).children().children(".el-tree-node__children").children(".is-current").removeClass("is-current")
            }else if(node.parent && node.parent.data.id!="cloudSoft-1"){
                this.$refs.treeLeft2.setCurrentKey(data.id);
                // this.$refs.treeLeft1.setCurrentKey(null);
                $(_this.$refs.treeLeft1.$el).children().children(".el-tree-node__children").children(".is-current").removeClass("is-current")
            }
        },
        searAndClear:function(num, isClear){
            var _this = this;
            if(isClear){
                this.keyword='';
            }
            if(num==1){
                this.getAppFunc();
            }else{
                this.getAppList();
            }
        },
        changeEnableType:function(data,e){
            var _this = this;
            if(data.funcType=='func'){
                if(data.isActiveFunc=="1"){
                    var url = ctx + 'rest/opu/rs/func/disable';
                    var msg = '禁用该功能';
                }else{
                    var msg = '启用该功能';
                    var url = ctx + 'rest/opu/rs/func/enable';
                }
            }else{
                if(data.isActiveFunc=="1"){
                    var msg = '禁用该模块';
                    var url = ctx + 'rest/opu/rs/func/disable/cascade';
                }else{
                    var msg = '启用该模块';
                    var url = ctx + 'rest/opu/rs/func/enable/cascade';
                }
            }
            // confirmMsg('','确定要'+msg+'吗？',function(){
            request('opus-admin',{url:url,type: 'put',data: {funcId : data.funcId}},function (data) {
                _this.$message({
                    message: '修改成功',
                    type: 'success'
                });
                // _this.getAppFunc();
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
            // },function(){
            //   data.isActiveFunc = data.isActiveFunc;
            // })
        },
        // treeNodeRightClick(event,data,Node){  // 树节点右键事件
        //   console.log(111)
        //   $('.right-click-opt').show().css({'left': event.clientX, 'top': event.clientY});
        //   this.leftSelId = data.id;
        //   this.leftSelPid = data.pId;
        //   this.leftSelNode = Node;
        //   this.leftSelNodeData = data;
        // },
        //新增应用工程
        addAppSoft:function(){
            var _this= this;
            this.softCss=false;
            this.setPic2 = true;
            this.notAllowed=true;
            this.allowed2=true;
            this.iconCss2=false;
            this.sidebarActive = "baseInfo"

            request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft/code',type: 'get',
                data: {
                    codeRule:'OPU-RS-APP-SOFT'
                }
            },function (data) {
                _this.formData3 = new Object;
                _this.formData3 = {
                    protocolHeader:'http://',
                    isActive:"1",
                    isCloudSoft:"0",
                    isSingleUrl:"0",
                    autoSwitch:"0",
                    useLoadBalence:"0",
                    softStatus:"1",
                    softIconCss:'',
                    isImgIcon:"0"
                }
                _this.$nextTick(function () {
                    _this.$refs['formData3'].clearValidate();
                });
                // vue中set方法
                _this.$set(_this.formData3,'softCode',data[0]);
                request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft/code/check',type: 'get',
                    data: {
                        softCode:data[0],
                        appSoftId:_this.softId
                    }
                },function (data) {
                    if(data){
                        _this.addSoft=true;
                        _this.editSoftStatus = false;
                        _this.addIconAndEdit = false;
                        _this.setParent = false;
                        _this.roleAuthor1 = false;
                        _this.showRightSlider=true;
                        _this.moduleLabel = '新增应用';
                    }else{
                        _this.$message({
                            message: '编号重复',
                            type: 'error'
                        });
                    }
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        //编辑应用
        editSoft:function(){
            var _this = this;
            this.editSoftStatus=true;
            if(this.multipleSelection2.length>1) {
                _this.$message({
                    message: '只能选择一项编辑',
                    type: 'error'
                });
                return;
            }else if(this.multipleSelection2.length==0){
                _this.$message({
                    message: '请选择一项编辑',
                    type: 'error'
                });
                return;
            }else{
                request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft',type: 'get',data:{appSoftId:this.multipleSelection2[0].appSoftId}},function (data) {
                    _this.addSoft=true;
                    _this.addIconAndEdit = false;
                    _this.setParent = false;
                    _this.roleAuthor1 = false;
                    _this.showRightSlider=true;
                    _this.moduleLabel = '基本信息';
                    _this.formData3 = data;
                    _this.formData3.appSoftId = _this.multipleSelection2[0].appSoftId;
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            }

        },
        //删除应用
        deleteSoft:function(){
            var _this = this;
            var ids = [];
            $.each(this.multipleSelection2, function(index, val) {
                ids.push(val.appSoftId);
            });
            if(ids.length==0){
                _this.$message({
                    message: '请先选择一条要删除的数据',
                    type: 'error'
                });
                return
            }
            confirmMsg('','此操作将批量删除选择的应用数据，应用下关联的功能、角色将丢失，您确定执行吗？',function(){
                request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft/more?appSoftIds='+ids,type: 'delete',},function (data) {
                    _this.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    if(_this.isCloudSoft==1){
                        _this.node=false;
                    }else{
                        _this.node=true;
                    }
                    _this.getTreeData();
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },
        //同步应用
        synApps:function(){
            var _this = this;
            confirmMsg('','此操作将同步并覆盖当前应用信息，您确定执行吗？',function(){
                request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft/sync',type: 'put',
                    data:{
                        isAdmin:_this.isAdmin,
                        isInnerSoft:_this.isCloudSoft
                    }
                },function (data) {
                    _this.$message({
                        message: '同步成功',
                        type: 'success'
                    });
                    _this.getTreeData();
                    _this.node=true;
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },
        //同步云应用
        synCloudApps:function(){
            var _this = this;
            confirmMsg('','此操作将同步并覆盖当前应用信息，您确定执行吗？',function(){
                request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft/sync',type: 'put',
                    data:{
                        isAdmin:_this.isAdmin,
                        isInnerSoft:_this.isCloudSoft
                    }
                },function (data) {
                    _this.$message({
                        message: '同步成功',
                        type: 'success'
                    });
                    _this.getTreeData();
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },
        // 保存应用
        saveSoft:function(){
            var _this = this;
            var param = JSON.stringify({
                isAdmin:this.isAdmin,
                appSoftId:this.formData3.appSoftId,
                softName: this.formData3.softName,
                softCode: this.formData3.softCode,
                softWebContext: this.formData3.softWebContext,
                clientId: this.formData3.clientId,
                clientSecret: this.formData3.clientSecret,
                softInnerUrl: this.formData3.softInnerUrl,
                softGovUrl: this.formData3.softGovUrl,
                softOuterUrl: this.formData3.softOuterUrl,
                protocolHeader: this.formData3.protocolHeader,
                softType: this.formData3.softType,
                isCloudSoft: this.formData3.isCloudSoft,
                isSingleUrl: this.formData3.isSingleUrl,
                autoSwitch: this.formData3.autoSwitch,
                useLoadBalence: this.formData3.useLoadBalence,
                softStatus: this.formData3.softStatus,
                softMemo : this.formData3.softMemo,
                isImgIcon : this.formData3.isImgIcon,
                softIconCss : this.formData3.softIconCss
            })
            if(this.editSoftStatus){
                var type="put"
            }else{
                var type='post'
            }
            this.$refs['formData3'].validate((valid) => {
                if(!valid)  {
                setTimeout(()=>{
                    var isError= document.getElementsByClassName("is-error");
                isError[0].querySelector('input').focus();
            },1)
                return false;
            };
            if(!this.formData3.cloudSoftId){
                _this.node=true;
            }else{
                _this.node=false;
            }
            request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft',type: type,ContentType:'application/json',data: param},function (data) {
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });

                _this.getTreeData();
                _this.showRightSlider=false;
                _this.editSoftStatus = false;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        })

        },
        handleCurrentChange5:function(row, column, event){
            if(column.label=="操作") return;
            var _this = this;
            this.softCss=true;
            this.editSoftStatus=true;
            this.setPic2 = false;
            this.sidebarActive = "baseInfo"
            var isimageIcon = row.isImgIcon ? row.isImgIcon :"1";
            this.usedPic2(isimageIcon);
            this.$refs.tabPane.$el.scrollTop =0;
            request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft',type: 'get',
                    data:{appSoftId:row.appSoftId}},
                function (data) {
                    _this.addSoft=true;
                    _this.addIconAndEdit = false;
                    _this.setParent = false;
                    _this.roleAuthor1 = false;
                    _this.showRightSlider=true;
                    _this.moduleLabel = '基本信息';
                    _this.formData3 = data;
                    _this.formData3.appSoftId = row.appSoftId;
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
        },
        handleCurrentChange6:function(row, column, event){
            event.cancelBubble=true;
            var _this = this;
            if(column.label=="是否启用"||column.label=="操作") return;
            $(".el-tabs").removeClass('hide');
            this.$refs.tabPane.$el.scrollTop =0;
            var isimageIcon = row.data.isImgIcon ? row.data.isImgIcon :"0";
            this.usedPic(isimageIcon);
            this.setPic=false;
            this.isAdd = true;
            this.sidebarActive = "baseInfo"
            request('opus-admin',{url:ctx + 'app/func',type: 'get',
                    data:{funcId:row.data.funcId}},
                function (data) {
                    $(column.currentTarget).addClass('cloumn').siblings().removeClass("cloumn");
                    _this.formData1 = data;
                    _this.currFuncData = data;
                    _this.chooseType("edit",row.data.funcType);
                    _this.delBtn=true;
                    _this.SaAbtn=false;
                    _this.setParent =false;
                    _this.setparentBtn =true;
                })
        },
        handleCurrentChange7:function(row, column, event){
            var _this = this;
            event.cancelBubble=true;
            this.roleAuthor1=true;
            this.addSoft = false;
            this.addAndEdit=false;
            this.setParent=false;
            this.addIconAndEdit=false;
            this.showRightSlider=true;
            this.moduleLabel ='编辑角色';
            this.$refs.tabPane.$el.scrollTop =0;
            request('opus-admin',{url:ctx + 'rest/opu/rs/role',type: 'get',ContentType:'application/json',
                data:{
                    roleId:row.roleId
                }
            },function (data) {
                _this.formData2 = data;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        hideClickOpt:function(){ // 关闭树节点右键弹窗
            $('.right-click-opt').hide()
        },
        handleSelectionChange:function(val) {
            this.multipleSelection = val;
            this.funcIds=[];
            this.funcTypes=[];
            this.currTypeData = val[0];
            this.multipleSelection.map((item)=> {
                this.funcIds.push(item.data.funcId)
            this.funcTypes.push(item.data.funcType)
        })
            return this.funcIds;
            return this.funcTypes;
        },
        handleSelectionChange2:function(val) {
            this.multipleSelection2 = val;
        },
        handleSelectionChange3:function(val) {
            this.currentRow = val;
            this.multipleSelection3 = val;
            // this.getRoleFunc(this.multipleSelection3);
            if(this.multipleSelection3.length>1){
                this.$refs.singleTable.toggleRowSelection(this.multipleSelection3[0]);
                this.multipleSelection3 = this.multipleSelection3[0];
                this.getRoleFunc(this.multipleSelection3);
            }
        },
        handleCurrentTable3:function(val) {
            this.currentRow = val;
            this.multipleSelection3 = val;
            if(this.multipleSelection3.length>1){
                this.$refs.singleTable.toggleRowSelection(this.multipleSelection3[0]);
                this.multipleSelection3 = this.multipleSelection3[0];
                this.getRoleFunc(this.multipleSelection3);
            }

        },

        handleSizeChange:function(val) {
            this.pageSize = val;
            this.getAppFunc()
        },
        handleCurrentChange:function(val) {
            this.page = val;
            this.getAppFunc()
        },
        handleSizeChange2:function(val) {
            this.pageSize2 = val;
            this.getAppList()
        },
        handleCurrentChange2:function(val) {
            this.page2 = val;
            this.getAppList()
        },
        handleSizeChange3:function(val) {
            this.pageSize3 = val;
            this.getRole()
        },
        handleCurrentChange3:function(val) {
            this.page3 = val;
            this.getRole()
        },
        resetForm:function(){
            this.formData1 = new Object();
            this.formData1.isCreateMenu=1;
        },
        getModuleOrFunc:function(funcId) {
            var _that = this;
            request('opus-admin',{url:ctx + 'rest/opu/rs/func',type: 'get',data: {funcId : funcId}},function (data) {
                _that.formData1 = data;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        nodeClick4:function(data,checked,node){
            this.checkedId = data.id
            this.$refs.treeLeft4.setCheckedNodes([data]);
        },
        handleClick4:function(data, checked, node){
            if(checked == true){
                this.checkedId = data.id;
                this.$refs.treeLeft4.setCheckedNodes([data]);
            }
        },
        editEnter:function(){
            if(this.appData.data) {
                appSoftId = this.appData.data.appSoftId;
            }else{
                appSoftId = this.appData.appSoftId;
            }
            $(".el-tabs").addClass('hide');
            var _this = this;
            this.setPic = true;
            this.editSoftStatus=true;
            request('opus-admin',{url:ctx + 'rest/opu/rs/appSoft',type: 'get',
                    data:{appSoftId:appSoftId}},
                function (data) {
                    _this.addSoft=true;
                    _this.addIconAndEdit = false;
                    _this.setParent = false;
                    _this.roleAuthor1 = false;
                    _this.showRightSlider=true;
                    _this.moduleLabel = '基本信息';
                    _this.formData3 = data;
                    _this.formData3.appSoftId = appSoftId;
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });

        },
        // 获取其他父级
        getOtherParent:function() {
            var _that = this;
            this.addSoft = false;
            // if(this.currTypeData){
            //   this.currFuncData = this.currTypeData.data;
            // }else{
            this.currFuncData = this.currFuncData;
            // }
            request('opus-admin',{url:ctx + 'rest/opu/rs/func/omodu/tree',type: 'get',
                data: {
                    appSoftId:this.currFuncData.appSoftId,
                    objId:this.currFuncData.funcId
                }
            },function (data) {
                if(data.length==0){
                    _that.$message({
                        message: '当前对象无可用父级，无需设置',
                        type: 'error'
                    });
                    return;
                }
                _that.treeData4 = data;
                _that.showRightSlider=true;
                _that.moduleLabel = '设置父级';
                _that.setParent = true;
                _that.addAndEdit =false;
                _that.addIconAndEdit =false;
                var id = [];
                util.treeTableXcode(_that.tableData, _that.currFuncData);
                if(!_that.currFuncData.parentFuncId){
                    id.push("root");
                }else{
                    id.push(_that.currFuncData.parentFuncId);
                }
                _that.$nextTick(function(){
                    _that.$refs.treeLeft4.setCheckedKeys(id);
                })
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        // 设置父级
        setOtherParent:function(){
            var _this = this;
            var funcId = this.$refs.treeLeft4.getCheckedKeys();
            if(funcId.length==0){
                _this.$message({
                    message: '请指定一个父级',
                    type: 'error'
                });
                return
            }
            request('opus-admin',{url:ctx + 'rest/opu/rs/func/setparent',type: 'put',
                data: {
                    appSoftId:this.currFuncData.appSoftId,
                    curFuncId :this.currFuncData.funcId,
                    targetFuncId :funcId[0]
                }
            },function (data) {
                _this.$message({
                    message: '设置成功',
                    type: 'success'
                });
                _this.getAppFunc();
                _this.cancle();
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        cancle:function(){
            var _this = this;
            _this.addIconAndEdit = true;
            _this.setParent =false;
            _this.moduleLabel = this.local;
        },
        // 新增按钮点击事件
        isData:function(){
            if(this.funcIds.length==0){
                // this.formData1 = new Object;
                // this.$nextTick(function () {
                //   this.$refs['formData1'].clearValidate();
                // });
                // this.$refs.tabPane.$el.scrollTop =0;
                // this.sidebarActive = "baseInfo";
                // this.SaAbtn=true;
                // this.addIconAndEdit = true;
                // this.creatBtn=true;
                // this.tmn=true;
                // this.edit=false;
                // this.setPic = false;
                // this.addSoft=false;
                // this.Soft = false;
                // this.funASoft = false;
                // this.setParent = false;
                // this.showRightSlider = true;
                // this.formData1.isCreateMenu="1"
                // this.formData1.funcInvokeType="0"
                // this.formData1.isAutoImport="0"
                // this.formData1.funcType="modu"
                // this.formData1.isActiveFunc="1"
                // this.formData1.isImgIcon = "0";
                // this.formData1.tmnId = "1";
                // this.formData1.funcDeleted="0"
                // this.code='模块编号'
                // this.name='模块名称'
                // this.moduleLabel = "新增顶级模块";
                // this.roleAuthor1 =false;
                // this.setparentBtn =false;
                // this.getCode('module');
                // this.delBtn=false;
                // $(".tabs").removeClass('hide');
                this.$refs.tabPane.$el.scrollTop =0;
                this.dialogEditTable=true;
            }else if(this.funcIds && this.funcIds.length==1){
                if(this.funcTypes=="func"||this.funcTypes=="widget"){
                    this.$message({
                        message: '此项不能再新增',
                        type: 'error'
                    });
                }else{
                    this.$refs.tabPane.$el.scrollTop =0;
                    this.dialogEditTable=true;
                }
            }else{
                this.$message({
                    message: '请只选择一项',
                    type: 'error'
                });
            }
        },
        // 新增时获取编号
        getCode:function(type){
            var _this = this;
            if(type=="module"){
                var rule = 'OPU-RS-MODULE';
            }else{
                var rule = 'OPU-RS-FUNC';
            }
            request('opus-admin',{url:ctx + 'rest/opu/rs/func/funcCode',type: 'get',
                data: {
                    codeRule:rule
                }
            },function (data) {
                // vue中set方法
                _this.$set(_this.formData1,'funcCode',data[0])
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        editTypeData:function(){
            if(this.funcIds.length==0){
                this.$message({
                    message: '请选择一项',
                    type: 'error'
                });
            }else if(this.funcIds && this.funcIds.length==1){
                this.formData1 = this.currTypeData.data;
                this.currFuncData = this.currTypeData.data;
                this.chooseType("edit",this.funcTypes);
                this.delBtn=true;
                this.SaAbtn=false;
                this.setParent =false;
                this.setparentBtn =true;

            }else{
                this.$message({
                    message: '请只选择一项',
                    type: 'error'
                });
            }
        },
        // 新增类型选择弹窗
        selectType:function(oper,type){
            var _this =this;
            this.setPic=true;
            this.notAllowed=true;
            this.chooseType(oper,type);
            this.setParent = false;
            this.dialogEditTable=false;
            this.sidebarActive = "baseInfo"
            this.delBtn=false;
            this.isAdd = false;
            this.allowed=true;
            this.iconCss=false;
            // this.getAppFunc();

        },
        // 保存新增或修改数据
        saveFunc:function() {
            var _that = this;
            if(this.formData1.funcType != "toolBar"){
                var param = JSON.stringify({
                    funcCode :  this.formData1.funcCode,
                    funcName : this.formData1.funcName,
                    pagePosition : this.formData1.pagePosition,
                    isCreateMenu :this.formData1.isCreateMenu,
                    netTmnId : this.formData1.netTmnId,
                    funcLevel : this.formData1.funcLevel,
                    funcType : this.formData1.funcType,
                    funcSortNo : this.formData1.funcSortNo,
                    funcMemo : this.formData1.funcMemo,
                    parentFuncId : this.formData1.parentFuncId,
                    funcDeleted : this.formData1.funcDeleted,
                    isAutoImport : this.formData1.isAutoImport,
                    funcInvokeType : this.formData1.funcInvokeType,
                    isActiveFunc : this.formData1.isActiveFunc,
                    funcId : this.formData1.funcId,
                    appSoftId : this.appSoftId,
                    isImgIcon : this.formData1.isImgIcon ,
                    funcShortcutKey:this.formData1.funcShortcutKey,
                    currNetId : this.currentNetId,
                    widgetType : this.formData1.widgetType,
                    funcInvokeUrl : this.formData1.funcInvokeUrl,
                    funcInvokeParam : this.formData1.funcInvokeParam,
                    funcIconCss : this.formData1.funcIconCss
                })
            }else{
                var param = JSON.stringify({
                    funcCode :  this.formData1.funcCode,
                    funcName : this.formData1.funcName,
                    pagePosition : this.formData1.pagePosition,
                    isCreateMenu :this.formData1.isCreateMenu,
                    netTmnId : this.formData1.netTmnId,
                    funcLevel : this.formData1.funcLevel,
                    funcType : this.formData1.funcType,
                    funcSortNo : this.formData1.funcSortNo,
                    funcMemo : this.formData1.funcMemo,
                    parentFuncId : this.formData1.parentFuncId,
                    funcDeleted : this.formData1.funcDeleted,
                    isAutoImport : this.formData1.isAutoImport,
                    funcInvokeType : this.formData1.funcInvokeType,
                    isActiveFunc : this.formData1.isActiveFunc,
                    funcId : this.formData1.funcId,
                    appSoftId : this.appSoftId,
                    isImgIcon : this.formData1.isImgIcon ,
                    funcShortcutKey:this.formData1.funcShortcutKey,
                    currNetId : this.currentNetId,
                    widgetType : this.formData1.widgetType,
                    funcInvokeUrl : this.formData1.funcInvokeUrl,
                    funcInvokeParam : this.formData1.funcInvokeParam,
                    funcIconCss : this.formData1.funcIconCss,
                    toolBarId : this.formData1.toolBarId,
                    widgetId : this.formData1.widgetId,
                    onload : this.formData1.onload,
                    toolBarInvokeUrl : this.formData1.toolBarInvokeUrl,
                    icon : this.formData1.icon,
                    src : this.formData1.src,
                })
            }
            this.$refs['formData1'].validate((valid) => {
                if(!valid)  return false;
            if(this.formData1.funcType != "toolBar"){
                request('opus-admin',{url:ctx + 'rest/opu/rs/func/menu',type: 'post',ContentType:'application/json',data: param},function (data) {
                    _that.editPagePosition(data.content.funcId,_that.formData1.pagePosition,function (res) {
                        _that.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        // _that.delBtn=true;
                        _that.getAppFunc();
                        _that.keyword2="";
                        _that.showRightSlider=false;
                    },function (res) {
                        alertMsg('',res?res:'服务器异常','关闭','error',true);
                    });
                    /*_that.$message({
                        message: '保存成功',
                        type: 'success'
                    });
                    // _that.delBtn=true;
                    _that.getAppFunc();
                    _that.keyword2="";
                    _that.showRightSlider=false;*/
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            }else{
                request('opus-admin',{url:ctx + 'app/func/menu',type: 'post',ContentType:'application/json',data: param},function (data) {
                    _that.editPagePosition(_that.formData1.funcId,_that.formData1.pagePosition,function (res) {
                        debugger
                        _that.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        // _that.delBtn=true;
                        _that.getAppFunc();
                        _that.keyword2="";
                        _that.showRightSlider=false;
                    },function (res) {
                        alertMsg('',res?res:'服务器异常','关闭','error',true);
                    });
                    /*_that.$message({
                        message: '保存成功',
                        type: 'success'
                    });
                    // _that.delBtn=true;
                    _that.getAppFunc();
                    _that.keyword2="";
                    _that.showRightSlider=false;*/
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            }
        })
        },
        editPagePosition: function (funcId, pagePosition, success, error) {
            if (funcId == undefined || funcId == "" || funcId == null) {
                error("funcId is null");
                return;
            }
            request('',
                {
                    url: ctx + 'app/func/editPagePosition',
                    type: 'get',
                    data: {
                        funcId: funcId,
                        pagePosition: pagePosition
                    }
                }, function (data) {
                    success(data)
                }, function (msg) {
                    error(msg)
                });
        },
        // 保存并新增
        saveAndAdd:function(){
            this.saveFunc();
            this.chooseType('add',this.currentType);
        },
        // 单条删除功能
        deleteFunc:function(data) {
            var _that = this;
            if(data){
                var funcId = data.funcId;
            }else{
                var funcId = this.currFuncData.funcId;
            }
            if(this.moOrFunc=="func"||this.moOrFunc=="widget"){
                var url = ctx + 'rest/opu/rs/func?funcId='
            }else{
                var url = ctx + 'rest/opu/rs/func/cascade?funcId='
            }
            if(data.funcType=="func"){
                var text = "此操作将删除本功能数据，如果功能已经关联菜单，可能影响用户关联的菜单数据，您确定执行吗？";
            }else if(data.funcType=="modu"){
                var text = "此操作将删除本模块以及包含的下级数据，您确定执行吗？";
            }else if(data.funcType=="widget"){
                var text = "此操作将删除本微件数据，如果微件已经关联菜单，可能影响用户关联的菜单数据，您确定执行吗？";
            }else{
                var text = "此操作将删除本工具栏数据，如果工具栏已经关联菜单，可能影响用户关联的菜单数据，您确定执行吗？";
            }
            confirmMsg('',text,function(){
                request('opus-admin',{url:url+funcId,type: 'delete'},function (data) {
                    _that.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    _that.showRightSlider = false;
                    _that.getAppFunc();
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },
        // 删除功能和模块
        delFuncAndModule:function(data) {
            var _that = this;
            if(_that.funcIds.length==0){
                _that.$message({
                    message: '请选择要删除的数据',
                    type: 'error'
                });
                return
            }
            confirmMsg('','此操作将批量删除选择的数据，可能影响用户关联的菜单数据，您确定执行吗？',function(){
                request('opus-admin',{url:ctx + 'rest/opu/rs/func/more?funcIds='+_that.funcIds,type: 'delete'},function (data) {
                    _that.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    _that.getAppFunc();
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },

        // table操作按钮编辑事件
        showManInfo:function(data, type){
            if(type=="edit"){
                this.currentFuncId= data.funcId;
                this.getModuleOrFunc(data.funcId);
                var oper = "编辑";
            }else{
                var oper = "新增";
            }
            this.sidebarActive = "baseInfo";
            this.delBtn=true;
            this.addSoft=false;
            this.chooseType(type,data.funcType);
            this.currFuncData = data;
            this.setParent = false;
            this.addAndEdit =true;
            this.showRightSlider = true;
        },
        // 解决sleect选不中值问题
        forceUpdate:function(){
            this.$forceUpdate()
        },
        // 新增/编辑、模块/功能/微件/工具 时表单显示字段
        chooseType:function(oper, type){
            this.roleAuthor1 =false;
            this.addSoft=false;
            this.currentType = type;
            if(oper=="add"){
                this.formData1 = new Object;

                if(this.funcIds.length!=0){
                    oper = "新增";
                    this.formData1.parentFuncId = this.currTypeData.data.funcId;
                }else{
                    oper = "新增顶级";
                }
                this.setparentBtn = false;
                this.$nextTick(function () {
                    this.$refs['formData1'].clearValidate();
                });
                this.formData1.isCreateMenu="1"
                this.formData1.funcDeleted="0"
                this.formData1.isAutoImport="0"
                this.formData1.funcInvokeType="0"
                this.formData1.isActiveFunc="1"
                this.formData1.netTmnId=(this.option4!=null&&this.option4.length>0)?this.option4[0].netTmnId:' ';
                this.formData1.isImgIcon="0"
                this.getCode(type);
                // this.usedPic("0");
                this.addIconAndEdit=true;
                if(type=='func'){
                    this.formData1.funcType="func"
                    this.moduleLabel = oper+'功能';
                    this.code='功能编号'
                    this.name='功能名称'
                    this.callType='功能调用类型'
                    this.invokeUrl='功能访问路径'
                    this.creatBtn = true;
                    this.Soft = false;
                    this.funASoft = true;
                    this.isToolBar = false;
                    this.formData1.funcShortcutKey = 0;
                }else if(type=='widget'){
                    this.formData1.funcType="widget"
                    this.moduleLabel = oper+'微件';
                    this.code='微件编号'
                    this.name='微件名称'
                    this.callType='微件调用类型'
                    this.invokeUrl='微件访问路径'
                    this.creatBtn = true;
                    this.Soft = true;
                    this.funASoft = true;
                    this.isToolBar = false;
                    this.formData1.funcShortcutKey = 0;
                    this.formData1.widgetIsConfig = 1;
                    this.formData1.widgetIsVisiable =1;
                    this.formData1.widgetIsUseTpl = 0;
                }else if(type=='module'){
                    this.formData1.funcType="modu"
                    this.moduleLabel = oper+'模块';
                    this.code='模块编号'
                    this.name='模块名称'
                    this.creatBtn = true;
                    this.Soft = false;
                    this.funASoft = false;
                    this.isToolBar = false;
                }else if(type=='toolBar'){
                    this.formData1.funcType="toolBar";
                    this.moduleLabel = oper+'工具栏';
                    this.code='工具栏编号';
                    this.toolBarId = '工具栏ID';
                    this.name='工具栏名称';
                    this.toolBarInvokeUrl='工具栏路径';
                    this.onload='工具栏加载函数';
                    this.src='工具栏源码路径';
                    this.icon='工具栏图标样式';
                    this.widgetId='微件ID'
                    this.creatBtn = false;
                    this.Soft = false;
                    this.isToolBar = true;
                    this.funASoft = false;
                }
                this.tmn = true;
                this.edit=false;
            }else{
                oper = "编辑";
                this.setparentBtn = true;
                if(type=='func'){
                    this.moduleLabel = '基本信息';
                    this.code='功能编号'
                    this.name='功能名称'
                    this.callType='功能调用类型'
                    this.invokeUrl='功能访问路径'
                    this.Soft = false;
                    this.funASoft = true;
                    this.isToolBar = false;
                }else if(type=='widget'){
                    this.moduleLabel = '基本信息';
                    this.code='微件编号'
                    this.name='微件名称'
                    this.callType='微件调用类型'
                    this.invokeUrl='微件访问路径'
                    this.creatBtn = true;
                    this.Soft = true;
                    this.funASoft = true;
                    this.isToolBar = false;
                }else if(type=='modu'){
                    this.moduleLabel = '基本信息';
                    this.code='模块编号'
                    this.name='模块名称'
                    this.creatBtn = false;
                    this.Soft = false;
                    this.funASoft = false;
                    this.isToolBar = false;
                }else if(type=='toolBar'){
                    this.formData1.funcType="toolBar";
                    this.moduleLabel = oper+'工具栏';
                    this.code='工具栏编号';
                    this.toolBarId = '工具栏ID';
                    this.name='工具栏名称';
                    this.toolBarInvokeUrl='工具栏路径';
                    this.onload='工具栏加载函数';
                    this.src='工具栏源码路径';
                    this.icon='工具栏图标样式';
                    this.widgetId='微件ID'
                    this.creatBtn = true;
                    this.Soft = false;
                    this.funASoft = false;
                    this.isToolBar = true;
                }
                this.local = this.moduleLabel;

                this.addIconAndEdit =true;
                this.creatBtn = false;
                this.tmn = false;
                this.edit = true;
            }
            this.showRightSlider=true;

        },
        usedPic:function(val){
            var _this = this;
            if(val==1){
                this.iconCss=false;
                this.allowed = false;
                this.notAllowed = false;
            }else{
                this.iconCss=true;
                this.allowed = true;
                this.notAllowed = true;
            }
        },
        usedPic2:function(val){
            var _this = this;
            if(val==1){
                this.iconCss2=false;
                this.allowed2 = false;
                this.notAllowed = false;
            }else{
                this.iconCss2=true;
                this.allowed2 = true;
                this.notAllowed = true;
            }
        },
        handleCheckChange:function(data, checked, indeterminate) {
            console.log(data, checked, indeterminate);
        },
        //获取角色数据
        getRole:function(data) {
            var _this = this;
            _this.loading = true;
            request('opus-admin',{url:ctx + 'rest/opu/rs/role/list',type: 'get',
                data:{
                    appId : this.appSoftId
                }
            },function (data) {
                if(!data) return;
                _this.tableData3 = data;
                _this.loading = false;
                _this.multipleSelection3 = data[0];
                // if(!_this.multipleSelection3 || _this.multipleSelection==undefined) {
                //   _this.treeData3 = [];
                //   return
                // };
                _this.total3= data.total;
                _this.getFuncTree(_this.multipleSelection3);
                _this.getRoleFunc(_this.multipleSelection3);
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        //获取功能树
        getFuncTree:function(item) {
            var _this = this;
            request('opus-admin',{url:ctx + 'rest/opu/rs/func/tree',type: 'get',
                data:{
                    appSoftId : this.appSoftId
                }
            },function (data) {
                if(!data) return;
                _this.treeData3 = data;
                _this.$refs.singleTable.toggleRowSelection(item);
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        //获取角色对应功能
        getRoleFunc:function(item) {
            var _this = this;
            if(!item) {
                this.setCheckedKeys([]);
                return
            };
            request('opus-admin',{url:ctx + 'rest/opu/rs/role/func',type: 'get',
                data:{
                    roleId :item.roleId
                }
            },function (data) {
                var id = [];
                for(var i=0; i<data.length; i++){
                    id.push(data[i].funcId);
                }
                _this.setCheckedKeys(id);
                _this.loading = false;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        setCurrent:function(row) {
            this.$refs.singleTable.setCurrentRow(row);
        },
        setCheckedKeys:function(id) {
            this.$refs.treeLeft3.setCheckedKeys(id,false);
        },
        // 新增角色按钮
        addRole:function(){
            this.formData2 = new Object;
            this.roleAuthor1=true;
            this.addIconAndEdit=false;
            this.addAndEdit=false;
            this.addSoft= false;
            this.notAllowed = true;
            this.setPic2=true;
            this.setParent=false;
            this.showRightSlider=true;
            this.formData2.appId = this.appData.data?this.appData.data.appSoftId:this.appData.appSoftId;
            this.formData2.roleId = "";
            this.moduleLabel ='新增角色';
            this.$nextTick(function () {
                this.$refs['formData2'].clearValidate();
            });
        },
        //编辑角色数据按钮
        editRole:function() {
            var _this = this;
            if(_this.multipleSelection3.length==0){
                _this.$message({
                    message: '请先选择角色',
                    type: 'error'
                });
                return
            }
            this.roleAuthor1=true;
            this.addAndEdit=false;
            this.setParent=false;
            this.addIconAndEdit=false;
            this.showRightSlider=true;
            this.moduleLabel ='编辑角色';

            if(_this.multipleSelection3.roleId){
                roleId=_this.multipleSelection3.roleId
            }else{
                roleId=_this.multipleSelection3[0].roleId
            }
            request('opus-admin',{url:ctx + 'rest/opu/rs/role',type: 'get',ContentType:'application/json',
                data:{
                    roleId:roleId
                }
            },function (data) {
                _this.formData2 = data;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        //删除角色数据按钮
        deleteRole:function() {
            var _this = this;
            if(_this.multipleSelection3.length==0){
                _this.$message({
                    message: '请先选择一条要删除的数据',
                    type: 'error'
                });
                return
            }
            if(_this.multipleSelection3.roleId){
                roleId=_this.multipleSelection3.roleId
            }else{
                roleId=_this.multipleSelection3[0].roleId
            }
            confirmMsg('','此操作将删除选择的角色数据，可能影响用户关联的菜单数据，您确定执行吗？',function(){
                request('opus-admin',{url:ctx + 'rest/opu/rs/role?roleId='+roleId,type: 'delete',ContentType:'application/json',
                    data:{}
                },function (data) {
                    _this.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    _this.getRole();
                    _this.getFuncTree(_this.multipleSelection3);
                }, function(msg){
                    alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                });
            })
        },
        //保存角色表单数据
        saveRole:function() {
            var _this = this;
            this.$refs['formData2'].validate((valid) => {
                if(!valid)  return false;
            request('opus-admin',{url:ctx + 'rest/opu/rs/role',type: 'post',ContentType:'application/json',
                data:JSON.stringify({
                    roleCode:this.formData2.roleCode,
                    roleName:this.formData2.roleName,
                    roleType:this.formData2.roleType,
                    roleSortNo:this.formData2.roleSortNo,
                    roleMemo:this.formData2.roleMemo,
                    roleId:this.formData2.roleId,
                    appId:this.formData2.appId,
                    roleDeleted:this.formData2.roleDeleted
                })
            },function (data) {
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });
                _this.showRightSlider=false;
                _this.getRole();
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        })

        },
        //保存角色与功能的关联关系
        saveRoleAndFunc:function() {
            var _this = this;
            if(_this.multipleSelection3.roleId){
                roleId=_this.multipleSelection3.roleId
            }else{
                roleId=_this.multipleSelection3[0].roleId
            }
            var funcIds1 = this.$refs.treeLeft3.getCheckedKeys();
            var funcIds2 = this.$refs.treeLeft3.getHalfCheckedKeys();
            var funcIds = funcIds1.concat(funcIds2)
            request('opus-admin',{url:ctx + 'rest/opu/rs/role/func?funcIds='+funcIds+'&roleId='+roleId,type: 'POST',ContentType:'application/json',
                data:{}
            },function (data) {
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });
                _this.getRole();
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        clearClass:function(){
            $(".tree2").removeClass('dis');
            $(".tree1").removeClass('dis');
        },
        filterNodeOrg:function(value, data) {
            if (!value) {
                return true;
            }
            if(data.label.indexOf(value) !==-1){
                this.num++;
            }
            if(data.label==this.lastNodeLabel){
                if(this.num==0){
                    $(".tree2").addClass('dis');
                }else{
                    $(".tree1").addClass('dis');
                    $(".tree2").addClass('dis');
                }
                this.num=0;
            }
            return data.label.indexOf(value) !== -1;
        },
        // 标签页切换
        tabClick2:function(val){
            if(!this.appList){
                if(val.name=="smallIcon"){
                    this.getIcon('16','small')
                }else if(val.name=="middleIcon"){
                    this.getIcon('24','middle')
                }else if(val.name=="bigIcon"){
                    this.getIcon('32','big')
                }else if(val.name=="hugeIcon"){
                    this.getIcon('64','huge')
                }
            }else{
                if(val.name=="smallIcon"){
                    this.getIcon2('16','small')
                }else if(val.name=="middleIcon"){
                    this.getIcon2('24','middle')
                }else if(val.name=="bigIcon"){
                    this.getIcon2('32','big')
                }else if(val.name=="hugeIcon"){
                    this.getIcon2('64','huge')
                }
            }



        },
        getIcon:function(imgSize, type){
            var _this = this;
            request('opus-admin',{url:ctx+'rest/opu/rs/func/ico',type: 'get',
                data: {
                    imgSize:imgSize,
                    type:type,
                    funcId:this.formData1.funcId
                }},function (data) {
                if(data.fileNameList.length>0) $(".noIcon").hide();
                if(type=="small"){
                    _this.icons1 = data.fileNameList;
                    if(_this.icons1.length==0){
                        $(".noIcon1").show();
                        return;
                    }
                }else if(type=="middle"){
                    _this.icons2 = data.fileNameList;
                    if(_this.icons2.length==0){
                        $(".noIcon2").show();
                        return;
                    }
                }else if(type=="big"){
                    _this.icons3 = data.fileNameList;
                    if(_this.icons3.length==0){
                        $(".noIcon3").show();
                        return;
                    }
                }else{
                    _this.icons4 = data.fileNameList;
                    if(_this.icons4.length==0){
                        $(".noIcon4").show();
                        return;
                    }
                }
                var smallImgPath = _this.formData1.smallImgPath;
                var middleImgPath = _this.formData1.middleImgPath;
                var bigImgPath = _this.formData1.bigImgPath;
                var hugeImgPath = _this.formData1.hugeImgPath;
                $.each(li, function(index, val) {
                    $(val).removeClass('active');
                });
                clearTimeout(timer);
                var timer = setTimeout(function(){
                    _this.addIconClass($(".smallBox").children("li"), smallImgPath);
                    _this.addIconClass($(".middleBox").children("li"), middleImgPath);
                    _this.addIconClass($(".bigBox").children("li"), bigImgPath);
                    _this.addIconClass($(".hugeBox").children("li"), hugeImgPath);
                }, 500)

            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        getIcon2:function(imgSize, type){
            var _this = this;
            if(type=="small"){
                var li = $(".smallBox").children("li");
            }else if(type=="middle"){
                var li = $(".middleBox").children("li");
            }else if(type=="big"){
                var li = $(".bigBox").children("li");
            }else{
                var li = $(".hugeBox").children("li");
            }
            var smallImgPath = _this.formData3.smallImgPath;
            var middleImgPath = _this.formData3.middleImgPath;
            var bigImgPath = _this.formData3.bigImgPath;
            var hugeImgPath = _this.formData3.hugeImgPath;
            $.each(li, function(index, val) {
                $(val).removeClass('active');
            });
            request('opus-admin',{url:ctx+'rest/opu/rs/appSoft/ico',type: 'get',
                data: {
                    imgSize:imgSize,
                    type:type,
                    appSoftId:this.formData3.appSoftId
                }},function (data) {
                if(data.fileNameList.length>0) $(".noIcon").hide();
                if(type=="small"){
                    _this.icons1 = data.fileNameList;
                    var li = $(".smallBox").children("li");
                    if(_this.icons1.length==0){
                        $(".noIcon1").show();
                        return;
                    }
                }else if(type=="middle"){
                    _this.icons2 = data.fileNameList;
                    var li = $(".middleBox").children("li");
                    if(_this.icons2.length==0){
                        $(".noIcon2").show();
                        return;
                    }
                }else if(type=="big"){
                    _this.icons3 = data.fileNameList;
                    var li = $(".bigBox").children("li");
                    if(_this.icons3.length==0){
                        $(".noIcon3").show();
                        return;
                    }
                }else{
                    _this.icons4 = data.fileNameList;
                    var li = $(".hugeBox").children("li");
                    if(_this.icons4.length==0){
                        $(".noIcon4").show();
                        return;
                    }
                }
                clearTimeout(timer);
                var timer = setTimeout(function(){
                    _this.addIconClass($(".smallBox").children("li"), smallImgPath);
                    _this.addIconClass($(".middleBox").children("li"), middleImgPath);
                    _this.addIconClass($(".bigBox").children("li"), bigImgPath);
                    _this.addIconClass($(".hugeBox").children("li"), hugeImgPath);
                }, 500)

            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        addIconClass:function(li, path){
            $.each(li, function(index, val) {
                if(val.dataset.url.indexOf(path) !==-1 && !$(val).hasClass('active')){
                    $(val).addClass('active');
                }
            });
        },
        iconClick:function(e){
            var _this = this;
            if(this.appList){
                var param = {
                    appSoftId:this.formData3.appSoftId
                };
                if(this.formData3.appSoftId==undefined){
                    _this.sidebarActive = "baseInfo";
                    return;
                }
                var curFormData = _this.formData3;
                var dataUrl = 'rest/opu/rs/appSoft';
                var type="put"
            }else{
                var param = {
                    funcId:this.formData1.funcId
                };
                if(this.formData1.funcId==undefined){
                    _this.sidebarActive = "baseInfo";
                    return;
                }
                var curFormData = _this.formData1;
                var dataUrl = 'rest/opu/rs/func/menu';
                var type="post"

            }

            var url = e.srcElement.dataset.url.split("agcloud");
            $(e.srcElement).addClass('active').siblings().removeClass("active");

            var nowUrl = "/agcloud"+url[1];
            if(e.srcElement.classList[0]=="small"){
                _this.$set(curFormData,'smallImgPath',nowUrl);
                param.smallImgPath=nowUrl
            }else if(e.srcElement.classList[0]=="middle"){
                _this.$set(curFormData,'middleImgPath',nowUrl);
                param.middleImgPath=nowUrl
            }else if(e.srcElement.classList[0]=="big"){
                _this.$set(curFormData,'bigImgPath',nowUrl);
                param.bigImgPath=nowUrl
            }else{
                _this.$set(curFormData,'hugeImgPath',nowUrl);
                param.hugeImgPath=nowUrl
            }

            request('opus-admin',{url:ctx+dataUrl,type: 'post',ContentType:'application/json',
                data: JSON.stringify(param) ,type:type
            },function (data) {
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });
                _this.sidebarActive = "baseInfo";

            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },

    },

    watch: {
        filterText: function(val) {
            this.$refs.treeLeft3.filter(val);
        },
        filterText1: function(val) {
            this.$refs.treeLeft1.filter(val);
            this.$refs.treeLeft2.filter(val);
        },
        keyword: function(val) {
            this.getAppList();
        },
        keyword2: function(val) {
            this.getAppFunc();
        },
    }
});

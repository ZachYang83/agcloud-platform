/*
 * @Author: ZL
 * @Date:   2019/05/15
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#portal',
  data: function () {
    var _that = this;
    var checkSame = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== _that.editPasswordData.newPassword) {
        callback(new Error('两次输入密码不一致！'));
      } else {
        callback();
      }
    };
    var checkDiffer = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value == _that.editPasswordData.oldPassword) {
        callback(new Error('新密码和原密码不能相同！'));
      } else {
        callback();
      }
    };
    var checkOldPass = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入原密码'));
      } else {
        request('opus-admin', {
          url: ctx + 'opus/front/om/users/passwordCheckout',
          data: {
            oldPassword: value
          },
          type: 'get'
        }, function (data) {
          if (data.success) {
            callback();
          } else {
            callback(new Error(data.message ? data.message : '原密码错误'));
          }
        })
      }
    };
    return {

      headerData: [], // 头部菜单所有data
      leftData: [], // 左侧菜单data
      headerActive: 0, // 头部菜单active状态
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth), //当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      showMenuMore: false, // 展示更多菜单默认隐藏
      menuCount: 1, // 头部菜单展示条数 menuCount+1
      loginName: '', // 用户登陆名
      userName: '', // 用户名
      userId: '', // 用户id
      activeTabIframe: '', // 顶部选项卡 绑定active
      activeTabData: [], // 顶部选项卡 所有展示的tab
      tabIndex: [], // 顶部选项卡 所有展示的tab的key
      hideHeaderData: [], // 头部菜单展示data
      headerDataShow: [], // 头部菜单隐藏data
      showMoreItem: false, // 是否展示隐藏的头部菜单
      leftMenuActive: 0, // 左侧菜单active
      //editUserInfoFlag: false, // 是否展示个人信息topbar
      showUserInfo: false, // 是否展示个人信息弹窗
      editPasswordFlag: false, // 是否展示修改密码弹窗
      editPasswordData: {
        oldPassword: '',
        newPassword: '',
        newPasswordCheck: ''
      }, // 修改密码data
      newPasswordCheck: '', // 再次确认新密码
      editPasswordRule: {
        oldPassword: [{
          required: true,
          validator: checkOldPass,
          trigger: 'blur'
        }, ],
        newPassword: [{
          validator: checkDiffer,
          required: true,
          trigger: 'blur'
        }],
        newPasswordCheck: [{
          validator: checkSame,
          required: true,
          trigger: 'blur'
        }]
      }, // 修改密码校验
      topOrgId: '',
      userSex: '',
      userEmail: '',
      showTabOpt: false,
      tabOptTop: '',
      tabOptleft: '',
      tabId: '',
      tabKey: '',
      //侧边栏展开
      leftClosed: false,
      isFullScreen: true,
      intervalTime: null, // 定时获取消息条数周期
      messgeCount: 0,
      ctx: ctx,
      portalTitle: null,
      portalLogoUrl: null,


      isHasMenu: true, //蓝图 --> 无右侧导航栏+tab
      isNomenuArr: [],
      isCurIframe: {},
      defaultActive: '0', //初始化默认选中的0, 0-0
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener("resize", function () {
      vm.curWidth = document.documentElement.clientWidth;
      vm.curHeight = document.documentElement.clientHeight;
      vm.setHeaderShow();
      if (!_that.checkFull()) {
        _that.isFullScreen = true;
      } else {
        _that.isFullScreen = false;
      }
    });
    //在document挂载onlick事件
    document.addEventListener("click", this.displayMenuPopover)
    // 监听子页面的传递过来的数据
    if (window.addEventListener) {
      window.addEventListener('message', _that.onMessage, false);
    } else {
      if (window.attachEvent) {
        window.attachEvent("onmessage", _that.onMessage);
      }
    }
    _that.getUserIndo();
    _that.getCountAoaMsgRangeForMyReceive();
    /*setInterval(function () {
      _that.getCountAoaMsgRangeForMyReceive();
    }, 30000);*/
    //获取门户标题及logo url
    _that.initPortal();
  },
  watch: {
    intervalTime: function () {
      var _that = this;
      setInterval(function () {
        _that.getViewDataCountByViewCode();
      }, _that.intervalTime)
    }
  },
  methods: {
    //获取门户标题及logo url
    initPortal: function () {
      var _that = this;
      request('opus-front', {
        url: ctx + 'opus/front/config',
        type: 'get',
      }, function (data) {
        // if (data.success) {
        //   _that.portalTitle = data.content.title;
        //   if (_that.portalTitle != null)
        //     $("title").html(_that.portalTitle);
        //   _that.portalLogoUrl = data.content.logoUrl;
        // }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getUserIndo: function () { // 获取用户登陆信息
      var _that = this;
      request('opus-admin', {
        url: ctx + 'opus/front/om/users/currOpusLoginUser',
        // url: '../../../../../static/agcloud/framework/ui-schemes/default/js/user_1.json',
        type: 'get',
      }, function (data) {
        if (data.success) {
          _that.loginName = data.content.user.loginName;
          _that.userName = data.content.user.userName;
          _that.userId = data.content.user.userId;
          _that.topOrgId = data.content.currentOrgId
          _that.getHeaderData();
          _that.getUserAllInfo(data.content.user.loginName);
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getUserAllInfo: function (loginName) { // 获取登录用户所有信息
      var _that = this;
      request('agx', {
        // url: '../../../../../static/agcloud/framework/ui-schemes/default/js/user_2.json',
        url: ctx + 'opus/front/om/users',
        type: 'get',
        data: {
          loginName: loginName
        }
      }, function (data) {
        if (data.success) {
          _that.userEmail = data.content.userEmail;
          _that.userSex = data.content.userSex;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getHeaderData: function () { // 获取菜单数据
      var _that = this;
      var props = {
        isTree: 'true',
        netName: '前端网络入口',
        tmnId: '1',
        topOrgId: _that.topOrgId,
        userId: _that.userId
      };
      var _that = this;
      request('opus-admin', {
        url: ctx + 'opus/front/om/users/user/' + _that.userId + '/allMenus',
        type: 'get',
        data: props
      }, function (data) {
        _that.headerData = data.content;
        if (data.content && data.content.length > 0) {
          if (data.content[0].childrenList) {
            _that.leftData = data.content[0].childrenList;
            _that.activeTabData.push({
              menuName: _that.leftData[0].menuName,
              id: _that.leftData[0].id,
              menuOuterUrl: _that.leftData[0].menuOuterUrl
            });
            _that.tabIndex.push(_that.leftData[0].id);

            //第一个有侧边栏
            _that.headerChangeMenu(data.content[0], 0, '');

          } else {
            var o = {
              menuName: data.content[0].menuName,
              id: data.content[0].id,
              menuOuterUrl: data.content[0].menuOuterUrl
            };
            _that.isCurIframe = o;
            _that.isNomenuArr.push(o);

          }

          _that.$nextTick(function () {
            _that.menuAddActive();
          });
          _that.getViewDataCountByViewCode();
        }
        _that.setHeaderShow();
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    setHeaderShow: function () { // 设置头部可展示菜单长度
      var _that = this;
      var logoWidth = $('.protal-header .logo-box').outerWidth(true);
      var topbarWidth = $('.user-info-box').outerWidth(true);
      var width = _that.curWidth - logoWidth - topbarWidth;
      if (_that.headerData !== null && _that.headerData.length > 0) {
        if (width <= 150 && _that.headerData.length > 0) {
          _that.showMenuMore = true;
          _that.menuCount = -1;
        } else if (width <= 360 && width > 150 && _that.headerData.length > 1) {
          _that.showMenuMore = true;
          _that.menuCount = 0;
        } else if (width <= 520 && width > 360 && _that.headerData.length > 2) {
          _that.showMenuMore = true;
          _that.menuCount = 1;
        } else if (width <= 680 && width > 520 && _that.headerData.length > 3) {
          _that.showMenuMore = true;
          _that.menuCount = 2;
        } else if (width <= 920 && width > 680 && _that.headerData.length > 4) {
          _that.showMenuMore = true;
          _that.menuCount = 3;
        } else if (width <= 1200 && width > 920 && _that.headerData.length > 6) {
          _that.showMenuMore = true;
          _that.menuCount = 5;
        } else if (width > 1200 && width <= 1320 && _that.headerData.length > 8) {
          _that.showMenuMore = true;
          _that.menuCount = 7;
        } else if (width > 1320 && _that.headerData.length > 9) {
          _that.showMenuMore = true;
          _that.menuCount = 8;
        } else {
          _that.showMenuMore = false;
          _that.menuCount = _that.headerData.length;
        }
      } else {
        _that.showMenuMore = false;
        if (_that.headerData) {
          _that.menuCount = _that.headerData.length;
        }
      }
      _that.initMenuCount(_that.headerData, _that.menuCount)
    },
    initMenuCount: function (eleLi, n) { // 重置头部菜单
      if (eleLi) {
        this.headerDataShow = eleLi.slice(0, n);
        this.hideHeaderData = eleLi.slice(n)
      }
    },
    // 头部菜单点击事件
    headerChangeMenu: function (data, index, e) {
      var _that = this;

      //无侧边栏，tab
      if (!data.childrenList) {
        _that.isHasMenu = true;

        //点击头部 当前页面刷新iframe（针对单个链接）
        if (e && _that.headerActive === index) {
          if (data.menuOuterUrl.indexOf("?") != -1) {
            $('#' + _that.isCurIframe.id)[0].src = data.menuOuterUrl + "&time=" + new Date().getTime();
          } else {
            $('#' + _that.isCurIframe.id)[0].src = data.menuOuterUrl + "?time=" + new Date().getTime();
          }
        }
        _that.headerActive = index;
        _that.isCurIframe = data;

        var ids = [];
        for (var i = 0; i < _that.isNomenuArr.length; i++) {
          ids.push(_that.isNomenuArr[i].id);
        }
        if (ids.toString().indexOf(data.id) === -1) {
          _that.isNomenuArr.push(data);
        }

      } else {
        //有侧边栏，tab
        _that.isHasMenu = false;
        _that.headerActive = index;
        _that.leftData = data.childrenList;
        _that.showMoreItem = false;
        _that.leftMenuActive = -1;
        _that.getViewDataCountByViewCode();

        $(e.srcElement).addClass('m-menu__item--active').siblings('.m-menu__item--active').removeClass('m-menu__item--active');
        $(e.srcElement).parents('.m-menu__item').addClass('m-menu__item--active').siblings('.m-menu__item--active').removeClass('m-menu__item--active');

        var curArr = _that.leftData[0];
        if (!!curArr.childrenList) {
          curArr = curArr.childrenList[0];
          _that.defaultActive = '0-0'; //2级菜单
        } else {
          _that.defaultActive = '0'; //默认第一个
        }

        var curTab = {
          menuName: curArr.menuName,
          id: curArr.id,
          menuOuterUrl: curArr.menuOuterUrl,
            url:''
        }

        if (_that.tabIndex.indexOf(curArr.id) == -1) {
            //新增查看接口API
            var str = curArr.menuOuterUrl;
            var url = str.substring(str.lastIndexOf("/agsupport"), str.length);
            request('', {
                url: ctx + 'agsupport/restfulswagger/getRestFulSwaggerUrl',
                data: { url: url },
                type: 'get'
            }, function (res) {
                if (res.success) {
                    curTab.url = res.content;
                }
                _that.activeTabData.push(curTab);
                _that.tabIndex.push(curArr.id);
            }, function () {
                _that.activeTabData.push(curTab);
                _that.tabIndex.push(curArr.id);
            });
        }
        _that.activeTabIframe = curArr.id;
        _that.$nextTick(function () {
          _that.menuAddActive();
        })
      }

    },
    addTab: function (ev, data, activeTab, ind) { // 点击新增顶部tab
     var _that = this;
      ev.cancelBubble = true;
      this.leftMenuActive = ind;
      if (!data.childrenList && (this.tabIndex.indexOf(data.id) == -1)) {

        var curTab = {
            menuName: data.menuName,
            id: data.id,
            menuOuterUrl: data.menuOuterUrl,
            url:''
        }
          //新增查看接口API
          var str = data.menuOuterUrl;
          var url = str.substring(str.lastIndexOf("/agsupport"), str.length);
          request('', {
              url: ctx + 'agsupport/restfulswagger/getRestFulSwaggerUrl',
              data: { url: url },
              type: 'get'
          }, function (res) {
              if (res.success) {
                  curTab.url = res.content;
              }
              _that.activeTabData.push(curTab);
              _that.activeTabIframe = data.id;
              _that.menuAddActive();
              _that.tabIndex.push(data.id);
          }, function () {
              _that.activeTabData.push(curTab);
              _that.activeTabIframe = data.id;
              _that.menuAddActive();
              _that.tabIndex.push(data.id);
          });
      } else {
        if (this.activeTabIframe === data.id) {
          $('#pane-' + this.activeTabIframe).find('iframe')[0].contentWindow.location.reload(true);
        }
        this.activeTabIframe = data.id;
        this.menuAddActive();
      }
    },
    removeTab: function (targetName) { // 移除顶部tab
      var tabs = this.activeTabData;
      var activeName = this.activeTabIframe;
      var _that = this;
      this.showTabOpt = false;
      if (activeName === targetName) {
        tabs.forEach(function (tab, index) {
          if (tab.id === targetName) {
            var nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.id;
            }
          }
        });
      }
      _that.activeTabIframe = activeName;
      _that.menuAddActive();
      _that.activeTabData = tabs.filter(function (tab) {
        return tab.id !== targetName
      });
      _that.tabIndex = [];
      _that.activeTabData.forEach(function (e) {
        _that.tabIndex.push(e.id);
      });
    },
    tabMouseRightClick: function (ev) { // 顶部tab 右击事件
      ev.cancelBubble = true;
      if (ev.path) {
        this.tabId = ev.path[0].id;
        this.tabKey = ev.path[0].id.split('tab-')[1];
      } else {
        this.tabId = ev.srcElement.id
        this.tabKey = ev.srcElement.id.split('tab-')[1];
      }
      if (!(this.tabId.indexOf('tab-MENU_') > -1)) {
        return false;
      } else {
        this.showTabOpt = true;
        this.tabOptTop = ev.offsetY;
        if ($('body').hasClass('demo2')) {
          this.tabOptleft = ev.clientX - 90;
        } else {
          this.tabOptleft = ev.clientX - 170;
        }
        this.activeTabIframe = this.tabKey;
        this.menuAddActive();
      }
    },
    closeOther: function () { // 移除其他顶部tab
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(start, start + 1);
      this.activeTabData = this.activeTabData.slice(start, start + 1);
      this.showTabOpt = false;
    },
    closeLeft: function () { // 移除左边顶部tab
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(start);
      this.activeTabData = this.activeTabData.slice(start);
      this.showTabOpt = false;
    },
    closeRight: function () { // 移除右边顶部tab
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(0, start + 1);
      this.activeTabData = this.activeTabData.slice(0, start + 1);
      this.showTabOpt = false;
    },
    //查看接口
    toAPIUrl: function () {
      var start = this.tabIndex.indexOf(this.tabKey);
      var part_url = this.activeTabData[start].url;
      if(part_url){
          var url = ctx + part_url;
          window.open(url);
      }else{
          this.$message({
              message: '此页面暂无接口文档，待添加！',
              type: 'success'
          });
      }
    },
    changeTab: function () { // tab点击事件
      var tabEle = $('.el-tabs__content .el-tab-pane');
      var tabEleLen = tabEle.length;
      var _that = this;
      _that.menuAddActive();
    },
    changeLeftClosed: function (flag) { // 左边栏展开收缩事件
      if (flag) {
        this.leftClosed = false
      } else {
        this.leftClosed = true
      }
    },
    displayMenuPopover: function () { // 隐藏tab操作窗口
      this.showTabOpt = false;
    },
    onMessage: function (e) {
      var _that = this
      var data = e.data
      if (!data.vueDetected && data.id != undefined) {
        data.menuOuterUrl = e.origin + data.menuOuterUrl
        _that.addTab(e, data, 'MENU_' + data.id, _that.leftMenuActive++);
      }
    },
    logout: function (e) { // 退出登陆
      e.cancelBubble = true;
      var _that = this;
      confirmMsg('', '确定要退出登陆吗？', function () {
        request('opus-admin', {
          url: ctx + 'opus/front/om/users/listLogoutUrl',
          type: 'get',
          data: {
            netName: '前端网络入口',
            orgId: _that.topOrgId,
            userId: _that.userId
          }
        }, function (data) {
          if (data.success) {
            if (data.content.length > 0) {
              for (let i = 0; i < data.content.length; i++) {
                var a = document.createElement('iframe');
                a.src = data.content[i];
                a.style.display = 'none';
                document.body.appendChild(a);
              }
            }
            window.location.href = ctx + 'logout';
          }
        }, function (msg) {
          window.location.reload();
          // alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      })
    },
    editPasswordSubmit: function () { // 修改密码
      var _that = this;
      _that.$refs['editPasswordData'].validate(function (valid) {
        if (valid) {
          var props = {
            newPassword: _that.editPasswordData.newPassword,
            oldPassword: _that.editPasswordData.oldPassword,
          };
          request('opus-admin', {
            url: ctx + 'opus/front/om/users/password',
            data: props,
            type: 'put'
          }, function (data) {
            if (data.success) {
              _that.$message({
                message: '密码修改成功',
                type: 'success'
              });
              _that.editPasswordFlag = false;
            } else {
              _that.$message({
                message: data.message ? data.message : '密码修改失败',
                type: 'error'
              });
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '密码修改失败',
              type: 'error'
            });
          })
        } else {
          alertMsg('', '请输入完整且正确的信息', '关闭', 'warning', true);
          return false;
        }
      });
    },
    clearDialogData: function (formName) { // 清空表单
      this.$refs[formName].resetFields();
    },
    toggleFullScreen: function () { // 切换全屏
      if (this.isFullScreen) {
        this.enterfullscreen()
      } else {
        this.exitfullscreen();
      }
    },
    enterfullscreen: function () { //进入全屏
      var docElm = document.documentElement;
      //W3C
      if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
      }
      //FireFox
      else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
      }
      //Chrome等
      else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
      }
      //IE11
      else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
        var widthFull = $(window).width();
        $('#wrapper').css('width', widthFull);
      } else if (typeof window.ActiveXObject !== "undefined") { //for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
          wscript.SendKeys("{F11}");
        }
      }
      this.isFullScreen = true;
    },
    exitfullscreen: function () { //退出全屏
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      } else if (typeof window.ActiveXObject !== "undefined") { //for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
          wscript.SendKeys("{F11}");
        }
      }
      this.isFullScreen = false;
    },
    checkFull: function () { // 判断是否全屏
      var isFull = document.fullscreenEnabled || window.fullScreen || document.webkitIsFullScreen || document.msFullscreenEnabled;
      if (isFull === undefined) {
        isFull = false;
      }
      return isFull
    },
    // showEditUserInfo: function () { // 是否展示login-info
    //   this.editUserInfoFlag = !this.editUserInfoFlag;
    // },
    menuAddActive: function () {
      $('.protal-content-left .el-menu-item').removeClass('is-active');
      $('#' + this.activeTabIframe).addClass('is-active').siblings('.el-menu-item').removeClass('is-active');
    },
    getViewDataCountByViewCode: function () {
      var totalCount = 0;
      var _that = this;
      // debugger
      _that.leftData.map(function (e) {
        if (e.openMenuCount) {
          _that.intervalTime = e.menuCountInterval;
          request('opus-admin', {
            url: ctx + e.menuCountUrl,
            type: 'get',
          }, function (data) {
            if (data) {
              e.subCount = data;
            }
          }, function (msg) {
            _that.$message({
              message: '服务请求失败',
              type: 'error'
            });
          });
        } else if (e.childrenList) {
          //  debugger
          // _that.getViewDataCountByViewCode();
        }
      });
    },
    getCountAoaMsgRangeForMyReceive: function () {
      var _that = this;
      request('opus-admin', {
        url: ctx + 'rest/aoa/msg/range/getCountAoaMsgRangeForMyReceiveUnReaded',
        type: 'post',
      }, function (data) {
        _that.messgeCount = data.content;
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    }
  }
});
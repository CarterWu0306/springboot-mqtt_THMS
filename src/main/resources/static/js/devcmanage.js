var flag=0;
function sel() {

    flag=1;

    var devcNameParam="";
    var typeParam="";
    var devcStatusParam="";
    var alarmStatusParam="";

    var typeIndex = document.getElementById("type").selectedIndex; // 选中索引
    var devcStatusIndex = document.getElementById("devcStatus").selectedIndex; // 选中索引
    var alarmStatusIndex = document.getElementById("alarmStatus").selectedIndex; // 选中索引

    var devcName = document.getElementById("devcName").value;
    var type = document.getElementById("type").options[typeIndex].text;
    var devcStatus = document.getElementById("devcStatus").options[devcStatusIndex].text;
    var alarmStatus = document.getElementById("alarmStatus").options[alarmStatusIndex].text;

    if (devcName!="请选择设备名称") {
        devcNameParam=devcName;
    }
    if (type!="请选择设备类型") {
        typeParam=type;
    }
    if (devcStatus!="请选择设备状态") {
        devcStatusParam=devcStatus;
    }
    if (alarmStatus!="请选择警报状态") {
        alarmStatusParam=alarmStatus;
    }


    layui.use('table', function(){
        var table = layui.table;

        table.render({
            elem: '#devc'
            ,url:'selDevcManageInfo'+'?devcNameParam='+devcNameParam+'&typeParam='+typeParam+'&devcStatusParam='+devcStatusParam+'&alarmStatusParam='+alarmStatusParam
            ,where:{}
            ,id: 'listReload1'
            ,height : "full-125"
            ,limits : [10,15,20,25,30]
            ,limit : 20
            ,cols: [[
                {type:'checkbox'}
                ,{field:'name', width:150, title: '设备名称', align:"center"}
                ,{field:'clientId', width:150, title: '客户端id',align:"center"}
                ,{field:'code', width:100, title: '设备编号',align:"center"}
                ,{field:'type', width:100, title: '设备类型', align:"center"}
                ,{field:'status', width:100, title: '设备状态', align:"center"}
                ,{field:'alarmStatus', width:100, title: '警报状态', align:"center"}
                ,{field:'dutyPerson', width:90, title: '负责人', align:"center"}
                ,{field:'createTime', width:230, title: '创建时间',sort: true,align:"center",templet: function(d) {
                        return formatDate(d.createTime);
                    }}
                ,{field:'createPerson', width:90,title: '创建人',align:"center"}
                ,{field:'memo',width:120,title: '备注',align:"center"}
                ,{field:'descript',width:300, title: '设备描述'}
                ,{field:'memo',title: '操作',align:"center",toolbar: '#operation'}
            ]]
            ,page: true
        });

        //监听工具条
        table.on('tool(demo)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit'){
                var deviceId=obj.data.deviceId;
                var devcName=obj.data.name;
                var clientId=obj.data.clientId;
                var code=obj.data.code;
                var type=obj.data.type;
                var status=obj.data.status;
                var alarmStatus=obj.data.alarmStatus;
                var createPerson=obj.data.createPerson;
                var dutyPerson=obj.data.dutyPerson;
                var descript=obj.data.descript;


                var index = layui.layer.open({
                    title : "编辑设备",
                    type : 2,
                    id: 'layerDemo',
                    area: ['600px', '500px'],
                    shade: 0.3,
                    skin:'demo-class',
                    content : '/deviceAdd?deviceId='+deviceId+'&devcName='+devcName+'&clientId='+clientId+'&code='+code+'&type='+type+'&status='+status+'&alarmStatus='+alarmStatus+'&createPerson='+createPerson+'&dutyPerson='+dutyPerson+'&descript='+descript,
                    success : function(layero, index){
                    }

                })
            } else if(obj.event === 'del'){
                layer.confirm('真的删除行么', function(index){
                    //发送删除请求
                    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                    httpRequest.open('POST', 'delDevice', true); //第二步：打开连接
                    httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                    httpRequest.send('deviceId='+data.deviceId);//发送请求 将情头体写在send中

                    obj.del();
                    layer.close(index);
                });
            }
        });


        var listVal = [];
        table.on('checkbox(demo)', function () {
            listVal.splice(0,listVal.length);
            var checkStatus = table.checkStatus('listReload1');
            checkData = checkStatus.data;
            for ( var i = 0; i <checkData.length; i++){
                listVal[i]=checkData[i];
            }
        });

        layui.$('.delDevc').on('click', function(){
            if (listVal.length==0){
                layer.alert('所选内容为空');
            }else {
                layer.confirm('确认删除', function(index){
                    for (var i = 0; i < listVal.length; i++) {
                        //批量删除
                        var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                        httpRequest.open('POST', 'delDevice', true); //第二步：打开连接
                        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                        httpRequest.send('deviceId=' + listVal[i].deviceId);//发送请求 将情头体写在send中
                    }
                    if (i === listVal.length&&i>0) {
                        setTimeout(function () {
                            window.location.reload();
                        }, 500);
                    }
                });
            }
        });

    });

}

layui.use('table', function(){
    var table = layui.table;

    table.render({
        elem: '#devc'
        ,url:'selDevcManageInfo'
        ,where:{}
        ,id: 'listReload2'
        ,height : "full-125"
        ,limits : [10,15,20,25,30]
        ,limit : 20
        ,cols: [[
            {type:'checkbox'}
            ,{field:'name', width:150, title: '设备名称', align:"center"}
            ,{field:'clientId', width:150, title: '客户端id',align:"center"}
            ,{field:'code', width:100, title: '设备编号',align:"center"}
            ,{field:'type', width:100, title: '设备类型', align:"center"}
            ,{field:'status', width:100, title: '设备状态', align:"center"}
            ,{field:'alarmStatus', width:100, title: '警报状态', align:"center"}
            ,{field:'dutyPerson', width:90, title: '负责人', align:"center"}
            ,{field:'createTime', width:230, title: '创建时间',sort: true,align:"center",templet: function(d) {
                    return formatDate(d.createTime);
                }}
            ,{field:'createPerson', width:90,title: '创建人',align:"center"}
            ,{field:'memo',width:120,title: '备注',align:"center"}
            ,{field:'descript',width:300, title: '设备描述'}
            ,{field:'memo',title: '操作',align:"center",toolbar: '#operation'}
        ]]
        ,page: true
    });

    //监听工具条
    table.on('tool(demo)', function(obj){
        var data = obj.data;
        if(obj.event === 'edit'){
            var deviceId=obj.data.deviceId;
            var devcName=obj.data.name;
            var clientId=obj.data.clientId;
            var code=obj.data.code;
            var type=obj.data.type;
            var status=obj.data.status;
            var alarmStatus=obj.data.alarmStatus;
            var createPerson=obj.data.createPerson;
            var dutyPerson=obj.data.dutyPerson;
            var descript=obj.data.descript;


            var index = layui.layer.open({
                title : "编辑设备",
                type : 2,
                id: 'layerDemo',
                area: ['600px', '500px'],
                shade: 0.3,
                skin:'demo-class',
                content : '/deviceAdd?deviceId='+deviceId+'&devcName='+devcName+'&clientId='+clientId+'&code='+code+'&type='+type+'&status='+status+'&alarmStatus='+alarmStatus+'&createPerson='+createPerson+'&dutyPerson='+dutyPerson+'&descript='+descript,
                success : function(layero, index){
                }

            })
        } else if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                //发送删除请求
                var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                httpRequest.open('POST', 'delDevice', true); //第二步：打开连接
                httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                httpRequest.send('deviceId='+data.deviceId);//发送请求 将情头体写在send中

                obj.del();
                layer.close(index);
            });
        }
    });

    var listVal = [];
    table.on('checkbox(demo)', function () {
        listVal.splice(0,listVal.length);
        var checkStatus = table.checkStatus('listReload2');
        checkData = checkStatus.data;
        for ( var i = 0; i <checkData.length; i++){
            listVal[i]=checkData[i];
        }
    });


    layui.$('.delDevc').on('click', function(){
        if (listVal.length==0&&flag==0){
            layer.alert('所选内容为空');
        }else {
            layer.confirm('确认删除', function(index){
                for (var i = 0; i < listVal.length; i++) {
                    //批量删除
                    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                    httpRequest.open('POST', 'delDevice', true); //第二步：打开连接
                    httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                    httpRequest.send('deviceId=' + listVal[i].deviceId);//发送请求 将情头体写在send中
                }
                if (i === listVal.length&&i>0) {
                    setTimeout(function () {
                        window.location.reload();
                    }, 500);
                }
            });
        }
    });

});

function add0(m){return m<10?'0'+m:m }
function formatDate(timestamp)
{
    //timestamp是整数，否则要parseInt转换,不会出现少个0的情况
        var time = new Date(timestamp);
        var year = time.getFullYear();
        var month = time.getMonth()+1;
        var date = time.getDate();
        var hours = time.getHours();
        var minutes = time.getMinutes();
        var seconds = time.getSeconds();
        return year+'-'+add0(month)+'-'+add0(date)+' '+add0(hours)+':'+add0(minutes)+':'+add0(seconds);
}

function add() {
    var deviceId=-1;

    var index = layui.layer.open({
        title : "添加设备",
        type : 2,
        id: 'layerDemo',
        area: ['600px', '500px'],
        shade: 0.3,
        skin:'demo-class',
        content : '/deviceAdd?deviceId='+deviceId,
        success : function(layero, index){
        }

    })
}


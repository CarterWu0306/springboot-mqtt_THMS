var flag=0;
function sel() {

    flag=1;
    var deviceParam="";
    var typeParam="";
    var startParam="";
    var endParam="";
    var statusParam="";

    var typeIndex = document.getElementById("type").selectedIndex; // 选中索引
    var statusIndex = document.getElementById("status").selectedIndex; // 选中索引

    var devcName = document.getElementById("devcName").value;
    var type = document.getElementById("type").options[typeIndex].text;
    var status = document.getElementById("status").options[statusIndex].text;
    var daterange = document.getElementById("daterange").value;

    if (devcName!="请选择设备名称") {
        deviceParam=devcName;
    }
    if (type!="请选择警报类型") {
        typeParam=type;
    }
    if (status!="请选择警报状态") {
        statusParam=status;
    }

    if (!(daterange == "" || daterange == null || daterange == undefined)){
        var strs = daterange.split(" - ")
        var startTime = strs[0];
        var endTime = strs[1];
        startParam=startTime;
        endParam=endTime;
    }


    layui.use('table', function(){
        var table = layui.table;

        table.render({
            elem: '#alarm'
            ,url:'selAlarmHistory'+'?deviceParam='+deviceParam+'&typeParam='+typeParam+'&startParam='+startParam+'&endParam='+endParam+'&statusParam='+statusParam
            ,where:{}
            ,id: 'listReload1'
            ,height : "full-125"
            ,limits : [10,15,20,25,30]
            ,limit : 20
            ,cols: [[
                {type:'checkbox'}
                ,{field:'alarmId', width:80, title: '序号', sort: true,align:"center"}
                ,{field:'type', width:180, title: '警报类型', align:"center"}
                ,{field:'devcName', width:200, title: '警报设备',align:"center"}
                ,{field:'alarmText', width:400, title: '警报内容', align:"center"}
                ,{field:'startTime', width:180, title: '起始时间',sort: true,align:"center",templet: function(d) {
                        return formatDate(d.startTime);
                    }}
                ,{field:'endTime', width:180, title: '结束时间',align:"center",templet: function(d) {
                        if (d.endTime!=null){
                            return formatDate(d.endTime);
                        }else{
                            return "未结束"
                        }
                    }}
                ,{field:'holdTime', width:180,title: '持续时间',align:"center"}
                ,{field:'status',width:100, title: '警报状态',align:"center"}
                ,{field:'operation', title: '操作',align:"center",toolbar: '#operation'}
            ]]
            ,page: true
        });

        //监听工具条
        table.on('tool(demo)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm('真的删除行么', function(index){
                    //发送删除请求
                    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                    httpRequest.open('POST', 'delAlarm', true); //第二步：打开连接
                    httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                    httpRequest.send('alarmId='+data.alarmId);//发送请求 将情头体写在send中

                    obj.del();
                    layer.close(index);
                });
            }else if(obj.event === 'selinfo') {
                var alarmId = data.alarmId;
                var index = layui.layer.open({
                    title : "设备详情",
                    type : 2,
                    id: 'layerDemo',
                    area: ['600px', '500px'],
                    shade: 0.3,
                    skin:'demo-class',
                    content : '/selDeviceInfo?alarmId='+alarmId,
                    success : function(layero, index){
                    }

                })
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

        layui.$('.ignore').on('click', function(){
            if (listVal.length==0){
                layer.alert('所选内容为空');
            }else {
                layer.confirm('确认忽略', function(index) {
                    for (var i = 0; i < listVal.length; i++) {
                        //批量删除
                        var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                        httpRequest.open('POST', 'delAlarm', true); //第二步：打开连接
                        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                        httpRequest.send('alarmId=' + listVal[i].alarmId);//发送请求 将情头体写在send中
                    }
                    if (i === listVal.length && i > 0) {
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
        elem: '#alarm'
        ,url:'selAlarmHistory'
        ,where:{}
        ,id: 'listReload2'
        ,height : "full-125"
        ,limits : [10,15,20,25,30]
        ,limit : 20
        ,cols: [[
            {type:'checkbox'}
            ,{field:'alarmId', width:80, title: '序号', sort: true,align:"center"}
            ,{field:'type', width:180, title: '警报类型', align:"center"}
            ,{field:'devcName', width:200, title: '警报设备',align:"center"}
            ,{field:'alarmText', width:400, title: '警报内容', align:"center"}
            ,{field:'startTime', width:180, title: '起始时间',sort: true,align:"center",templet: function(d) {
                    return formatDate(d.startTime);
                }}
            ,{field:'endTime', width:180, title: '结束时间',align:"center",templet: function(d) {
                if (d.endTime!=null){
                    return formatDate(d.endTime);
                }else{
                    return "未结束"
                }
                }}
            ,{field:'holdTime', width:180,title: '持续时间',align:"center"}
            ,{field:'status',width:100, title: '警报状态',align:"center"}
            ,{field:'operation', title: '操作',align:"center",toolbar: '#operation'}
        ]]
        ,page: true
    });

    //监听工具条
    table.on('tool(demo)', function(obj){
        var data = obj.data;
        /*if(obj.event === 'solve'){
            layer.confirm('是否已解决', function(index){
                //发送更新请求
                var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                httpRequest.open('POST', 'updAlarm', true); //第二步：打开连接
                httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                httpRequest.send('alarmId='+data.alarmId);//发送请求 将情头体写在send中
                layer.close(index);
                location.reload();
            });
        } else */
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                //发送删除请求
                var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                httpRequest.open('POST', 'delAlarm', true); //第二步：打开连接
                httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                httpRequest.send('alarmId='+data.alarmId);//发送请求 将情头体写在send中

                obj.del();
                layer.close(index);
            });
        }else if(obj.event === 'selinfo') {
            var alarmId = data.alarmId;
            var index = layui.layer.open({
                title : "设备详情",
                type : 2,
                id: 'layerDemo',
                area: ['600px', '500px'],
                shade: 0.3,
                skin:'demo-class',
                content : '/selDeviceInfo?alarmId='+alarmId,
                success : function(layero, index){
                }

            })
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


    /*layui.$('.resolve').on('click', function(){
        if (listVal.length==0&&flag==0){
            layer.alert('所选内容为空');
        }else{
            layer.confirm('确认解决', function(index) {
                for (var i = 0; i < listVal.length; i++) {
                    //批量解决
                    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                    httpRequest.open('POST', 'updAlarm', true); //第二步：打开连接
                    httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                    httpRequest.send('alarmId=' + listVal[i].alarmId);//发送请求 将情头体写在send中
                }
                if (i === listVal.length && i > 0) {
                    setTimeout(function () {
                        window.location.reload();
                    }, 500);
                }
            });
        }
    });*/
    layui.$('.ignore').on('click', function(){
        if (listVal.length==0&&flag==0){
            layer.alert('所选内容为空');
        }else {
            layer.confirm('确认忽略', function(index) {
                for (var i = 0; i < listVal.length; i++) {
                    //批量删除
                    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
                    httpRequest.open('POST', 'delAlarm', true); //第二步：打开连接
                    httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
                    httpRequest.send('alarmId=' + listVal[i].alarmId);//发送请求 将情头体写在send中
                }
                if (i === listVal.length && i > 0) {
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
layui.use('laydate', function(){
    var laydate = layui.laydate;

    //日期时间范围
    laydate.render({
        elem: '#daterange'
        ,type: 'datetime'
        ,range: true
    });
});

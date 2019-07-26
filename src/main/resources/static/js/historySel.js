function sel() {

    var deviceParam="";
    var typeParam="";
    var startParam="";
    var endParam="";

    var typeIndex = document.getElementById("type").selectedIndex; // 选中索引

    var devcName = document.getElementById("devcName").value;
    var type = document.getElementById("type").options[typeIndex].text;
    var daterange = document.getElementById("daterange").value;


    if (devcName!="请输入设备名称") {
        deviceParam=devcName;
    }
    if (type!="请选择数据类型") {
        typeParam=type;
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
            elem: '#history'
            ,url:'selhistory'+'?deviceParam='+deviceParam+'&typeParam='+typeParam+'&startParam='+startParam+'&endParam='+endParam
            ,where:{

            }
            ,height : "full-125"
            ,limits : [10,15,20,25,30]
            ,limit : 20
            ,cols: [[
                {field:'id', width:80, title: '序号', sort: true,align:"center"}
                ,{field:'value', width:150, title: '数值', sort: true,align:"center"}
                ,{field:'type', width:150, title: '类型',align:"center"}
                ,{field:'devcName', width:300, title: '采集设备名称',align:"center"}
                ,{field:'topic', width:300, title: '主题',align:"center"}
                ,{field:'clientid', width:300, title: '采集设备客户端ID',align:"center"}
                ,{field:'time', title: '时间',align:"center",sort: true,templet: function(d){
                        return formatDate(d.time);
                    }}
            ]]
            ,page: true
        });
    });

}


layui.use('table', function(){
    var table = layui.table;

    table.render({
        elem: '#history'
        ,url:'selhistory'
        ,where:{

        }
        ,height : "full-125"
        ,limits : [10,15,20,25,30]
        ,limit : 20
        ,cols: [[
            {field:'id', width:80, title: '序号', sort: true,align:"center"}
            ,{field:'value', width:150, title: '数值', sort: true,align:"center"}
            ,{field:'type', width:150, title: '类型',align:"center"}
            ,{field:'devcName', width:300, title: '采集设备名称',align:"center"}
            ,{field:'topic', width:300, title: '主题',align:"center"}
            ,{field:'clientid', width:300, title: '采集设备客户端ID',align:"center"}
            ,{field:'time', title: '时间',align:"center",sort: true,templet: function(d){
                    return formatDate(d.time);
                }}
        ]]
        ,page: true
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
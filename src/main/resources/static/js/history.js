layui.use('laydate', function(){
    var laydate = layui.laydate;

    //日期时间范围
    laydate.render({
        elem: '#daterange'
        ,type: 'datetime'
        ,range: true
    });
});

var myChart = echarts.init(document.getElementById('history'));


    $(".search").click(function() {
        data1.splice(0,data1.length);
        data2.splice(0,data2.length);
        data3.splice(0,data3.length);

        var deviceParam="";
        var startParam="";
        var endParam="";
        var typeText="";

        var typeIndex = document.getElementById("type").selectedIndex; // 选中索引
        var deviceText = document.getElementById("devcName").value; // 选中文本
        var typeContext = document.getElementById("type").options[typeIndex].text; // 选中文本
        var daterange = document.getElementById("daterange").value;


        if (deviceText!="请选择设备名称") {
            deviceParam=deviceText;
        }
        if (typeContext!="请选择数据类型"){
            typeText=typeContext;
        }
        if (!(daterange == "" || daterange == null || daterange == undefined)){
            var strs = daterange.split(" - ")
            var startTime = strs[0];
            var endTime = strs[1];
            startParam=startTime;
            endParam=endTime;
        }
        console.log(deviceParam);
        if (deviceParam!=""&&typeText!=""){
            $.ajax({
                type: "POST",
                url: 'showHistory?deviceParam=' + deviceParam + '&startParam=' + startParam + '&endParam=' + endParam,
                contentType: "application/x-www-form-urlencoded",
                dataType: "json",
                error: function (request) {

                },
                success: function (data) {
                    myChart.clear();
                    for(var index in data){
                        for (var key in data[index]) {
                            if (key==="type"&&data[index][key]==="00"){
                                data1.push(setDatas(data[index]["value"],data[index]["time"]));
                            }
                            if (key==="type"&&data[index][key]==="01"){
                                data2.push(setDatas(data[index]["value"],data[index]["time"]));
                            }
                            if (key==="type"&&data[index][key]==="02"){
                                data3.push(setDatas(data[index]["value"],data[index]["time"]));
                            }
                        }
                    }
                    if (typeText==="温湿度"){
                        myChart.setOption(option);
                    }else if (typeText==="六氟化硫浓度"){
                        myChart.setOption(option2);
                    }
                }
            });
        }

    });

function setDatas(num,time) {
    return{
        name: time,
        value:[
            time,
            num=num
        ]
    }
}

var data1 = [];
var data2 = [];
var data3 = [];


option = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            animation: false
        }
    },
    legend: {
        data:['温度','湿度']
    },
    xAxis: {
        type: 'time',
        splitLine: {
            show: false
        }
    },
    yAxis: [{
        type: 'value',
        color: "#d63145",
        max : 100,
        min : 0,
        splitNumber : 10,
        axisLabel : {
            formatter : '{value}℃'
        },
        boundaryGap: [0, '40%'],
        splitLine: {
            show: false
        }
    },
        {
            type: 'value',
            color: '#0180ff',
            max : 100,
            min : 0,
            axisLabel : {
                formatter : '{value}%/RH'
            },
            boundaryGap: ['-30%', '40%'],
            splitLine: {
                show: false
            }
        }],
    dataZoom:[{
      type:"inside",         //详细配置可见echarts官网
　　  }],
    series: [{
        name: '湿度',
        yAxisIndex : '1',//使用第一个y轴
        type: 'line',
        itemStyle:{
            normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
                        offset: 0, color: '#81befd' // 0% 处的颜色
                    }, {
                        offset: 0.5, color: '#e4f2ff' // 100% 处的颜色
                    }, {
                        offset: 1, color: '#fff' // 100% 处的颜色
                    }]
                ),  //背景渐变色
                lineStyle: {        // 系列级个性化折线样式
                    width: 3,
                    type: 'solid',
                    color: "#0180ff"
                }
            },
            emphasis: {
                color: '#0180ff',
                lineStyle: {        // 系列级个性化折线样式
                    width:2,
                    type: 'dotted',
                    color: "#0180ff" //折线的颜色
                }
            }
        },//线条样式
        symbolSize:10, //折线点的大小
        areaStyle: {normal: {}},
        showSymbol: false,
        hoverAnimation: false,
        formatter: '{b}\n{c}%',
        data: data1
    },
        {
            name: '温度',
            yAxisIndex : '0',//使用第一个y轴
            type: 'line',
            itemStyle:{
                normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
                            offset: 0, color: '#f88891' // 0% 处的颜色
                        }, {
                            offset: 0.5, color: '#fdbacc' // 100% 处的颜色
                        }, {
                            offset: 1, color: '#fff' // 100% 处的颜色
                        }]
                    ),  //背景渐变色
                    lineStyle: {        // 系列级个性化折线样式
                        width: 3,
                        type: 'solid',
                        color: "#d63145"
                    }
                },
                emphasis: {
                    color: '#d63145',
                    lineStyle: {        // 系列级个性化折线样式
                        width:2,
                        type: 'dotted',
                        color: "#d63145" //折线的颜色
                    }
                }
            },//线条样式
            symbolSize:10, //折线点的大小
            areaStyle: {normal: {}},
            showSymbol: false,
            hoverAnimation: false,
            data: data2
        }]
};

option2 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            animation: false
        }
    },
    legend: {
        data:['六氟化硫浓度']
    },
    xAxis: {
        type: 'time',
        splitLine: {
            show: false
        }
    },
    yAxis: [{
        type: 'value',
        color: "#d6932e",
        axisLabel : {
            formatter : '{value}uL/L'
        },
        boundaryGap: [0, '100%'],
        splitLine: {
            show: false
        }
    }],
    dataZoom:[{
        type:"inside",         //详细配置可见echarts官网
    }],
    series: [{
        name: '六氟化硫浓度',
        type: 'line',
        itemStyle:{
            normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
                        offset: 0, color: '#d69f48' // 0% 处的颜色
                    }, {
                        offset: 0.5, color: '#d6c17f' // 100% 处的颜色
                    }, {
                        offset: 1, color: '#fff' // 100% 处的颜色
                    }]
                ),  //背景渐变色
                lineStyle: {        // 系列级个性化折线样式
                    width: 3,
                    type: 'solid',
                    color: "#d6932e"
                }
            },
            emphasis: {
                color: '#d6932e',
                lineStyle: {        // 系列级个性化折线样式
                    width:2,
                    type: 'dotted',
                    color: "#d6932e" //折线的颜色
                }
            }
        },//线条样式
        symbolSize:10, //折线点的大小
        areaStyle: {normal: {}},
        showSymbol: false,
        hoverAnimation: false,
        data: data3
    }]
};
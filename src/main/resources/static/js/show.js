var myChart = echarts.init(document.getElementById('show'));

var device;
var Type;
var findclientid;

function select() {
    var typeObj = document.getElementById("type"); //定位id

    var typeindex = typeObj.selectedIndex; // 选中索引

    var text = document.getElementById("devcName").value; // 选中文本
    var typeText = typeObj.options[typeindex].text; // 选中文本

    device=text;
    Type=typeText;

    data1.splice(0,data1.length);
    data2.splice(0,data2.length);
    data3.splice(0,data3.length);

    myChart.clear();

    //发送请求
    // var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
    // httpRequest.open('POST', 'showInit', true); //第二步：打开连接
    // httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
    // httpRequest.send('devcName='+device);//发送请求 将情头体写在send中

    $.ajax({
        type: "POST",
        url: 'showInit?devcName=' + device,
        contentType: "application/x-www-form-urlencoded",
        dataType: "text",
        error: function (request) {

        },
        success: function (data) {
            findclientid=data;
        }
    });
}

function setDatas(num,time) {
    return{
        name: time,
        value:[
            time,
            num=num.replace("%","")
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
            boundaryGap: [0, '40%'],
            splitLine: {
                show: false
            }
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

function reNewChart(num,time,type,clientid) {
    if (clientid===findclientid) {
        if (Type==='温湿度'){
            if (type==='00'){
                if (data1.length>20){
                    data1.shift();
                }
                data1.push(setDatas(num,time));
            }else if(type==='01'){
                if (data2.length>20){
                    data2.shift();
                }
                data2.push(setDatas(num,time));
            }
            myChart.setOption(option);
        }else if (Type==='六氟化硫浓度'){
            if (type==='02'){
                if (data3.length>20){
                    data3.shift();
                }
                data3.push(setDatas(num,time));
            }
            myChart.setOption(option2);
        }
    }


};
<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
        <el-col :span="6">
          <el-card style="color: #409eff">
            <div>
              <i class="el-icon-user-solid" />
              用户总数
            </div>
            <div v-html="userCont" style="padding: 10px 0; text-align: center; font-weight: bold"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="color: #f56c6c">
            <div>
              <i class="el-icon-money" />
              学生人数
            </div>
            <div v-html="stuContent" style="padding: 10px 0; text-align: center; font-weight: bold"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="color: #67c23a">
            <div>
              <i class="el-icon-bank-card" />
              老师人数
            </div>
            <div v-html="teaContent" style="padding: 10px 0; text-align: center; font-weight: bold"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="color: #e6a23c">
            <div>
              <i class="el-icon-s-shop" />
              课程量
            </div>
            <div v-html="curContent" style="padding: 10px 0; text-align: center; font-weight: bold"></div>
          </el-card>
        </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 1000px; height: 450px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'Home',
  data() {
    return {
      userCont:'',
      stuContent:'',
      teaContent:'',
      curContent:''
    };
  },
 created() {
   this.request
           .get('/user/content').then(res =>{
          this.userCont =res.data.user;
          this.stuContent =res.data.student;
          this.teaContent =res.data.teacher;
          this.curContent =res.data.course;

   })
 },
  mounted() {
    // 页面元素渲染之后再触发
    var option = {
      title: {
        text: '入学人数',
        left: 'center',
      },
      tooltip: {
        trigger: 'item',
      },
      legend: {
        orient: 'vertical',
        left: 'left',
      },
      xAxis: {
        type: 'category',
        data: ['第一季度', '第二季度', '第三季度', '第四季度'],
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          name: '学生',
          data: [],
          type: 'bar',
        },
        {
          name: '学生',
          data: [],
          type: 'line',
        },
      ],
    };

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    this.request.get('/echarts/members').then(res => {
      // 填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data;
      option.series[1].data = res.data;

      // 数据准备完毕之后再set
      myChart.setOption(option);
    });
  },
};
</script>

<style scoped></style>

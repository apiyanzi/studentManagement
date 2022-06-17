<template>
  <div>
    <div style="margin: 10px 0">
      <el-input
              style="width: 200px"
              placeholder="请输入名称"
              suffix-icon="el-icon-search"
              v-model="name"
      ></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-popconfirm
              class="ml-5"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="您确定批量删除这些数据吗？"
              @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">
          批量删除
          <i class="el-icon-remove-outline"></i>
        </el-button>
      </el-popconfirm>
    </div>
    <el-table
            :data="tableData"
            border
            stripe
            :header-cell-class-name="'headerBg'"
            @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="score" label="学分"></el-table-column>
      <el-table-column prop="times" label="课时"></el-table-column>
      <el-table-column prop="teacher" label="授课老师"></el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-popconfirm
                  class="ml-5"
                  confirm-button-text="确定"
                  cancel-button-text="我再想想"
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定删除吗？"
                  @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">
              退课
              <i class="el-icon-remove-outline"></i>
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[2, 5, 10, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
      ></el-pagination>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'Course',
    data() {
      return {
        form: {},
        tableData: [],
        name: '',
        multipleSelection: [],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        dialogFormVisible: false,
        teachers: [],
        user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      };
    },
    created() {

      this.load();
    },
    methods: {
      load() {

        this.request
                .get('/course/findStudentPage', {
                  params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    id:this.user.id
                  },
                })
                .then(res => {
                  this.tableData = res.data.records;
                  this.total = res.data.total;
                });

        this.request.get('/user/role/ROLE_TEACHER').then(res => {
          this.teachers = res.data;
        });
      },
      del(id) {
        this.request.delete('/course/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功');
            this.load();
          } else {
            this.$message.error('删除失败');
          }
        });
      },
      handleSelectionChange(val) {
        console.log(val);
        this.multipleSelection = val;
      },
      delBatch() {
        let ids = this.multipleSelection.map(v => v.id); // [{}, {}, {}] => [1,2,3]
        this.request.post('/course/del/batch', ids).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功');
            this.load();
          } else {
            this.$message.error('批量删除失败');
          }
        });
      },
      reset() {
        this.name = '';
        this.load();
      },
      handleSizeChange(pageSize) {
        console.log(pageSize);
        this.pageSize = pageSize;
        this.load();
      },
      handleCurrentChange(pageNum) {
        console.log(pageNum);
        this.pageNum = pageNum;
        this.load();
      },
    },
  };
</script>

<style scoped></style>

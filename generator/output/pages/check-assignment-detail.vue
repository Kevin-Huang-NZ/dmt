<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.roundId" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.projectId" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="ID" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="用户名" align="center" prop="t_user_name" sortable="custom" :class-name="getSortClass('t_user_name')">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.userName }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="roundId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roundId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="projectId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.projectId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="taskId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="userId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.userId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="taskDetailId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskDetailId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="checkStatus" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column label="checkResult" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkResult }}</span>
        </template>
      </el-table-column>
      <el-table-column label="checkMemo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkMemo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="adoptionStatus" align="center">
        <template slot-scope="{row}">
          <span>{{ row.adoptionStatus }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="角色" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roles | roleFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100px">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusClassFilter">
            {{ row.status | statusFilter }}
          </el-tag>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" width="230px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <!-- <el-button v-if="row.status!='1'" size="mini" type="success" @click="handleModifyStatus(row,'1')">
            启用
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" @click="handleModifyStatus(row,'0')">
            停用
          </el-button> -->
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="margin-left:50px;margin-right:50px;">
        <el-form-item label="roundId" prop="roundId">
          <el-input v-model="temp.roundId" />
        </el-form-item>
        <el-form-item label="projectId" prop="projectId">
          <el-input v-model="temp.projectId" />
        </el-form-item>
        <el-form-item label="taskId" prop="taskId">
          <el-input v-model="temp.taskId" />
        </el-form-item>
        <el-form-item label="userId" prop="userId">
          <el-input v-model="temp.userId" />
        </el-form-item>
        <el-form-item label="taskDetailId" prop="taskDetailId">
          <el-input v-model="temp.taskDetailId" />
        </el-form-item>
        <el-form-item label="checkStatus" prop="checkStatus">
          <el-input v-model="temp.checkStatus" />
        </el-form-item>
        <el-form-item label="checkResult" prop="checkResult">
          <el-input v-model="temp.checkResult" />
        </el-form-item>
        <el-form-item label="checkMemo" prop="checkMemo">
          <el-input v-model="temp.checkMemo" />
        </el-form-item>
        <el-form-item label="adoptionStatus" prop="adoptionStatus">
          <el-input v-model="temp.adoptionStatus" />
        </el-form-item>
        <!-- <el-form-item label="生日" prop="birthday">
          <el-date-picker v-model="user.birthday" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in userStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCheckAssignmentDetail, getCheckAssignmentDetail, createCheckAssignmentDetail, updateCheckAssignmentDetail, deleteCheckAssignmentDetail } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
// import { userStatusOptions, userStatusKeyValue } from '@/utils/options'

export default {
  name: 'CheckAssignmentDetail',
  components: { Pagination },
  directives: { waves },
  // filters: {
  //   statusFilter(status) {
  //     return userStatusKeyValue[status]
  //   },
  //   statusClassFilter(status) {
  //     const statusClassMap = {
  //       '0': 'danger',
  //       '1': 'success'
  //     }
  //     return statusClassMap[status]
  //   }
  // },
  data() {
    // const validatePhone = (rule, value, callback) => {
    //   if (!validPhone(value)) {
    //     callback(new Error('请输入正确的手机号码'))
    //   } else {
    //     callback()
    //   }
    // }
    return {
      // initCount: 0,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        roundId: undefined,
        projectId: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_id',
        ascDesc: 'desc'
      },
      // projectOptions: [],
      // taskOptions: [],
      // taskKeyValue: {},
      temp: {
        id: undefined,
        roundId: '',
        projectId: '',
        taskId: '',
        userId: '',
        taskDetailId: '',
        checkStatus: '',
        checkResult: undefined,
        checkMemo: undefined,
        adoptionStatus: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      dialogPvVisible: false,
      rules: {
        roundId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        projectId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        taskId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        taskDetailId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        checkStatus: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        checkResult: [
          { max: 200, message: '长度不能超过200字符。', trigger: 'blur' }
        ],
        checkMemo: [
          { max: 500, message: '长度不能超过500字符。', trigger: 'blur' }
        ],
        adoptionStatus: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false
    }
  },
  // computed: {
  //   getTaskName: function() {
  //     return function(taskId) {
  //       return this.taskKeyValue[taskId]
  //     }
  //   }
  // },
  // watch: {
  //   taskOptions(newVal, oldVal) {
  //     // console.log('taskOptions has been changed')
  //     const { projectId } = this.listQuery
  //     if (typeof projectId !== 'undefined') {
  //       this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === projectId)
  //     }
  //   },
  //   'listQuery.projectId'(newVal, oldVal) {
  //     // console.log('projectId has been changed')
  //     if (this.taskOptions.length > 0) {
  //       this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === newVal)
  //     }
  //   },
  //   initCount() {
  //     if (this.initCount === 1) {
  //       this.getList()
  //     }
  //   }
  // },
  // beforeCreate() {
  //   listProject({ pageNum: -1, orderBy: 't_status', ascDesc: 'asc' }).then(response => {
  //     const { dataList } = response
  //     dataList.forEach((ele, index) => {
  //       const { id, projectName } = ele
  //       this.projectOptions.push({ key: id, display_name: projectName })
  //       if (index === 0) {
  //         this.listQuery.projectId = id
  //       }
  //     })
  //     // if (typeof this.$route.params.projectId !== 'undefined') {
  //     //   this.listQuery.projectId = parseInt(this.$route.params.projectId)
  //     // }
  //     this.initCount++
  //   })
  //   listTask({ pageNum: -1 }).then(response => {
  //     const { dataList } = response
  //     dataList.forEach((ele, index) => {
  //       const { id, taskName, projectId } = ele
  //       this.taskOptions.push({ key: id, display_name: taskName, projectId: projectId })
  //       this.taskKeyValue[id] = taskName
  //     })
  //   })
  // },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listCheckAssignmentDetail(this.listQuery).then(response => {
        const { pagination, dataList } = response
        this.list = dataList
        this.total = pagination.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // handleModifyStatus(row, status) {
    //   const tempData = ((r) => ({ id: r.id, status: r.status }))(row)
    //   tempData.status = status
    //   updateCheckAssignmentDetail(tempData).then(() => {
    //     this.$message({
    //       message: '更新成功',
    //       type: 'success',
    //       duration: 1000
    //     })
    //     row.status = status
    //   })
    // },
    sortChange(data) {
      const { prop, order } = data

      if (order === 'ascending') {
        this.listQuery.ascDesc = 'asc'
      } else {
        this.listQuery.ascDesc = 'desc'
      }
      this.listQuery.orderBy = prop
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        roundId: '',
        projectId: '',
        taskId: '',
        userId: '',
        taskDetailId: '',
        checkStatus: '',
        checkResult: undefined,
        checkMemo: undefined,
        adoptionStatus: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createCheckAssignmentDetail(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '新建成功。',
              type: 'success',
              duration: 1000
            })
            this.getList()
          })
        }
      })
    },
    handleUpdate(row) {
      const { id } = row
      getCheckAssignmentDetail(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          roundId: r.roundId,
          projectId: r.projectId,
          taskId: r.taskId,
          userId: r.userId,
          taskDetailId: r.taskDetailId,
          checkStatus: r.checkStatus,
          checkResult: r.checkResult,
          checkMemo: r.checkMemo,
          adoptionStatus: r.adoptionStatus
        }))(response)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCheckAssignmentDetail(tempData).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '更新成功。',
              type: 'success',
              duration: 1000
            })

            this.getList()
          })
        }
      })
    },
    handleDelete(row, index) {
      const { id } = row
      deleteCheckAssignmentDetail(id).then(() => {
        this.$message({
          message: '删除成功。',
          type: 'success',
          duration: 1000
        })
        this.getList()
      })
    },
    // handleDownload() {
    //   this.downloadLoading = true

    //   const tempData = {
    //     projectId: this.listQuery.projectId,
    //     pageNum: 1,
    //     pageSize: 99999,
    //     orderBy: 't_task_id asc, t_original',
    //     ascDesc: 'asc'
    //   }
    //   listTaskDetail(tempData).then(response => {
    //     const { dataList } = response
    //     this.allList = dataList
    //     import('@/vendor/Export2Excel').then(excel => {
    //       const tHeader = ['外文地名', '国别', '语种', '地理实体类别', '备注',
    //         { v: '备注', c: '外文地名、国别、语种、地理实体类别、备注5列，是导入数据列。其他列在导入时不读取。' },
    //         '罗马转写状态', '罗马字母转写',
    //         '初译状态', '音译', '意译',
    //         '人工审核状态', '终译']
    //       const filterVal = ['original', 'country', 'language', 'gec', 'memo', 'romanStatusName', 'roman', 'transStatusName', 'transliteration', 'freeTranslation', 'checkStatusName', 'transResult']
    //       const data = this.formatJson(filterVal)
    //       excel.export_json_to_excel({
    //         header: tHeader,
    //         data,
    //         filename: 'task-detail'
    //       })
    //       this.allList = null
    //       this.downloadLoading = false
    //     })
    //   })
    // },
    // formatJson(filterVal) {
    //   return this.allList.map(v => filterVal.map(j => {
    //     if (j === 'romanStatusName') {
    //       return romanStatusKeyValue[v['romanStatus']]
    //     } else if (j === 'transStatusName') {
    //       return transStatusKeyValue[v['transStatus']]
    //     } else if (j === 'checkStatusName') {
    //       return checkStatusKeyValue[v['checkStatus']]
    //     } else {
    //       return v[j]
    //     }
    //   }))
    // },
    // beforeImportExcel(file) {
    //   const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    //   const isLt10M = file.size / 1024 / 1024 / 10
    //   if (!isExcel) {
    //     this.$message.error('上传文件格式不正确，请使用 导出 的excel编辑数据并上传!')
    //   }
    //   if (!isLt10M) {
    //     this.$message.error('上传的excel文件大小不能超过 10MB!')
    //   }
    //   return isExcel && isLt10M
    // },
    // importExcel(res) {
    //   const { projectId, taskId } = this.listQuery
    //   // if (!taskId) {
    //   //   this.$alert('导入数据时，请先选择任务。', '提示', {
    //   //     type: 'error'
    //   //   })
    //   // } else {
    //   // Create new formData object
    //   const fd = new FormData()
    //   // append the file you want to upload
    //   fd.append('file', res.file)
    //   // add other data to the form data object if needed
    //   fd.append('projectId', projectId)
    //   fd.append('taskId', taskId)
    //   importTaskDetail(fd).then((response) => {
    //     this.$message({
    //       message: '导入成功。',
    //       type: 'success',
    //       duration: 1000
    //     })
    //     this.getList()
    //   })
    //   // }
    // },
    // handleClear() {
    //   const { projectId, taskId } = this.listQuery
    //   // if (!taskId) {
    //   //   this.$alert('清空数据时，请先选择任务。', '提示', {
    //   //     type: 'error'
    //   //   })
    //   // } else {
    //   this.$confirm('此操作将清空当前任务的所有数据, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     const tempData = {
    //       projectId: projectId,
    //       taskId: taskId
    //     }
    //     clearTaskDetail(tempData).then(() => {
    //       this.$message({
    //         message: '清空成功。',
    //         type: 'success',
    //         duration: 1000
    //       })
    //       this.list = null
    //     })
    //   }).catch(() => {
    //   })
    //   // }
    // },
    getSortClass: function(key) {
      const { orderBy, ascDesc } = this.listQuery
      if (orderBy === key) {
        return ascDesc === 'asc' ? 'ascending' : 'descending'
      }
      return ''
    }
  }
}
</script>

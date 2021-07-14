<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.projectId" class="filter-item" style="width:400px;" filterable placeholder="请选择项目">
        <el-option v-for="item in projectOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.taskId" class="filter-item" style="width:200px;" filterable placeholder="请选择任务">
        <el-option v-for="item in projectTaskOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.roundId" class="filter-item" style="width:200px;" filterable placeholder="请选择终译轮数" @change="handleFilter">
        <el-option v-for="item in projectTaskRoundOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="warning" icon="el-icon-coin" @click="handleAssign">
        实际分配
      </el-button>
    </div>

    <div class="filter-container">
      <el-alert type="info" :closable="false">
        外文地名总数：<el-tag>{{ assignmentSts.total }}</el-tag>；已分配数量：<el-tag type="warning">{{ assignmentSts.assigned }}</el-tag>；待分配数量：<el-tag type="danger">{{ assignmentSts.remained }}</el-tag>
      </el-alert>
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
      <el-table-column label="专家" align="center">
        <template slot-scope="{row}">
          <span>{{ getUserName(row.userId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分配数量" align="center">
        <template slot-scope="{row}">
          <span>{{ row.amount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成数量" align="center">
        <template slot-scope="{row}">
          <span>{{ row.done }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际分配状态" class-name="status-col" width="150px">
        <template slot-scope="{row}">
          <el-tag :type="row.assignStatus | assignStatusClassFilter">
            {{ row.assignStatus | assignStatusFilter }}
          </el-tag>
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
          <el-button v-if="row.assignStatus=='0'" type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.assignStatus=='0'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
          <el-button v-if="row.assignStatus=='1'" type="primary" size="mini" @click="handleAdd(row)">
            追加
          </el-button>
          <el-button v-if="row.assignStatus=='1'" size="mini" type="danger" @click="handleWithdraw(row)">
            撤回
          </el-button>
          <!-- <el-button v-if="row.status!='1'" size="mini" type="success" @click="handleModifyStatus(row,'1')">
            启用
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" @click="handleModifyStatus(row,'0')">
            停用
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="margin-left:50px;margin-right:50px;">

        <el-form-item label="专家" prop="userId">
          <el-select v-model="temp.userId" :disabled="temp.assignStatus=='1'" class="filter-item" placeholder="请选择">
            <el-option v-for="item in userOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配数量" prop="amount">
          <el-input v-model="temp.amount" :disabled="temp.assignStatus=='1'" type="number" />
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
        <el-button v-if="temp.assignStatus=='0'" type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCheckRound, listCheckAssignment, getCheckAssignment, createCheckAssignment, updateCheckAssignment, deleteCheckAssignment, getCheckAssignmentSts, finalTransAssign, finalTransAdd, finalTransWithdraw } from '@/api/final-trans'
import { listProject, listTask } from '@/api/project'
import { listUser } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { validZeroNPositiveInt } from '@/utils/validate'
import { assignStatusOptions, assignStatusKeyValue } from '@/utils/options'

export default {
  name: 'Assignment',
  components: { Pagination },
  directives: { waves },
  filters: {
    assignStatusFilter(status) {
      return assignStatusKeyValue[status]
    },
    assignStatusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success'
      }
      return statusClassMap[status]
    }
  },
  data() {
    const validateAssignAmount = (rule, value, callback) => {
      if (!validZeroNPositiveInt(value)) {
        callback(new Error('请输入正确的分配数量'))
      } else {
        callback()
      }
    }

    return {
      // initCount: 0,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        roundId: undefined,
        projectId: undefined,
        taskId: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_id',
        ascDesc: 'desc'
      },
      assignmentSts: {
        total: 0,
        assigned: 0,
        remained: 0
      },
      assignStatusOptions,
      projectOptions: [],
      taskOptions: [],
      roundOptions: [],
      projectTaskOptions: [],
      projectTaskRoundOptions: [],
      userOptions: [],
      userKeyValue: {},
      temp: {
        id: undefined,
        roundId: '',
        projectId: '',
        taskId: '',
        userId: '',
        amount: '',
        done: '',
        assignStatus: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      dialogPvVisible: false,
      rules: {
        userId: [
          { required: true, message: '请选择专家。', trigger: 'blur' }
        ],
        amount: [
          { required: true, trigger: 'blur', validator: validateAssignAmount }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false
    }
  },
  computed: {
    getUserName: function() {
      return function(userId) {
        return this.userKeyValue[userId]
      }
    }
  },
  watch: {
    taskOptions(newVal, oldVal) {
      // console.log('taskOptions has been changed')
      const { projectId } = this.listQuery
      if (typeof projectId !== 'undefined') {
        this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === projectId)
      }
    },
    'listQuery.projectId'(newVal, oldVal) {
      // console.log('projectId has been changed')
      this.list = null
      this.assignmentSts.total = 0
      this.assignmentSts.assigned = 0
      this.assignmentSts.remained = 0
      if (this.taskOptions.length > 0) {
        this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === newVal)
      } else {
        this.listQuery.taskId = undefined
      }
    },
    projectTaskOptions(newVal, oldVal) {
      if (newVal.length > 0) {
        this.listQuery.taskId = newVal[0].key
      } else {
        this.listQuery.taskId = undefined
      }
    },
    roundOptions(newVal, oldVal) {
      // console.log('taskOptions has been changed')
      const { projectId, taskId } = this.listQuery
      if (typeof taskId !== 'undefined') {
        this.projectTaskRoundOptions = this.roundOptions.filter(t => t.projectId === projectId && t.taskId === taskId)
      }
    },
    'listQuery.taskId'(newVal, oldVal) {
      this.list = null
      this.assignmentSts.total = 0
      this.assignmentSts.assigned = 0
      this.assignmentSts.remained = 0
      if (this.roundOptions.length > 0) {
        const { projectId } = this.listQuery
        this.projectTaskRoundOptions = this.roundOptions.filter(t => t.projectId === projectId && t.taskId === newVal)
      } else {
        this.listQuery.roundId = undefined
      }
    },
    projectTaskRoundOptions(newVal, oldVal) {
      if (newVal.length > 0) {
        this.listQuery.roundId = newVal[0].key
        // this.initCount++
        this.listQuery.pageNum = 1
        this.getList()
      } else {
        this.listQuery.roundId = undefined
      }
    }
    // initCount() {
    //   if (this.initCount === 3) {
    //     this.getList()
    //   }
    // }
  },
  beforeCreate() {
    listProject({ pageNum: -1, orderBy: 't_status', ascDesc: 'asc' }).then(response => {
      const { dataList } = response
      dataList.forEach((ele, index) => {
        const { id, projectName } = ele
        this.projectOptions.push({ key: id, display_name: projectName })
        if (index === 0) {
          this.listQuery.projectId = id
        }
      })
      // if (typeof this.$route.params.projectId !== 'undefined') {
      //   this.listQuery.projectId = parseInt(this.$route.params.projectId)
      // }
      // this.initCount++
    })
    listTask({ pageNum: -1 }).then(response => {
      const { dataList } = response
      dataList.forEach((ele, index) => {
        const { id, taskName, projectId } = ele
        this.taskOptions.push({ key: id, display_name: taskName, projectId: projectId })
      })
      // this.initCount++
    })
    listCheckRound({ pageNum: -1 }).then(response => {
      const { dataList } = response
      dataList.forEach((ele, index) => {
        const { id, roundName, projectId, taskId } = ele
        this.roundOptions.push({ key: id, display_name: roundName, projectId: projectId, taskId: taskId })
      })
      // this.initCount++
    })
    listUser({ pageNum: -1, roles: 'A' }).then(response => {
      const { dataList } = response
      dataList.forEach((ele, index) => {
        const { id, userName } = ele
        this.userOptions.push({ key: id, display_name: userName })
        this.userKeyValue[id] = userName
      })
      // this.initCount++
    })
  },
  created() {
  },
  methods: {
    getList() {
      if (typeof this.listQuery.roundId === 'undefined') {
        this.list = null
        this.assignmentSts.total = 0
        this.assignmentSts.assigned = 0
        this.assignmentSts.remained = 0
      } else {
        this.listLoading = true
        listCheckAssignment(this.listQuery).then(response => {
          const { pagination, dataList } = response
          this.list = dataList
          this.total = pagination.total
          this.listLoading = false
        })
        getCheckAssignmentSts(this.listQuery).then(response => {
          const { total, assigned, remained } = response

          this.assignmentSts.total = total
          this.assignmentSts.assigned = assigned
          this.assignmentSts.remained = remained
        })
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // handleModifyStatus(row, status) {
    //   const tempData = ((r) => ({ id: r.id, status: r.status }))(row)
    //   tempData.status = status
    //   updateCheckAssignment(tempData).then(() => {
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
        roundId: this.listQuery.roundId,
        projectId: this.listQuery.projectId,
        taskId: this.listQuery.taskId,
        userId: '',
        amount: '',
        done: '',
        assignStatus: ''
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
          createCheckAssignment(this.temp).then(() => {
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
      getCheckAssignment(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          roundId: r.roundId,
          projectId: r.projectId,
          taskId: r.taskId,
          userId: r.userId,
          amount: r.amount,
          done: r.done,
          assignStatus: r.assignStatus
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
          updateCheckAssignment(tempData).then(() => {
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
      deleteCheckAssignment(id).then(() => {
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
    handleAssign() {
      const { projectId, taskId, roundId } = this.listQuery
      // if (!taskId) {
      //   this.$alert('清空数据时，请先选择任务。', '提示', {
      //     type: 'error'
      //   })
      // } else {
      this.$confirm('此操作将按照设置的分配数量，把外文地名分配到相关专家名下, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const tempData = {
          projectId: projectId,
          taskId: taskId,
          id: roundId
        }
        finalTransAssign(tempData).then(() => {
          this.$message({
            message: '分配成功。',
            type: 'success',
            duration: 1000
          })
          this.list.forEach((ele, index) => {
            ele.assignStatus = '1'
          })
        })
      }).catch(() => {
      })
      // }
    },
    handleAdd(row) {
      const { projectId, taskId, roundId, userId } = row
      this.$prompt('请输入追加数量', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[1-9][0-9]*$/,
        inputErrorMessage: '追加数量不正确'
      }).then(({ value }) => {
        const tempData = {
          projectId: projectId,
          taskId: taskId,
          roundId: roundId,
          userId: userId,
          amount: value
        }
        finalTransAdd(tempData).then(() => {
          this.$message({
            message: '追加成功。',
            type: 'success',
            duration: 1000
          })
          row.amount = row.amount + parseInt(value)
          this.assignmentSts.assigned = this.assignmentSts.assigned + parseInt(value)
          this.assignmentSts.remained = this.assignmentSts.remained - parseInt(value)
        })
      }).catch(() => {
      })
    },
    handleWithdraw(row) {
      const { projectId, taskId, roundId, userId } = row
      this.$prompt('请输入撤回数量', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[1-9][0-9]*$/,
        inputErrorMessage: '撤回数量不正确'
      }).then(({ value }) => {
        const tempData = {
          projectId: projectId,
          taskId: taskId,
          roundId: roundId,
          userId: userId,
          amount: value
        }
        finalTransWithdraw(tempData).then(() => {
          this.$message({
            message: '撤回成功。',
            type: 'success',
            duration: 1000
          })
          row.amount = row.amount - parseInt(value)

          this.assignmentSts.assigned = this.assignmentSts.assigned - parseInt(value)
          this.assignmentSts.remained = this.assignmentSts.remained + parseInt(value)
        })
      }).catch(() => {
      })
    },
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

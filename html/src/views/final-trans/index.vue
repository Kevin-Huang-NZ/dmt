<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.projectId" class="filter-item" style="width:400px;" filterable placeholder="请选择项目">
        <el-option v-for="item in projectOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.taskId" class="filter-item" style="width:200px;" filterable placeholder="请选择任务" @change="handleFilter">
        <el-option v-for="item in projectTaskOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-input v-model="listQuery.roundName" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

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
      <el-table-column label="终译轮数名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roundName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划结束日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dueDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusClassFilter">
            {{ row.status | checkRoundStatusFilter }}
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
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <!-- <el-button v-if="row.status!='1'" size="mini" type="success" @click="handleModifyStatus(row,'1')">
            启用
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" @click="handleModifyStatus(row,'0')">
            停用
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="margin-left:50px;margin-right:50px;">
        <el-form-item label="终译轮数名称" prop="roundName">
          <el-input v-model="temp.roundName" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="temp.startDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="计划结束日期" prop="dueDate">
          <el-date-picker v-model="temp.dueDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in checkRoundStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input v-model="temp.memo" type="textarea" :rows="4" />
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
import { listCheckRound, getCheckRound, createCheckRound, updateCheckRound } from '@/api/final-trans'
import { listProject, listTask } from '@/api/project'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { checkRoundStatusOptions, checkRoundStatusKeyValue } from '@/utils/options'

export default {
  name: 'FinalTask',
  components: { Pagination },
  directives: { waves },
  filters: {
    checkRoundStatusFilter(status) {
      return checkRoundStatusKeyValue[status]
    },
    statusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success',
        '9': 'warning'
      }
      return statusClassMap[status]
    }
  },
  data() {
    const validateDueDate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请选择计划结束日期'))
      } else if (this.$moment(value, 'yyyy-MM-dd').isBefore(this.$moment(this.temp.startDate, 'yyyy-MM-dd'))) {
        callback(new Error('计划结束日期不能早于开始日期。'))
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
        projectId: undefined,
        taskId: undefined,
        roundName: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_status',
        ascDesc: 'asc'
      },
      checkRoundStatusOptions,
      projectOptions: [],
      taskOptions: [],
      projectTaskOptions: [],
      temp: {
        id: undefined,
        projectId: '',
        taskId: '',
        roundName: '',
        startDate: '',
        dueDate: '',
        status: '',
        memo: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      dialogPvVisible: false,
      rules: {
        roundName: [
          { required: true, message: '请填写终译轮数名称。', trigger: 'blur' },
          { max: 100, message: '终译轮数名称长度不能超过100字符。', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期。', trigger: 'blur' }
        ],
        dueDate: [
          { required: true, trigger: 'blur', validator: validateDueDate }
        ],
        status: [
          { required: true, message: '请选择状态。', trigger: 'blur' }
        ],
        memo: [
          { max: 500, message: '备注长度不能超过500字符。', trigger: 'blur' }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false
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
      this.listQuery.taskId = undefined
      this.list = null
      if (this.taskOptions.length > 0) {
        this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === newVal)
      }
    },
    projectTaskOptions(newVal, oldVal) {
      if (newVal.length > 0) {
        this.listQuery.taskId = newVal[0].key
        // this.initCount++
        this.listQuery.pageNum = 1
        this.getList()
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
  },
  created() {
  },
  methods: {
    getList() {
      if (typeof this.listQuery.taskId === 'undefined') {
        this.list = null
      } else {
        this.listLoading = true
        listCheckRound(this.listQuery).then(response => {
          const { pagination, dataList } = response
          this.list = dataList
          this.total = pagination.total
          this.listLoading = false
        })
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // changeProjectFilter() {
    //   const { projectId } = this.listQuery
    //   this.listQuery.taskId = undefined
    //   this.list = null
    //   this.projectTaskOptions = this.taskOptions.filter(t => t.projectId === projectId)
    //   // this.listQuery.pageNum = 1
    //   // this.getList()
    //   // console.log(this.projectTaskOptions)
    // },
    // handleModifyStatus(row, status) {
    //   const tempData = ((r) => ({ id: r.id, status: r.status }))(row)
    //   tempData.status = status
    //   updateCheckRound(tempData).then(() => {
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
        projectId: this.listQuery.projectId,
        taskId: this.listQuery.taskId,
        roundName: '',
        startDate: '',
        dueDate: '',
        status: '',
        memo: undefined
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
          createCheckRound(this.temp).then(() => {
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
      getCheckRound(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          projectId: r.projectId,
          taskId: r.taskId,
          roundName: r.roundName,
          startDate: r.startDate,
          dueDate: r.dueDate,
          status: r.status,
          memo: r.memo
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
          updateCheckRound(tempData).then(() => {
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
    // handleDelete(row, index) {
    //   const { id } = row
    //   deleteCheckRound(id).then(() => {
    //     this.$message({
    //       message: '删除成功。',
    //       type: 'success',
    //       duration: 1000
    //     })
    //     this.getList()
    //   })
    // },
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

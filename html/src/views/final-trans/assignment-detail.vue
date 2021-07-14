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
      <el-select v-model="listQuery.userId" class="filter-item" style="width:200px;" clearable filterable placeholder="专家" @change="handleFilter">
        <el-option v-for="item in userOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.checkStatus" class="filter-item" style="width:200px;" clearable filterable placeholder="核对状态" @change="handleFilter">
        <el-option v-for="item in checkStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <!-- <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建
      </el-button> -->
      <el-popover
        ref="popoverAdoptAll"
        placement="bottom"
        title="采纳所有核对结果"
        width="300"
        trigger="hover"
        content="选择终译轮数后，可以执行。执行对象为列表中的外语地名。"
      />
      <span v-popover:popoverAdoptAll>
        <el-button :disabled="!listQuery.roundId" class="filter-item" style="margin-left: 10px;" type="warning" icon="el-icon-check" @click="handleAdoptAll">
          采纳所有
        </el-button>
      </span>
      <el-popover
        ref="popoverRejectAll"
        placement="bottom"
        title="拒绝所有核对结果"
        width="300"
        trigger="hover"
        content="选择终译轮数后，可以执行。执行对象为列表中的外语地名。"
      />
      <span v-popover:popoverRejectAll>
        <el-button :disabled="!listQuery.roundId" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-close" @click="handleRejectAll">
          拒绝所有
        </el-button>
      </span>
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
      <!-- <el-table-column label="ID" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column> -->
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
      <el-table-column label="外文地名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.original }}</span>
        </template>
      </el-table-column>
      <el-table-column label="国别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.country }}</span>
        </template>
      </el-table-column>
      <el-table-column label="语种" align="center">
        <template slot-scope="{row}">
          <span>{{ row.language }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地理实体类别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.gec }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template slot-scope="{row}">
          <span>{{ row.memo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="罗马转写状态" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.romanStatus | romanStatusClassFilter">
            {{ row.romanStatus | romanStatusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="罗马字母转写" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roman }}</span>
        </template>
      </el-table-column>
      <el-table-column label="初译状态" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.transStatus | transStatusClassFilter">
            {{ row.transStatus | transStatusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="音译" align="center">
        <template slot-scope="{row}">
          <span>{{ row.transliteration }}</span>
        </template>
      </el-table-column>
      <el-table-column label="意译" align="center">
        <template slot-scope="{row}">
          <span>{{ row.freeTranslation }}</span>
        </template>
      </el-table-column>
      <el-table-column label="核对状态" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.checkStatus | checkStatusClassFilter">
            {{ row.checkStatus | checkStatusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="核对结果" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkResult }}</span>
          <el-popover
            v-if="row.checkMemo"
            placement="top-start"
            title="核对备注"
            width="200"
            trigger="hover"
            :content="row.checkMemo"
          >
            <i slot="reference" class="el-icon-question" />
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="采纳状态" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.adoptionStatus | adoptionStatusClassFilter">
            {{ row.adoptionStatus | adoptionStatusFilter }}
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
          <el-button v-if="row.checkStatus === '1' && row.adoptionStatus == '0'" type="success" size="mini" @click="handleAdopt(row)">
            采纳
          </el-button>
          <el-button v-if="row.checkStatus === '1' && row.adoptionStatus == '0'" size="mini" type="warning" @click="handleReject(row)">
            拒绝
          </el-button>
          <!--
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='1'" size="mini" type="success" @click="handleModifyStatus(row,'1')">
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
    <!--
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
        <el-form-item label="生日" prop="birthday">
          <el-date-picker v-model="user.birthday" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in userStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
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
    -->
  </div>
</template>

<script>
import { listCheckRound, listCheckAssignmentDetail, updateAdoptionStatus, updateAdoptionStatusBatch } from '@/api/final-trans'
import { listProject, listTask } from '@/api/project'
import { listUser } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { romanStatusOptions, romanStatusKeyValue, transStatusOptions, transStatusKeyValue, checkStatusOptions, checkStatusKeyValue, adoptionStatusOptions, adoptionStatusKeyValue } from '@/utils/options'

export default {
  name: 'AssignmentDetail',
  components: { Pagination },
  directives: { waves },
  filters: {
    romanStatusFilter(status) {
      return romanStatusKeyValue[status]
    },
    transStatusFilter(status) {
      return transStatusKeyValue[status]
    },
    checkStatusFilter(status) {
      return checkStatusKeyValue[status]
    },
    adoptionStatusFilter(status) {
      return adoptionStatusKeyValue[status]
    },
    romanStatusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success',
        '9': 'warning'
      }
      return statusClassMap[status]
    },
    transStatusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success'
      }
      return statusClassMap[status]
    },
    checkStatusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success'
      }
      return statusClassMap[status]
    },
    adoptionStatusClassFilter(status) {
      const statusClassMap = {
        '0': 'info',
        '1': 'success',
        '2': 'danger'
      }
      return statusClassMap[status]
    }
  },
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
        projectId: undefined,
        taskId: undefined,
        roundId: undefined,
        userId: undefined,
        checkStatus: undefined,
        adoptionStatus: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_id',
        ascDesc: 'desc'
      },
      romanStatusOptions,
      transStatusOptions,
      checkStatusOptions,
      adoptionStatusOptions,
      projectOptions: [],
      taskOptions: [],
      roundOptions: [],
      projectTaskOptions: [],
      projectTaskRoundOptions: [],
      userOptions: [],
      userKeyValue: {},
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
      } else {
        this.listLoading = true
        listCheckAssignmentDetail(this.listQuery).then(response => {
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
    handleAdopt(row) {
      const tempData = ((r) => ({ id: r.id, adoptionStatus: r.adoptionStatus }))(row)
      tempData.adoptionStatus = '1'
      updateAdoptionStatus(tempData).then(() => {
        this.$message({
          message: '采纳成功',
          type: 'success',
          duration: 1000
        })
        row.adoptionStatus = '1'
      })
    },
    handleReject(row) {
      const tempData = ((r) => ({ id: r.id, adoptionStatus: r.adoptionStatus }))(row)
      tempData.adoptionStatus = '2'
      updateAdoptionStatus(tempData).then(() => {
        this.$message({
          message: '拒绝成功',
          type: 'success',
          duration: 1000
        })
        row.adoptionStatus = '2'
      })
    },
    handleAdoptAll() {
      if (this.listQuery.roundId) {
        const tempData = ((r) => ({ projectId: r.projectId, taskId: r.taskId, roundId: r.roundId, userId: r.userId, adoptionStatus: '1' }))(this.listQuery)
        updateAdoptionStatusBatch(tempData).then(() => {
          this.$message({
            message: '采纳所有，执行成功',
            type: 'success',
            duration: 1000
          })
          this.getList()
        })
      }
    },
    handleRejectAll() {
      if (this.listQuery.roundId) {
        const tempData = ((r) => ({ projectId: r.projectId, taskId: r.taskId, roundId: r.roundId, userId: r.userId, adoptionStatus: '2' }))(this.listQuery)
        updateAdoptionStatusBatch(tempData).then(() => {
          this.$message({
            message: '拒绝所有，执行成功',
            type: 'success',
            duration: 1000
          })
          this.getList()
        })
      }
    },
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
    // resetTemp() {
    //   this.temp = {
    //     id: undefined,
    //     roundId: '',
    //     projectId: '',
    //     taskId: '',
    //     userId: '',
    //     taskDetailId: '',
    //     checkStatus: '',
    //     checkResult: undefined,
    //     checkMemo: undefined,
    //     adoptionStatus: ''
    //   }
    // },
    // handleCreate() {
    //   this.resetTemp()
    //   this.dialogStatus = 'create'
    //   this.dialogFormVisible = true
    //   this.$nextTick(() => {
    //     this.$refs['dataForm'].clearValidate()
    //   })
    // },
    // createData() {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //       createCheckAssignmentDetail(this.temp).then(() => {
    //         this.dialogFormVisible = false
    //         this.$message({
    //           message: '新建成功。',
    //           type: 'success',
    //           duration: 1000
    //         })
    //         this.getList()
    //       })
    //     }
    //   })
    // },
    // handleUpdate(row) {
    //   const { id } = row
    //   getCheckAssignmentDetail(id).then(response => {
    //     // this.temp = Object.assign({}, response) // copy obj
    //     this.temp = ((r) => ({
    //       id: r.id,
    //       roundId: r.roundId,
    //       projectId: r.projectId,
    //       taskId: r.taskId,
    //       userId: r.userId,
    //       taskDetailId: r.taskDetailId,
    //       checkStatus: r.checkStatus,
    //       checkResult: r.checkResult,
    //       checkMemo: r.checkMemo,
    //       adoptionStatus: r.adoptionStatus
    //     }))(response)
    //     this.dialogStatus = 'update'
    //     this.dialogFormVisible = true
    //     this.$nextTick(() => {
    //       this.$refs['dataForm'].clearValidate()
    //     })
    //   })
    // },
    // updateData() {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //       const tempData = Object.assign({}, this.temp)
    //       updateCheckAssignmentDetail(tempData).then(() => {
    //         this.dialogFormVisible = false
    //         this.$message({
    //           message: '更新成功。',
    //           type: 'success',
    //           duration: 1000
    //         })

    //         this.getList()
    //       })
    //     }
    //   })
    // },
    // handleDelete(row, index) {
    //   const { id } = row
    //   deleteCheckAssignmentDetail(id).then(() => {
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

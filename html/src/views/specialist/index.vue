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
      <el-select v-model="listQuery.checkStatus" class="filter-item" style="width:200px;" clearable filterable placeholder="核对状态" @change="handleFilter">
        <el-option v-for="item in checkStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
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
      <el-table-column label="操作" align="center" width="230px" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleCheck(row)">
            核对
          </el-button>
          <!--
          <el-button size="mini" type="warning" @click="handleReject(row)">
            拒绝
          </el-button>
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

    <el-dialog title="填写核对结果" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="margin-left:50px;margin-right:50px;">
        <el-form-item label="音译" prop="transliteration">
          <el-input v-model="temp.transliteration" disabled>
            <el-button slot="append" icon="el-icon-copy-document" @click="handleCopyT" />
          </el-input>
        </el-form-item>
        <el-form-item label="意译" prop="freeTranslation">
          <el-input v-model="temp.freeTranslation" disabled>
            <el-button slot="append" icon="el-icon-copy-document" @click="handleCopyFt" />
          </el-input>
        </el-form-item>
        <el-form-item label="核对结果" prop="checkResult">
          <el-input v-model="temp.checkResult" />
        </el-form-item>
        <el-form-item label="核对备注" prop="checkMemo">
          <el-input v-model="temp.checkMemo" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="原文" prop="original">
          <el-input v-model="temp.original" disabled />
        </el-form-item>
        <el-form-item label="国别" prop="country">
          <el-input v-model="temp.country" disabled />
        </el-form-item>
        <el-form-item label="语种" prop="language">
          <el-input v-model="temp.language" disabled />
        </el-form-item>
        <el-form-item label="地理实体类别" prop="gec">
          <el-input v-model="temp.gec" disabled />
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input v-model="temp.memo" type="textarea" :rows="4" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateCheckResult()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from '@/store'
import { listCheckRound, listCheckAssignmentDetail, getCheckAssignmentDetail, updateCheckResult } from '@/api/final-trans'
import { listProject, listTask } from '@/api/project'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { romanStatusOptions, romanStatusKeyValue, transStatusOptions, transStatusKeyValue, checkStatusOptions, checkStatusKeyValue, adoptionStatusOptions, adoptionStatusKeyValue } from '@/utils/options'

export default {
  name: 'TranslationCheck',
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
        userId: store.getters && store.getters.userId,
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
      temp: {
        id: undefined,
        transliteration: '',
        freeTranslation: '',
        checkStatus: '',
        checkResult: undefined,
        checkMemo: undefined,
        original: '',
        country: undefined,
        language: undefined,
        gec: undefined,
        memo: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        checkResult: [
          { required: true, message: '请填写核对结果。', trigger: 'blur' },
          { max: 200, message: '核对结果长度不能超过200字符。', trigger: 'blur' }
        ],
        checkMemo: [
          { max: 500, message: '核对备注长度不能超过500字符。', trigger: 'blur' }
        ]
      },
      downloadLoading: false
    }
  },
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
  },
  created() {
  },
  methods: {
    getList() {
      if (typeof this.listQuery.roundId === 'undefined') {
        this.list = null
      } else {
        this.listLoading = true
        console.log('user id is:' + this.listQuery.userId)
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
    handleCheck(row) {
      const { id } = row
      getCheckAssignmentDetail(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          transliteration: r.transliteration,
          freeTranslation: r.freeTranslation,
          checkStatus: r.checkStatus,
          checkResult: r.checkResult,
          checkMemo: r.checkMemo,
          original: r.original,
          country: r.country,
          language: r.language,
          gec: r.gec,
          memo: r.memo
        }))(response)
        // this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    updateCheckResult() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCheckResult(tempData).then(() => {
            this.dialogFormVisible = false
            this.$message({
              message: '核对结果已保存。',
              type: 'success',
              duration: 1000
            })

            this.getList()
          })
        }
      })
    },
    handleCopyT() {
      this.temp.checkResult = this.temp.transliteration
    },
    handleCopyFt() {
      this.temp.checkResult = this.temp.freeTranslation
    },
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

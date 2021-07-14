<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.countryCode" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.languageCode" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

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
      <el-table-column label="countryCode" align="center">
        <template slot-scope="{row}">
          <span>{{ row.countryCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="languageCode" align="center">
        <template slot-scope="{row}">
          <span>{{ row.languageCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="original" align="center">
        <template slot-scope="{row}">
          <span>{{ row.original }}</span>
        </template>
      </el-table-column>
      <el-table-column label="roman" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roman }}</span>
        </template>
      </el-table-column>
      <el-table-column label="matchWay" align="center">
        <template slot-scope="{row}">
          <span>{{ row.matchWay }}</span>
        </template>
      </el-table-column>
      <el-table-column label="matchParams" align="center">
        <template slot-scope="{row}">
          <span>{{ row.matchParams }}</span>
        </template>
      </el-table-column>
      <el-table-column label="chinese" align="center">
        <template slot-scope="{row}">
          <span>{{ row.chinese }}</span>
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
        <el-form-item label="countryCode" prop="countryCode">
          <el-input v-model="temp.countryCode" />
        </el-form-item>
        <el-form-item label="languageCode" prop="languageCode">
          <el-input v-model="temp.languageCode" />
        </el-form-item>
        <el-form-item label="original" prop="original">
          <el-input v-model="temp.original" />
        </el-form-item>
        <el-form-item label="roman" prop="roman">
          <el-input v-model="temp.roman" />
        </el-form-item>
        <el-form-item label="matchWay" prop="matchWay">
          <el-input v-model="temp.matchWay" />
        </el-form-item>
        <el-form-item label="matchParams" prop="matchParams">
          <el-input v-model="temp.matchParams" />
        </el-form-item>
        <el-form-item label="chinese" prop="chinese">
          <el-input v-model="temp.chinese" />
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
import { listCtTransliteration, getCtTransliteration, createCtTransliteration, updateCtTransliteration, deleteCtTransliteration } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
// import { userStatusOptions, userStatusKeyValue } from '@/utils/options'

export default {
  name: 'CtTransliteration',
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
        countryCode: undefined,
        languageCode: undefined,
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
        countryCode: '',
        languageCode: '',
        original: '',
        roman: undefined,
        matchWay: '',
        matchParams: undefined,
        chinese: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      dialogPvVisible: false,
      rules: {
        countryCode: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        languageCode: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        original: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        roman: [
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        matchWay: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        matchParams: [
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        chinese: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 10, message: '长度不能超过10字符。', trigger: 'blur' }
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
      listCtTransliteration(this.listQuery).then(response => {
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
    //   updateCtTransliteration(tempData).then(() => {
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
        countryCode: '',
        languageCode: '',
        original: '',
        roman: undefined,
        matchWay: '',
        matchParams: undefined,
        chinese: ''
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
          createCtTransliteration(this.temp).then(() => {
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
      getCtTransliteration(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          countryCode: r.countryCode,
          languageCode: r.languageCode,
          original: r.original,
          roman: r.roman,
          matchWay: r.matchWay,
          matchParams: r.matchParams,
          chinese: r.chinese
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
          updateCtTransliteration(tempData).then(() => {
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
      deleteCtTransliteration(id).then(() => {
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

<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.projectId" class="filter-item" style="width:400px;" filterable placeholder="请选择项目" @change="handleFilter">
        <el-option v-for="item in projectOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <!-- <el-input v-model="listQuery.taskName" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.status" class="filter-item" clearable filterable placeholder="所有状态" @change="handleFilter">
        <el-option v-for="item in taskStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select> -->

      <!-- <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button> -->
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
      <el-table-column label="任务名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.taskName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="语种" align="center" prop="t_language_code" sortable="custom" :class-name="getSortClass('t_language_code')">
        <template slot-scope="{row}">
          <span>{{ getLanguageName(row.languageCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="罗马字母转换" align="center">
        <template slot-scope="{row}">
          <span>{{ row.needRoman | needRomanFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建日期" align="center" prop="t_create_date" sortable="custom" :class-name="getSortClass('t_create_date')">
        <template slot-scope="{row}">
          <span>{{ row.createDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100px" align="center" prop="t_status" sortable="custom" :class-name="getSortClass('t_status')">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusClassFilter">
            {{ row.status | taskStatusFilter }}
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
      <el-table-column label="操作" align="center" width="330px" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.needRoman=='1'" size="mini" type="success" @click="handleRoman(row)">
            罗马转写
          </el-button>
          <el-button size="mini" type="warning" @click="handleTrans(row)">
            初译
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
        <!-- <el-form-item label="projectId" prop="projectId">
          <el-input v-model="temp.projectId" />
        </el-form-item> -->
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="temp.taskName" />
        </el-form-item>
        <el-form-item label="语种" prop="languageCode">
          <el-select v-model="temp.languageCode" class="filter-item" placeholder="请选择">
            <el-option v-for="item in ctLanguageOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="罗马字母转换" prop="needRoman">
          <el-select v-model="temp.needRoman" class="filter-item" placeholder="请选择">
            <el-option v-for="item in needRomanOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in taskStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
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
import { listTask, getTask, createTask, updateTask, listProject, execRoman, execTrans } from '@/api/project'
import { getLanguages } from '@/api/ct'
import { listLanguage } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { taskStatusOptions, taskStatusKeyValue, needRomanOptions, needRomanKeyValue } from '@/utils/options'

export default {
  name: 'Task',
  components: { Pagination },
  directives: { waves },
  filters: {
    taskStatusFilter(status) {
      return taskStatusKeyValue[status]
    },
    needRomanFilter(status) {
      return needRomanKeyValue[status]
    },
    statusClassFilter(status) {
      const statusClassMap = {
        '1': 'success',
        '2': 'warning',
        '9': 'info'
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
      initCount: 0,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        projectId: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_status',
        ascDesc: 'asc'
      },
      // roleOptions,
      taskStatusOptions,
      needRomanOptions,
      // genderOptions,
      languageOptions: [],
      languageKeyValue: {},
      projectOptions: [],
      projectCountryCode: new Map(),
      ctLanguageOptions: [],
      temp: {
        id: undefined,
        projectId: '',
        taskName: '',
        languageCode: '',
        needRoman: '',
        createDate: '',
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
        projectId: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 0, message: '长度不能超过0字符。', trigger: 'blur' }
        ],
        taskName: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 100, message: '长度不能超过100字符。', trigger: 'blur' }
        ],
        languageCode: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 20, message: '长度不能超过20字符。', trigger: 'blur' }
        ],
        needRoman: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        createDate: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 10, message: '长度不能超过10字符。', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        memo: [
          { max: 500, message: '长度不能超过500字符。', trigger: 'blur' }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false
    }
  },
  computed: {
    getLanguageName: function() {
      return function(languageCode) {
        return this.languageKeyValue[languageCode]
      }
    }
  },
  watch: {
    // languageKeyValue() {
    //   // console.log('language has been changed')
    //   this.initCount++
    // },
    'listQuery.projectId'() {
      // console.log('projectId has been changed')
      this.getCtLanguage()
      this.initCount++
    },
    initCount(newVal, oldVal) {
      // console.log('initCount has been changed, new value:' + newVal)
      if (newVal === 2) {
        this.getList()
      }
    }
  },
  beforeCreate() {
    listLanguage({ pageNum: -1 }).then(response => {
      const { dataList } = response
      dataList.forEach(ele => {
        const { languageCode, languageName } = ele
        this.languageOptions.push({ key: languageCode, display_name: languageName })
        this.languageKeyValue[languageCode] = languageName
      })
      this.initCount++
    })
    listProject({ pageNum: -1, orderBy: 't_status', ascDesc: 'asc' }).then(response => {
      const { dataList } = response
      dataList.forEach((ele, index) => {
        const { id, projectName, countryCode } = ele
        this.projectOptions.push({ key: id, display_name: projectName })
        if (index === 0) {
          this.listQuery.projectId = id
        }
        this.projectCountryCode.set(id, countryCode)
      })
      if (typeof this.$route.params.projectId !== 'undefined') {
        this.listQuery.projectId = parseInt(this.$route.params.projectId)
      }
    })
  },
  created() {
    // this.getList()
    // setTimeout(() => {
    //   this.getCtLanguage()
    // }, 0.5 * 1000)
  },
  methods: {
    getCtLanguage() {
      this.ctLanguageOptions = []
      const countryCode = this.projectCountryCode.get(this.listQuery.projectId)
      getLanguages(countryCode, null).then(response => {
        const { dataList } = response
        dataList.forEach(ele => {
          const { languageCode, languageName } = ele
          this.ctLanguageOptions.push({ key: languageCode, display_name: languageName })
        })
      })
    },
    getList() {
      this.listLoading = true
      listTask(this.listQuery).then(response => {
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
    //   updateTask(tempData).then(() => {
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
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        projectId: this.listQuery.projectId,
        taskName: '',
        languageCode: '',
        needRoman: '',
        createDate: '',
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
          createTask(this.temp).then(() => {
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
      getTask(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          projectId: r.projectId,
          taskName: r.taskName,
          languageCode: r.languageCode,
          needRoman: r.needRoman,
          createDate: r.createDate,
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
          updateTask(tempData).then(() => {
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
    handleRoman(row) {
      this.downloadLoading = true
      const { id } = row
      execRoman(id).then(() => {
        this.downloadLoading = false
        this.$message({
          message: '罗马转写已完成。',
          type: 'success',
          duration: 1000
        })
      })
    },
    handleTrans(row) {
      this.downloadLoading = true
      const { id } = row
      execTrans(id).then(() => {
        this.downloadLoading = false
        this.$message({
          message: '初译已完成。',
          type: 'success',
          duration: 1000
        })
      })
    },
    // handleDelete(row, index) {
    //   const { id } = row
    //   deleteTask(id).then(() => {
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
    //   import('@/vendor/Export2Excel').then(excel => {
    //     const tHeader = ['phone', 'userName', 'roles', 'status']
    //     const filterVal = ['phone', 'userName', 'roles', 'status']
    //     const data = this.formatJson(filterVal)
    //     excel.export_json_to_excel({
    //       header: tHeader,
    //       data,
    //       filename: 'table-list'
    //     })
    //     this.downloadLoading = false
    //   })
    // },
    // formatJson(filterVal) {
    //   return this.list.map(v => filterVal.map(j => {
    //     return v[j]
    //     // if (j === 'timestamp') {
    //     //   return parseTime(v[j])
    //     // } else {
    //     //   return v[j]
    //     // }
    //   }))
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

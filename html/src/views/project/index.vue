<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.projectName" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.status" class="filter-item" clearable filterable placeholder="所有状态" @change="handleFilter">
        <el-option v-for="item in projectStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>

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
      <el-table-column label="项目名称" align="center" prop="t_project_name" sortable="custom" :class-name="getSortClass('t_project_name')">
        <template slot-scope="{row}">
          <span>{{ row.projectName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="国家" align="center" prop="t_country_code" sortable="custom" :class-name="getSortClass('t_country_code')">
        <template slot-scope="{row}">
          <span>{{ getCountryName(row.countryCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" align="center" prop="t_start_date" sortable="custom" :class-name="getSortClass('t_start_date')">
        <template slot-scope="{row}">
          <span>{{ row.startDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划结束日期" align="center" prop="t_due_date" sortable="custom" :class-name="getSortClass('t_due_date')">
        <template slot-scope="{row}">
          <span>{{ row.dueDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100px" align="center" prop="t_status" sortable="custom" :class-name="getSortClass('t_status')">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusClassFilter">
            {{ row.status | projectStatusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="任务" align="center" width="160px">
        <template slot-scope="{row}">
          <!-- <router-link :to="{ name: 'Task', params: {projectId: row.id }}" style="margin-left: 10px"> -->
          <router-link :to="'/project/task/'+row.id" style="margin-left: 10px">
            <el-button size="mini" type="info">
              管理
            </el-button>
          </router-link>
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
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="temp.projectName" />
        </el-form-item>
        <el-form-item label="国家" prop="countryCode">
          <el-select v-model="temp.countryCode" class="filter-item" placeholder="请选择">
            <el-option v-for="item in ctCountryOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="temp.startDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="计划结束日期" prop="dueDate">
          <el-date-picker v-model="temp.dueDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in projectStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
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
import { listProject, getProject, createProject, updateProject } from '@/api/project'
// import { listCountry } from '@/api/sys'
import { getCountries } from '@/api/ct'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { projectStatusOptions, projectStatusKeyValue } from '@/utils/options'

export default {
  name: 'Project',
  components: { Pagination },
  directives: { waves },
  filters: {
    projectStatusFilter(status) {
      return projectStatusKeyValue[status]
    },
    statusClassFilter(status) {
      const statusClassMap = {
        '1': 'success',
        '9': 'warning'
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
      initCount: 0,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        projectName: undefined,
        status: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_status',
        ascDesc: 'asc'
      },
      // countryOptions: [],
      countryKeyValue: {},
      ctCountryOptions: [],
      // roleOptions,
      projectStatusOptions,
      // genderOptions,
      temp: {
        id: undefined,
        projectName: '',
        countryCode: '',
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
        projectName: [
          { required: true, message: '请填写项目名称。', trigger: 'blur' },
          { max: 100, message: '项目名称长度不能超过100字符。', trigger: 'blur' }
        ],
        countryCode: [
          { required: true, message: '请填写选择国家。', trigger: 'blur' }
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
  computed: {
    getCountryName: function() {
      return function(countryCode) {
        return this.countryKeyValue[countryCode]
      }
    }
  },
  watch: {
    // countryKeyValue(newVal, oldVal) {
    //   // console.log('country has been changed')

    // },
    initCount() {
      if (this.initCount === 1) {
        this.getList()
      }
    }
  },
  beforeCreate() {
    getCountries(null).then(response => {
      const { dataList } = response
      dataList.forEach(ele => {
        const { countryCode, countryName } = ele
        this.ctCountryOptions.push({ key: countryCode, display_name: countryName })
        this.countryKeyValue[countryCode] = countryName
      })
      this.initCount++
    })
    // listCountry({ pageNum: -1 }).then(response => {
    //   const { dataList } = response
    //   dataList.forEach(ele => {
    //     const { countryCode, countryName } = ele
    //     // this.countryOptions.push({ key: countryCode, display_name: countryName })
    //     this.countryKeyValue[countryCode] = countryName
    //   })
    // })
  },
  created() {
    // this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // setTimeout(() => {
      listProject(this.listQuery).then(response => {
        const { pagination, dataList } = response
        this.list = dataList
        this.total = pagination.total
        this.listLoading = false
      })
      // }, 0.5 * 1000)
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    // handleModifyStatus(row, status) {
    //   const tempData = ((r) => ({ id: r.id, status: r.status }))(row)
    //   tempData.status = status
    //   updateProject(tempData).then(() => {
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
        projectName: '',
        countryCode: '',
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
          createProject(this.temp).then(() => {
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
      getProject(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          projectName: r.projectName,
          countryCode: r.countryCode,
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
          updateProject(tempData).then(() => {
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
    //   deleteProject(id).then(() => {
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

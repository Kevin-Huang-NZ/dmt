<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.countryCode" class="filter-item" clearable filterable placeholder="全部国家" @change="handleFilter">
        <el-option v-for="item in countryOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.languageCode" class="filter-item" clearable filterable placeholder="全部语种" @change="handleFilter">
        <el-option v-for="item in languageOptions" :key="item.key" :label="item.display_name" :value="item.key" />
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
      <el-table-column label="国家" align="center" prop="t_country_code" sortable="custom" :class-name="getSortClass('t_country_code')">
        <template slot-scope="{row}">
          <span>{{ getCountryName(row.countryCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="语种" align="center" prop="t_language_code" sortable="custom" :class-name="getSortClass('t_language_code')">
        <template slot-scope="{row}">
          <span>{{ getLanguageName(row.languageCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="罗马字母转写表" align="center" width="160px">
        <template slot-scope="{row}">
          <router-link :to="{ name: 'CTRoman', params: {countryCode: row.countryCode, languageCode: row.languageCode }}" style="margin-left: 10px">
            <el-button size="mini" type="info">
              管理
            </el-button>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="音译表" align="center" width="160px">
        <template slot-scope="{row}">
          <router-link :to="{ name: 'CTTransliteration', params: {countryCode: row.countryCode, languageCode: row.languageCode }}" style="margin-left: 10px">
            <el-button size="mini" type="info">
              管理
            </el-button>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="常用词" align="center" width="160px">
        <template slot-scope="{row}">
          <router-link :to="{ name: 'CTCommon', params: {countryCode: row.countryCode, languageCode: row.languageCode }}" style="margin-left: 10px">
            <el-button size="mini" type="info">
              管理
            </el-button>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100px">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusClassFilter">
            {{ row.status | statusFilter }}
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
          <el-button v-if="row.status!='1'" size="mini" type="success" @click="handleModifyStatus(row,'1')">
            启用
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" @click="handleModifyStatus(row,'0')">
            停用
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="margin-left:50px;margin-right:50px;">
        <el-form-item label="国家" prop="countryCode">
          <el-select v-model="temp.countryCode" class="filter-item" placeholder="请选择">
            <el-option v-for="item in countryOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="语种" prop="languageCode">
          <el-select v-model="temp.languageCode" class="filter-item" placeholder="请选择">
            <el-option v-for="item in languageOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in ctStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
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
import { listCountryLanguage, createCountryLanguage, updateCountryLanguage, deleteCountryLanguage } from '@/api/ct'
import { listCountry, listLanguage } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { ctStatusOptions, ctStatusKeyValue } from '@/utils/options'

export default {
  name: 'ConversionTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      return ctStatusKeyValue[status]
    },
    statusClassFilter(status) {
      const statusClassMap = {
        '0': 'danger',
        '1': 'success'
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
        countryCode: undefined,
        languageCode: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_country_code',
        ascDesc: 'asc'
      },
      // roleOptions,
      ctStatusOptions,
      // genderOptions,
      countryOptions: [],
      countryKeyValue: {},
      languageOptions: [],
      languageKeyValue: {},
      temp: {
        id: undefined,
        countryCode: '',
        languageCode: '',
        status: ''
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
          { required: true, message: '请选择国家。', trigger: 'blur' }
        ],
        languageCode: [
          { required: true, message: '请选择语种。', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态。', trigger: 'blur' }
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
    },
    getLanguageName: function() {
      return function(languageCode) {
        return this.languageKeyValue[languageCode]
      }
    }
  },
  watch: {
    initCount() {
      if (this.initCount === 2) {
        this.getList()
      }
    }
  },
  beforeCreate() {
    listCountry({ pageNum: -1 }).then(response => {
      const { dataList } = response
      dataList.forEach(ele => {
        const { countryCode, countryName } = ele
        this.countryOptions.push({ key: countryCode, display_name: countryName })
        this.countryKeyValue[countryCode] = countryName
      })
      this.initCount++
    })
    listLanguage({ pageNum: -1 }).then(response => {
      const { dataList } = response
      dataList.forEach(ele => {
        const { languageCode, languageName } = ele
        this.languageOptions.push({ key: languageCode, display_name: languageName })
        this.languageKeyValue[languageCode] = languageName
      })
      this.initCount++
    })
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // setTimeout(() => {
      listCountryLanguage(this.listQuery).then(response => {
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
    handleModifyStatus(row, status) {
      const tempData = Object.assign({}, row)
      tempData.status = status
      updateCountryLanguage(tempData).then(() => {
        this.$message({
          message: '更新成功',
          type: 'success',
          duration: 1000
        })
        row.status = status
      })
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
    resetTemp() {
      this.temp = {
        id: undefined,
        countryCode: '',
        languageCode: '',
        status: ''
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
          createCountryLanguage(this.temp).then(() => {
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
    // handleUpdate(row) {
    //   const { id } = row
    //   getCountryLanguage(id).then(response => {
    //     // this.temp = Object.assign({}, response) // copy obj
    //     this.temp = ((r) => ({
    //       id: r.id,
    //       countryCode: r.countryCode,
    //       languageCode: r.languageCode,
    //       status: r.status
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
    //       updateCountryLanguage(tempData).then(() => {
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
    handleDelete(row, index) {
      const tempData = Object.assign({}, row)
      deleteCountryLanguage(tempData).then(() => {
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

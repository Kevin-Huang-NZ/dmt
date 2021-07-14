<template>
  <div class="app-container">
    <div class="filter-container">
      <el-alert type="info" :closable="false">
        <el-tag>{{ currentCountry.countryName }}</el-tag> - <el-tag>{{ currentLanguage.languageName }}</el-tag> 的音译表
      </el-alert>
    </div>
    <div class="filter-container">
      <el-input v-model="listQuery.original" placeholder="查询关键字" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="info" icon="el-icon-download" @click="handleDownload">
        导出Excel
      </el-button>
      <el-upload
        class="import-excel"
        action=""
        :auto-upload="true"
        :http-request="importExcel"
        :show-file-list="false"
        :before-upload="beforeImportExcel"
      >
        <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-upload2">
          导入Excel
        </el-button>
      </el-upload>
      <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleClear">
        清空
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
      <el-table-column label="原文" align="center">
        <template slot-scope="{row}">
          <span>{{ row.original }}</span>
        </template>
      </el-table-column>
      <el-table-column label="罗马字母转写" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roman }}</span>
        </template>
      </el-table-column>
      <el-table-column label="匹配方式" align="center">
        <template slot-scope="{row}">
          <span>{{ row.matchWay | matchWayFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="匹配参数" align="center">
        <template slot-scope="{row}">
          <span>{{ row.matchParams }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汉字" align="center">
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
        <el-form-item label="原文" prop="original">
          <el-input v-model="temp.original" />
        </el-form-item>
        <el-form-item label="罗马字母转写" prop="roman">
          <el-input v-model="temp.roman" />
        </el-form-item>
        <el-form-item label="匹配方式" prop="matchWay">
          <el-select v-model="temp.matchWay" class="filter-item" placeholder="请选择" clearable>
            <el-option v-for="item in matchWayOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="匹配参数" prop="matchParams">
          <el-input v-model="temp.matchParams" />
        </el-form-item>
        <el-form-item label="汉字" prop="chinese">
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
import { listCtTransliteration, getCtTransliteration, createCtTransliteration, updateCtTransliteration, deleteCtTransliteration, importCtTransliteration, clearCtTransliteration } from '@/api/ct'
import { getCountryByCode, getLanguageByCode } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
import { matchWayOptions, matchWayKeyValue } from '@/utils/options'

export default {
  name: 'CtTransliteration',
  components: { Pagination },
  directives: { waves },
  filters: {
    matchWayFilter(matchWay) {
      return matchWayKeyValue[matchWay]
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
      tableKey: 0,
      list: null,
      allList: null,
      total: 0,
      listLoading: true,
      listQuery: {
        original: undefined,
        countryCode: this.$route.params.countryCode,
        languageCode: this.$route.params.languageCode,
        pageNum: 1,
        pageSize: 10,
        orderBy: 't_original',
        ascDesc: 'asc'
      },
      matchWayOptions,
      matchWayKeyValue,
      temp: {
        id: undefined,
        countryCode: this.$route.params.countryCode,
        languageCode: this.$route.params.languageCode,
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
        original: [
          { required: true, message: '请填写原文。', trigger: 'blur' },
          { max: 20, message: '原文长度不能超过20字符。', trigger: 'blur' }
        ],
        roman: [
          { max: 20, message: '罗马字母转写长度不能超过20字符。', trigger: 'blur' }
        ],
        matchWay: [
          { required: true, message: '请选择匹配方式。', trigger: 'blur' }
        ],
        matchParams: [
          { max: 20, message: '匹配参数长度不能超过20字符。', trigger: 'blur' }
        ],
        chinese: [
          { required: true, message: '请填写汉字。', trigger: 'blur' },
          { max: 10, message: '汉字长度不能超过10字符。', trigger: 'blur' }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false,
      currentCountry: {
        countryCode: '',
        countryName: ''
      },
      currentLanguage: {
        languageCode: '',
        languageName: '',
        isRoman: ''
      }
    }
  },
  beforeCreate() {
    getCountryByCode(this.$route.params.countryCode).then(response => {
      this.currentCountry = ((r) => ({
        countryCode: r.countryCode,
        countryName: r.countryName
      }))(response)
    })
    getLanguageByCode(this.$route.params.languageCode).then(response => {
      this.currentLanguage = ((r) => ({
        languageCode: r.languageCode,
        languageName: r.languageName,
        isRoman: r.isRoman
      }))(response)
    })
  },
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
      Object.assign(this.temp, {
        id: undefined,
        original: '',
        roman: undefined,
        matchWay: '',
        matchParams: undefined,
        chinese: ''
      })
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
    handleDownload() {
      this.downloadLoading = true

      const tempData = {
        countryCode: this.currentCountry.countryCode,
        languageCode: this.currentLanguage.languageCode,
        pageNum: 1,
        pageSize: 99999,
        orderBy: 't_original',
        ascDesc: 'asc'
      }
      listCtTransliteration(tempData).then(response => {
        const { dataList } = response
        this.allList = dataList
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['原文', '罗马字母转写',
            { v: '匹配方式', c: '匹配方式：1-精确；2-前缀（词头）；3-后缀（词尾）；4-前置（在xxx之前）；5-后置（在xxx之后）' },
            { v: '匹配方式-含义', c: '导入数据时，系统读取的是【匹配方式】列，请确保填写了正确的值' },
            '匹配参数', '汉字'
          ]
          const filterVal = ['original', 'roman', 'matchWay', 'matchWayName', 'matchParams', 'chinese']
          const data = this.formatJson(filterVal)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'ct-transliteration'
          })
          this.allList = null
          this.downloadLoading = false
        })
      })
    },
    formatJson(filterVal) {
      return this.allList.map(v => filterVal.map(j => {
        if (j === 'matchWayName') {
          return matchWayKeyValue[v['matchWay']]
        } else {
          return v[j]
        }
      }))
    },
    beforeImportExcel(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      const isLt10M = file.size / 1024 / 1024 / 10
      if (!isExcel) {
        this.$message.error('上传文件格式不正确，请使用 导出 的excel编辑数据并上传!')
      }
      if (!isLt10M) {
        this.$message.error('上传的excel文件大小不能超过 10MB!')
      }
      return isExcel && isLt10M
    },
    importExcel(res) {
      // Create new formData object
      const fd = new FormData()
      // append the file you want to upload
      fd.append('file', res.file)
      // add other data to the form data object if needed
      fd.append('countryCode', this.currentCountry.countryCode)
      fd.append('languageCode', this.currentLanguage.languageCode)
      importCtTransliteration(fd).then((response) => {
        this.$message({
          message: '导入成功。',
          type: 'success',
          duration: 1000
        })
        this.getList()
      })
    },
    handleClear() {
      this.$confirm('此操作将清空所有数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const tempData = {
          countryCode: this.currentCountry.countryCode,
          languageCode: this.currentLanguage.languageCode
        }
        clearCtTransliteration(tempData).then(() => {
          this.$message({
            message: '清空成功。',
            type: 'success',
            duration: 1000
          })
          this.list = null
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
<style lang="scss" scoped>
.import-excel {
  display: inline-block;
}
</style>

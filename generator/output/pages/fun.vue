<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.funNo" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.funNo" placeholder="" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80px" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="用户名" prop="user_name" sortable="custom" align="center" width="160px" :class-name="getSortClass('user_name')">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.userName }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="funNo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funNo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funNo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funName" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="pageName" align="center">
        <template slot-scope="{row}">
          <span>{{ row.pageName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funName" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funType" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="actionType" align="center">
        <template slot-scope="{row}">
          <span>{{ row.actionType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="funMemo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.funMemo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="actionNo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.actionNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="actionName" align="center">
        <template slot-scope="{row}">
          <span>{{ row.actionName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="memo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.memo }}</span>
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
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="funNo" prop="funNo">
          <el-input v-model="temp.funNo" />
        </el-form-item>
        <el-form-item label="funNo" prop="funNo">
          <el-input v-model="temp.funNo" />
        </el-form-item>
        <el-form-item label="funNo" prop="funNo">
          <el-input v-model="temp.funNo" />
        </el-form-item>
        <el-form-item label="funName" prop="funName">
          <el-input v-model="temp.funName" />
        </el-form-item>
        <el-form-item label="pageName" prop="pageName">
          <el-input v-model="temp.pageName" />
        </el-form-item>
        <el-form-item label="funName" prop="funName">
          <el-input v-model="temp.funName" />
        </el-form-item>
        <el-form-item label="funType" prop="funType">
          <el-input v-model="temp.funType" />
        </el-form-item>
        <el-form-item label="actionType" prop="actionType">
          <el-input v-model="temp.actionType" />
        </el-form-item>
        <el-form-item label="funMemo" prop="funMemo">
          <el-input v-model="temp.funMemo" />
        </el-form-item>
        <el-form-item label="actionNo" prop="actionNo">
          <el-input v-model="temp.actionNo" />
        </el-form-item>
        <el-form-item label="actionName" prop="actionName">
          <el-input v-model="temp.actionName" />
        </el-form-item>
        <el-form-item label="memo" prop="memo">
          <el-input v-model="temp.memo" />
        </el-form-item>
        <!-- <el-form-item label="生日" prop="birthday">
          <el-date-picker v-model="user.birthday" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="" />
        </el-form-item>
        <el-form-item label="状态">
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
import { listSysFun, getSysFun, createSysFun, updateSysFun, deleteSysFun } from '@/api/sys'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import { validPhone } from '@/utils/validate'
// import { userStatusOptions, userStatusKeyValue } from '@/utils/options'

export default {
  name: 'SysFun',
  components: { Pagination },
  directives: { waves },
  // filters: {
  //   statusFilter(status) {
  //     return userStatusKeyValue[status]
  //   },
  //   statusClassFilter(status) {
  //     const statusClassMap = {
  //       '0': 'success',
  //       '1': 'danger'
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
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        funNo: undefined,
        funNo: undefined,
        pageNum: 1,
        pageSize: 10,
        orderBy: 'id',
        ascDesc: 'asc'
      },
      // roleOptions,
      // userStatusOptions,
      // genderOptions,
      temp: {
        id: undefined,
        funNo: '',
        funNo: '',
        funNo: '',
        funName: '',
        pageName: '',
        funName: '',
        funType: '',
        actionType: '',
        funMemo: '',
        actionNo: '',
        actionName: '',
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
        funNo: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 80, message: '长度不能超过80字符。', trigger: 'blur' }
        ],
        funNo: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 30, message: '长度不能超过30字符。', trigger: 'blur' }
        ],
        funNo: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 50, message: '长度不能超过50字符。', trigger: 'blur' }
        ],
        funName: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 200, message: '长度不能超过200字符。', trigger: 'blur' }
        ],
        pageName: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 50, message: '长度不能超过50字符。', trigger: 'blur' }
        ],
        funName: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 50, message: '长度不能超过50字符。', trigger: 'blur' }
        ],
        funType: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        actionType: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1, message: '长度不能超过1字符。', trigger: 'blur' }
        ],
        funMemo: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 1,000, message: '长度不能超过1,000字符。', trigger: 'blur' }
        ],
        actionNo: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 30, message: '长度不能超过30字符。', trigger: 'blur' }
        ],
        actionName: [
          { required: true, message: '请填写。', trigger: 'blur' },
          { max: 50, message: '长度不能超过50字符。', trigger: 'blur' }
        ],
        memo: [
          { max: 500, message: '长度不能超过500字符。', trigger: 'blur' }
        ]
        // phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listSysFun(this.listQuery).then(response => {
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
    //   updateSysFun(tempData).then(() => {
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
        funNo: '',
        funNo: '',
        funNo: '',
        funName: '',
        pageName: '',
        funName: '',
        funType: '',
        actionType: '',
        funMemo: '',
        actionNo: '',
        actionName: '',
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
          createSysFun(this.temp).then(() => {
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
      getSysFun(id).then(response => {
        // this.temp = Object.assign({}, response) // copy obj
        this.temp = ((r) => ({
          id: r.id,
          funNo: r.funNo,
          funNo: r.funNo,
          funNo: r.funNo,
          funName: r.funName,
          pageName: r.pageName,
          funName: r.funName,
          funType: r.funType,
          actionType: r.actionType,
          funMemo: r.funMemo,
          actionNo: r.actionNo,
          actionName: r.actionName,
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
          updateSysFun(tempData).then(() => {
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
      deleteSysFun(id).then(() => {
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

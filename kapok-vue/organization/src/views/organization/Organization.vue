<template>
  <v-data-table-server
    v-model="state.selected"
    v-model:page="state.pagination.page"
    v-model:items-per-page="state.pagination.itemsPerPage"
    v-model:sort-by="state.pagination.sortBy"
    :headers="state.headers"
    :items-length="state.total"
    :items="state.data"
    :loading="state.loading"
    :height="mainHeight"
    class="elevation-1"
    item-value="id"
    select-strategy="single"
    fixed-header
    show-select
    return-object
    @update:options="loadData"
  >
    <template #top>
      <v-toolbar>
        <v-toolbar-title>
          <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          icon
          variant="elevated"
          color="primary"
          class="mr-4"
          @click="loadData">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <v-btn
          prepend-icon="mdi-plus"
          variant="flat"
          color="secondary"
          class="mr-4"
          @click="handleAdd">
          新增
        </v-btn>
        <v-btn
          prepend-icon="mdi-pencil"
          variant="flat"
          color="secondary"
          class="mr-4"
          @click="handleEdit">
          修改
        </v-btn>
        <v-btn
          prepend-icon="mdi-delete"
          variant="flat"
          color="secondary"
          class="mr-4"
          :loading="state.loadingDelete"
          @click="handleDelete">
          删除
        </v-btn>
        <v-btn
          icon
          class="mr-4">
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </v-toolbar>
    </template>
    <template #item.status="{ item }">
      <v-chip v-if="item.columns.status == '1'" color="green">启用</v-chip>
      <v-chip v-else color="red">停用</v-chip>
    </template>
    <template #item.type="{ item }">
      {{state.mappingType[item.columns.type]}}
    </template>
  </v-data-table-server>
</template>

<script setup>
import axios from 'axios'
import {computed, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {useAppStore} from '@/store/app'
import {useDisplay, useLayout} from 'vuetify'

const {height} = useDisplay()
const {mainRect} = useLayout()
const mainHeight = computed(() => {
  return height.value - mainRect.value.top - 64 - 48
})

const router = useRouter()
const appStore = useAppStore()
const state = reactive({
  loading: false,
  loadingDelete: false,
  pagination: {
    page: 1,
    itemsPerPage: 20,
    sortBy: []
  },
  headers: [
    {title: '组织架构代码', key: 'code', align: 'start', sortable: true},
    {title: '组织架构名称', key: 'name', align: 'start', sortable: true},
    {title: '组织架构类型', key: 'type', align: 'start', sortable: true},
    {title: '组织架构状态', key: 'status', align: 'start', sortable: false},
    {title: '创建时间', key: 'createdDate', align: 'start', sortable: false},
    {title: '创建用户', key: 'createdBy', align: 'start', sortable: false}
  ],
  selected: [],
  data: [],
  total: 0,
  mappingType: {
    1: '直线制',
    2: '职能制',
    3: '直线－职能制',
    4: '事业部制',
    5: '模拟分权制',
    6: '矩阵制',
    7: '扁平式'
  }
})

function loadData() {
  state.loading = true
  axios.get('/api/organization/v1/organization', {
    params: {
      page: state.pagination.page - 1,
      size: state.pagination.itemsPerPage,
      sort: state.pagination.sortBy.map(s => s.key + ',' + s.order)[0]
    }
  }).then(response => {
    const json = response.data
    state.data = json.data
    state.total = json.total
    state.selected = []
  }).finally(() => {
    state.loading = false
  })
}

function handleAdd() {
  router.push({
    name: 'OrganizationOrganizationEdit',
    params: {id: 0}
  })
}

function handleEdit() {
  const row = state.selected[0]
  if (!row) return

  router.push({
    name: 'OrganizationOrganizationEdit',
    params: {id: row.id}
  })
}

function handleDelete() {
  const row = state.selected[0]
  if (!row) return

  state.loadingDelete = true
  axios.delete(`/api/organization/v1/organization/${row.id}`).then(response => {
    const json = response.data
    appStore.setMessage(json.message)
    loadData()
  }).finally(() => {
    state.loadingDelete = false
  })
}
</script>

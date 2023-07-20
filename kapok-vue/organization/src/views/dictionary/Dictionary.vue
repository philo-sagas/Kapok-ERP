<template>
  <v-layout full-height>
    <v-app-bar flat border>
      <v-app-bar-title>
        <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-menu
        :close-on-content-click="false"
        :offset="[12, -36]"
        location="start"
      >
        <template v-slot:activator="{ props }">
          <v-text-field
            v-model="state.filter.keyword"
            @change="resetFilter"
            variant="underlined"
            placeholder="关键字"
            class="mr-6"
            clearable
          >
            <template #append>
              <v-icon size="large" color="success" v-bind="props">
                {{ state.filter.advanced ? 'mdi-filter-check' : 'mdi-filter-menu' }}
              </v-icon>
            </template>
          </v-text-field>
        </template>
        <v-card title="查询条件" min-width="300">
          <template v-slot:append>
            <v-btn
              icon
              variant="plain"
              color="primary"
              @click="resetFilter">
              <v-icon>mdi-restore</v-icon>
            </v-btn>
          </template>
          <v-divider></v-divider>
          <v-container>
            <v-row dense>
              <v-col cols="12" lg="12">
                <v-text-field
                  v-model.trim="state.filter.code"
                  label="数据字典代码"
                  clearable
                ></v-text-field>
              </v-col>
              <v-col cols="12" lg="12">
                <v-text-field
                  v-model.trim="state.filter.name"
                  label="数据字典名称"
                  clearable
                ></v-text-field>
              </v-col>
              <v-col cols="12" lg="12">
                <v-select
                  v-model="state.filter.enabled"
                  :items="enabledItems"
                  label="数据字典状态"
                  clearable
                ></v-select>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-menu>
      <v-btn
        icon
        variant="elevated"
        color="primary"
        class="mr-4"
        @click="loadData">
        <v-icon>mdi-magnify</v-icon>
      </v-btn>
      <v-btn
        :disabled="!hasAuthority('dictionary:save')"
        prepend-icon="mdi-plus"
        variant="flat"
        color="secondary"
        class="mr-4"
        @click="handleAdd">
        新增
      </v-btn>
      <v-btn
        :disabled="!hasAuthority('dictionary:delete')"
        prepend-icon="mdi-delete"
        variant="flat"
        color="secondary"
        class="mr-4"
        :loading="state.loadingDelete"
        @click="handleDelete()">
        删除
      </v-btn>
      <v-menu open-on-hover>
        <template v-slot:activator="{ props }">
          <v-btn
            icon
            v-bind="props"
            class="mr-4">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        <v-list density="compact" nav>
          <v-list-item
            title="导出"
            value="export"
            prepend-icon="mdi-export"
            variant="tonal"
            base-color="secondary"></v-list-item>
          <v-list-item
            title="导入"
            value="import"
            prepend-icon="mdi-import"
            variant="tonal"
            base-color="secondary"></v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <v-main>
      <v-data-table-server
        v-model="state.selected"
        v-model:page="state.pagination.page"
        v-model:items-per-page="state.pagination.itemsPerPage"
        v-model:sort-by="state.pagination.sortBy"
        :height="mainHeight"
        :headers="state.headers"
        :items-length="state.total"
        :items="state.data"
        :loading="state.loading"
        class="elevation-1"
        item-value="id"
        select-strategy="page"
        fixed-header
        show-select
        return-object
        @update:options="loadData"
      >
        <template #item.enabled="{ item }">
          <v-chip :color="item.columns.enabled == true ? 'success' : 'grey'">
            {{ enabledMapping[item.columns.enabled] }}
          </v-chip>
        </template>
        <template #item.type="{ item }">
          {{ dictionaryTypeMapping[item.columns.type] }}
        </template>
        <template #item.actions="{ item }">
          <v-btn
            icon
            size="small"
            variant="text"
            :disabled="!hasAuthority('dictionary:save')"
            @click="handleEdit(item.raw)">
            <v-icon>mdi-pencil</v-icon>
            <v-tooltip activator="parent" location="top">修改</v-tooltip>
          </v-btn>
          <v-btn
            icon
            size="small"
            variant="text"
            :disabled="!hasAuthority('dictionary:delete')"
            @click="handleDelete(item.raw)">
            <v-icon>mdi-delete</v-icon>
            <v-tooltip activator="parent" location="top">删除</v-tooltip>
          </v-btn>
        </template>
      </v-data-table-server>
    </v-main>
  </v-layout>
</template>

<script setup>
import axios from 'axios'
import {computed, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {useAppStore} from '@/store/app'
import {useDisplay, useLayout} from 'vuetify'
import {useDictionaryStore} from '@/store/dictionary'

const {height} = useDisplay()
const {mainRect} = useLayout()
const mainHeight = computed(() => {
  return height.value - mainRect.value.top - 64 - 48
})

const router = useRouter()
const appStore = useAppStore()
const {hasAuthority} = appStore
const dictionaryStore = useDictionaryStore()
const enabledItems = dictionaryStore.getValue('Enabled')
const enabledMapping = dictionaryStore.getMapping('Enabled')
const dictionaryTypeMapping = dictionaryStore.getMapping('DictionaryType')
const state = reactive({
  loading: false,
  loadingDelete: false,
  pagination: {
    page: 1,
    itemsPerPage: 20,
    sortBy: []
  },
  filter: {
    keyword: null,
    advanced: false
  },
  headers: [
    {title: '数据字典代码', key: 'code', align: 'start', sortable: true},
    {title: '数据字典名称', key: 'name', align: 'start', sortable: true},
    {title: '数据字典值', key: 'value', align: 'start', sortable: false},
    {title: '数据字典类型', key: 'type', align: 'start', sortable: true},
    {title: '数据字典状态', key: 'enabled', align: 'center', sortable: false},
    {title: '创建时间', key: 'createdDate', align: 'start', sortable: false},
    {title: '创建用户', key: 'createdBy', align: 'start', sortable: false},
    {title: '操作', key: 'actions', align: 'center', sortable: false, width: 120}
  ],
  selected: [],
  data: [],
  total: 0
})

function resetFilter() {
  const {keyword} = state.filter
  state.filter = {keyword}
}

function loadData() {
  state.loading = true
  const {code, name, enabled} = state.filter
  if (code || name || enabled != undefined) {
    state.filter.advanced = true
    state.filter.keyword = undefined
  } else {
    resetFilter()
  }
  axios.get('/api/organization/v1/dictionary', {
    params: {
      ...state.filter,
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
    name: 'OrganizationDictionaryEdit',
    params: {id: 0}
  })
}

function handleEdit(r) {
  const row = r || state.selected[0]
  if (!row) return

  router.push({
    name: 'OrganizationDictionaryEdit',
    params: {id: row.id}
  })
}

function handleDelete(row) {
  let url = '/api/organization/v1/dictionary'
  let data
  if (row) {
    url += '/' + row.id
  } else if (state.selected.length) {
    data = state.selected.map(r => r.id)
  } else {
    return
  }
  appStore.confirmMessage({
    message: '请确定是否删除选中记录？',
    ok: () => {
      state.loadingDelete = true
      axios.delete(url, {data}).then(response => {
        const json = response.data
        appStore.alertMessage(json.message)
        loadData()
      }).finally(() => {
        state.loadingDelete = false
      })
    }
  })
}
</script>

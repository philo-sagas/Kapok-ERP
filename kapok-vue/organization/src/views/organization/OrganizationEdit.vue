<template>
  <v-layout full-height>
    <v-app-bar flat border>
      <v-app-bar-title>
        <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn
        :disabled="!hasAuthority('organization:save')"
        prepend-icon="mdi-content-save"
        variant="elevated"
        color="primary"
        class="mr-4"
        @click="handleSubmit">
        保存
      </v-btn>
      <v-btn
        prepend-icon="mdi-subdirectory-arrow-left"
        variant="outlined"
        color="secondary"
        class="mr-4"
        @click="goBack">
        返回
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-card flat :loading="state.loading">
        <v-overlay
          v-model="state.loading"
          contained
          persistent
          class="align-center justify-center"
        >
        </v-overlay>
        <v-form ref="form">
          <v-container>
            <v-row>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model.trim="model.code"
                  :rules="[rules.required, rules.counter50]"
                  :counter="50"
                  :readonly="!isNew"
                  label="组织架构代码"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.name"
                  :rules="[rules.required, rules.counter50]"
                  label="组织架构名称"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="model.enabled"
                  :true-value="true"
                  :false-value="false"
                  label="组织架构状态"
                  color="success"
                  inset
                ></v-switch>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="4">
                <v-select
                  v-model="model.type"
                  :items="organizationTypeItems"
                  :rules="[rules.required]"
                  label="组织架构类型"
                  clearable
                ></v-select>
              </v-col>
              <v-col cols="12" md="4">
                <v-autocomplete
                  v-model="model.pid"
                  :loading="state.loadingSearch"
                  :items="state.orgItems"
                  @update:search="searchOrganization"
                  label="父组织架构"
                  clearable
                ></v-autocomplete>
              </v-col>
              <v-col cols="12" md="4">
                <v-textarea
                  v-model="model.description"
                  :rules="[rules.counter500]"
                  :counter="500"
                  label="详细描述"
                ></v-textarea>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="4">
                <v-textarea
                  v-model="model.functionalAuthority"
                  :rules="[rules.counter1000]"
                  :counter="1000"
                  label="职责权限"
                ></v-textarea>
              </v-col>
              <v-col cols="12" md="4">
                <v-textarea
                  v-model="model.workingProcedure"
                  :rules="[rules.counter1000]"
                  :counter="1000"
                  label="工作程序"
                ></v-textarea>
              </v-col>
              <v-col cols="12" md="4">
                <v-textarea
                  v-model="model.relatedRequirement"
                  :rules="[rules.counter1000]"
                  :counter="1000"
                  label="相关要求"
                ></v-textarea>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card>
    </v-main>
  </v-layout>
</template>

<script setup>
import axios from 'axios'
import {computed, onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {storeToRefs} from 'pinia'
import {useAppStore} from '@/store/app'
import {useRuleStore} from '@/store/rule'
import {useDictionaryStore} from '@/store/dictionary'

const props = defineProps({
  id: String
})
const state = reactive({
  loading: false,
  loadingSearch: false,
  searchInput: null,
  orgItems: [
    {title: '无', value: 0}
  ]
})
const router = useRouter()
const appStore = useAppStore()
const {hasAuthority} = appStore
const ruleStore = useRuleStore()
const {rules} = storeToRefs(ruleStore)
const dictionaryStore = useDictionaryStore()
const organizationTypeItems = dictionaryStore.getValue('OrganizationType')
const model = ref({
  enabled: true,
  rank: 0
})
const form = ref()

const isNew = computed(() => {
  return !props.id || props.id <= 0
})


onMounted(() => {
  loadData()
})

function loadData() {
  if (!props.id || props.id <= 0) {
    initOrgItems()
    return
  }

  state.loading = true
  axios.get(`/api/organization/v1/organization/${props.id}`).then(response => {
    const json = response.data
    model.value = json.data
    if (json.data && json.data.pid) {
      initOrgItems(json.data.pid)
    }
  }).finally(() => {
    state.loading = false
  })
}

let orgController = null

function searchOrganization(value) {
  if (!value || value === state.searchInput) return

  if (orgController && !orgController.signal.aborted) {
    orgController.abort()
  }
  if (!orgController || orgController.signal.aborted) {
    orgController = new AbortController()
  }
  state.loadingSearch = true
  axios.get('/api/organization/v1/organization/search', {
    params: {
      keyword: value
    },
    signal: orgController.signal
  }).then(response => {
    const json = response.data
    if (json.data && json.data.length) {
      state.orgItems = json.data.map(item => {
        return {
          title: item.name,
          value: item.id
        }
      })
      state.searchInput = value
    }
  }).finally(() => {
    state.loadingSearch = false
  })
}

function initOrgItems(orgId) {
  let url = '/api/organization/v1/organization'
  let params
  if (orgId > 0) {
    url += '/' + orgId
  } else {
    url += '/search'
    params = {keyword: ''}
  }
  state.loadingSearch = true
  axios.get(url, {params}).then(response => {
    const json = response.data
    if (!json.data && json.data.length <= 0) return

    if (!(json.data instanceof Array)) {
      state.searchInput = json.data.name
      json.data = [json.data]
    }
    state.orgItems = json.data.map(item => {
      return {
        title: item.name,
        value: item.id
      }
    })
  }).finally(() => {
    state.loadingSearch = false
  })
}

async function handleSubmit() {
  const {valid} = await form.value.validate()
  if (!valid) return

  state.loading = true
  axios.post('/api/organization/v1/organization', model.value).then(response => {
    const json = response.data
    appStore.alertMessage(json.message)
    goBack()
  }).finally(() => {
    state.loading = false
  })
}

function goBack() {
  router.push({
    name: 'OrganizationOrganization'
  })
}
</script>

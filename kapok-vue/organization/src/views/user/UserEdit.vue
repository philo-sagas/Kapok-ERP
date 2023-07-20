<template>
  <v-layout full-height>
    <v-app-bar flat border>
      <v-app-bar-title>
        <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn
        :disabled="!hasAuthority('user:save')"
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
                  v-model.trim="model.subject"
                  :rules="[rules.required, rules.counter50]"
                  :counter="20"
                  :readonly="!isNew"
                  label="用户账号"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.password"
                  :rules="isNew ? [rules.required] : []"
                  label="用户密码"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="model.enabled"
                  :true-value="true"
                  :false-value="false"
                  label="用户状态"
                  color="success"
                  inset
                ></v-switch>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.username"
                  :rules="[rules.counter20]"
                  :counter="20"
                  label="用户名称"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.phoneNumber"
                  :rules="[rules.mobile]"
                  :counter="20"
                  label="手机号码"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.email"
                  :rules="[rules.email, rules.counter50]"
                  :counter="50"
                  label="电子邮件"
                ></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="4">
                <v-select
                  v-model="model.gender"
                  :items="userGenderItems"
                  label="性别"
                  clearable
                ></v-select>
              </v-col>
              <v-col cols="12" md="4">
                <c-date-picker-field
                  v-model="model.birthdate"
                  label="出生日期"
                ></c-date-picker-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-textarea
                  v-model="model.address"
                  :rules="[rules.counter100]"
                  :counter="100"
                  label="居住地址"
                ></v-textarea>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="4">
                <v-autocomplete
                  v-model="model.roleId"
                  :loading="state.loadingSearchRole"
                  :items="state.roleItems"
                  @update:search="searchRole"
                  label="角色"
                  clearable
                ></v-autocomplete>
              </v-col>
              <v-col cols="12" md="4">
                <v-autocomplete
                  v-model="model.orgId"
                  :loading="state.loadingSearchOrg"
                  :items="state.orgItems"
                  @update:search="searchOrganization"
                  label="组织架构"
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
  loadingSearchRole: false,
  loadingSearchOrg: false,
  searchInputRole: null,
  searchInputOrg: null,
  roleItems: [
    {title: '无', value: 0}
  ],
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
const userGenderItems = dictionaryStore.getValue('UserGender')
const model = ref({
  enabled: true
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
    initRoleItems()
    initOrgItems()
    return
  }

  state.loading = true
  axios.get(`/api/organization/v1/user/${props.id}`).then(response => {
    const json = response.data
    model.value = json.data
    if (json.data && json.data.roleId) {
      initRoleItems(json.data.roleId)
    }
    if (json.data && json.data.orgId) {
      initOrgItems(json.data.orgId)
    }
  }).finally(() => {
    state.loading = false
  })
}

let roleController = null

function searchRole(value) {
  if (!value || value === state.searchInputRole) return

  if (roleController && !roleController.signal.aborted) {
    roleController.abort()
  }
  if (!roleController || roleController.signal.aborted) {
    roleController = new AbortController()
  }
  state.loadingSearchRole = true
  axios.get('/api/organization/v1/role/search', {
    params: {
      keyword: value
    },
    signal: roleController.signal
  }).then(response => {
    const json = response.data
    if (json.data && json.data.length) {
      state.roleItems = json.data.map(item => {
        return {
          title: item.name,
          value: item.id
        }
      })
      state.searchInputRole = value
    }
  }).finally(() => {
    state.loadingSearchRole = false
  })
}

let orgController = null

function searchOrganization(value) {
  if (!value || value === state.searchInputOrg) return

  if (orgController && !orgController.signal.aborted) {
    orgController.abort()
  }
  if (!orgController || orgController.signal.aborted) {
    orgController = new AbortController()
  }
  state.loadingSearchOrg = true
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
      state.searchInputOrg = value
    }
  }).finally(() => {
    state.loadingSearchOrg = false
  })
}

function initRoleItems(roleId) {
  let url = '/api/organization/v1/role'
  let params
  if (roleId > 0) {
    url += '/' + roleId
  } else {
    url += '/search'
    params = {keyword: ''}
  }
  state.loadingSearchRole = true
  axios.get(url, {params}).then(response => {
    const json = response.data
    if (!json.data && json.data.length <= 0) return

    if (!(json.data instanceof Array)) {
      state.searchInputRole = json.data.name
      json.data = [json.data]
    }
    state.roleItems = json.data.map(item => {
      return {
        title: item.name,
        value: item.id
      }
    })
  }).finally(() => {
    state.loadingSearchRole = false
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
  state.loadingSearchOrg = true
  axios.get(url, {params}).then(response => {
    const json = response.data
    if (!json.data && json.data.length <= 0) return

    if (!(json.data instanceof Array)) {
      state.searchInputOrg = json.data.name
      json.data = [json.data]
    }
    state.orgItems = json.data.map(item => {
      return {
        title: item.name,
        value: item.id
      }
    })
  }).finally(() => {
    state.loadingSearchOrg = false
  })
}

async function handleSubmit() {
  const {valid} = await form.value.validate()
  if (!valid) return

  state.loading = true
  axios.post('/api/organization/v1/user', model.value).then(response => {
    const json = response.data
    appStore.alertMessage(json.message)
    goBack()
  }).finally(() => {
    state.loading = false
  })
}

function goBack() {
  router.push({
    name: 'OrganizationUser'
  })
}
</script>

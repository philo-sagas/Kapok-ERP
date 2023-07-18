<template>
  <v-toolbar>
    <v-toolbar-title>
      <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn
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
  </v-toolbar>
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
              v-model="model.code"
              :rules="[rules.required, rules.counter50]"
              :counter="50"
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
            <v-combobox
              v-model="model.type"
              :items="state.typeItems"
              :rules="[rules.required]"
              label="组织架构类型"
              :return-object="false"
              clearable
            ></v-combobox>
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
        <v-row>
          <v-col cols="12" md="8">
            <v-textarea
              v-model="model.description"
              :rules="[rules.counter500]"
              :counter="500"
              label="详细描述"
            ></v-textarea>
          </v-col>
          <v-col cols="12" md="4">
            <v-switch
              v-model="model.status"
              :true-value="1"
              :false-value="0"
              label="组织架构状态"
            ></v-switch>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-card>
</template>

<script setup>
import axios from 'axios'
import {reactive, ref, watchEffect} from 'vue'
import {useRouter} from 'vue-router'
import {storeToRefs} from 'pinia'
import {useAppStore} from '@/store/app'
import {useRuleStore} from '@/store/rule'

const props = defineProps({
  id: String
})
const state = reactive({
  loading: false,
  typeItems: [
    {title: '直线制', value: 1},
    {title: '职能制', value: 2},
    {title: '直线－职能制', value: 3},
    {title: '事业部制', value: 4},
    {title: '模拟分权制', value: 5},
    {title: '矩阵制', value: 6},
    {title: '扁平式', value: 7}
  ]
})
const router = useRouter()
const appStore = useAppStore()
const ruleStore = useRuleStore()
const {rules} = storeToRefs(ruleStore)
const model = ref({
  status: 1
})
const form = ref()


watchEffect(loadData)

function loadData() {
  if (!props.id || props.id <= 0) return

  state.loading = true
  axios.get(`/api/organization/v1/organization/${props.id}`).then(response => {
    const json = response.data
    model.value = json.data
  }).finally(() => {
    state.loading = false
  })
}

async function handleSubmit() {
  const {valid} = await form.value.validate()
  if (!valid) return

  state.loading = true
  axios.post('/api/organization/v1/organization', model.value).then(response => {
    const json = response.data
    appStore.setMessage(json.message)
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

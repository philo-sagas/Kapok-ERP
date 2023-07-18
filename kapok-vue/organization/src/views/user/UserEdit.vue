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
              v-model="model.username"
              :rules="[rules.required, rules.counter50]"
              :counter="50"
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
              v-model="model.status"
              :true-value="1"
              :false-value="0"
              label="用户状态"
            ></v-switch>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="model.nickname"
              :rules="[rules.counter20]"
              :counter="20"
              label="用户名称"
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="model.mobile"
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
            <c-date-picker-field
              v-model="model.birthdate"
              label="出生日期"
            ></c-date-picker-field>
          </v-col>
          <v-col cols="12" md="8">
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
</template>

<script setup>
import axios from 'axios'
import {computed, reactive, ref, watchEffect} from 'vue'
import {useRouter} from 'vue-router'
import {storeToRefs} from 'pinia'
import {useAppStore} from '@/store/app'
import {useRuleStore} from '@/store/rule'

const props = defineProps({
  id: String
})
const state = reactive({
  loading: false
})
const router = useRouter()
const appStore = useAppStore()
const ruleStore = useRuleStore()
const {rules} = storeToRefs(ruleStore)
const model = ref({
  status: 1
})
const form = ref()

const isNew = computed(() => {
  return !props.id || props.id <= 0
})

watchEffect(loadData)

function loadData() {
  if (!props.id || props.id <= 0) return

  state.loading = true
  axios.get(`/api/organization/v1/user/${props.id}`).then(response => {
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
  axios.post('/api/organization/v1/user', model.value).then(response => {
    const json = response.data
    appStore.setMessage(json.message)
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

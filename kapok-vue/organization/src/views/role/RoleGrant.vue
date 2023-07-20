<template>
  <v-layout full-height>
    <v-app-bar flat border>
      <v-app-bar-title>
        <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn
        :disabled="!hasAuthority('role:grant')"
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
                  v-model="model.code"
                  :counter="50"
                  label="角色代码"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.name"
                  label="角色名称"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="model.enabled"
                  :true-value="true"
                  :false-value="false"
                  label="角色状态"
                  color="success"
                  inset
                  readonly
                ></v-switch>
              </v-col>
            </v-row>
            <c-checkbox-group-hierarchy v-model="model.permIds" :items="state.permissions">
              <v-progress-linear
                :active="state.loadingTree"
                indeterminate
                height="2">
              </v-progress-linear>
            </c-checkbox-group-hierarchy>
          </v-container>
        </v-form>
      </v-card>
    </v-main>
  </v-layout>
</template>

<script setup>
import axios from 'axios'
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {useAppStore} from '@/store/app'

const props = defineProps({
  id: String
})
const state = reactive({
  loading: false,
  loadingTree: false,
  permissions: []
})
const router = useRouter()
const appStore = useAppStore()
const {hasAuthority} = appStore
const model = ref({})
const form = ref()


onMounted(() => {
  loadData()
  loadTree()
})

function loadData() {
  if (!props.id || props.id <= 0) return

  state.loading = true
  axios.get(`/api/organization/v1/role/${props.id}/granted`).then(response => {
    const json = response.data
    model.value = json.data
  }).finally(() => {
    state.loading = false
  })
}

function loadTree() {
  state.loadingTree = true
  axios.get('/api/organization/v1/permission/tree').then(response => {
    const json = response.data
    const root = {children: json.data}
    const queue = [root]
    while (queue.length) {
      const node = queue.pop()
      if (node.children && node.children.length) {
        const children = node.children.map(n => {
          return {
            title: n.name,
            value: n.id,
            children: n.leaf ? undefined : n.children
          }
        })
        node.children = children
        queue.push(...node.children)
      }
    }
    state.permissions = root.children
  }).finally(() => {
    state.loadingTree = false
  })
}

async function handleSubmit() {
  const {valid} = await form.value.validate()
  if (!valid) return

  state.loading = true
  axios.post('/api/organization/v1/role/grant', model.value).then(response => {
    const json = response.data
    appStore.alertMessage(json.message)
    goBack()
  }).finally(() => {
    state.loading = false
  })
}

function goBack() {
  router.push({
    name: 'OrganizationRole'
  })
}
</script>

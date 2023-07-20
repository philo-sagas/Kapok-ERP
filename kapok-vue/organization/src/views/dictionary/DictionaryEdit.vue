<template>
  <v-layout full-height>
    <v-app-bar flat border>
      <v-app-bar-title>
        <v-breadcrumbs :items="$route.meta.breadcrumbs"></v-breadcrumbs>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn
        :disabled="!hasAuthority('dictionary:save')"
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
                  label="数据字典代码"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-text-field
                  v-model="model.name"
                  :rules="[rules.required, rules.counter50]"
                  label="数据字典名称"
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="model.enabled"
                  :true-value="true"
                  :false-value="false"
                  label="数据字典状态"
                  color="success"
                  inset
                ></v-switch>
              </v-col>
            </v-row>
            <v-row>
              <v-col v-if="model.type == 1" cols="12" md="4">
                <v-text-field
                  v-model="model.value"
                  :rules="[rules.counter50]"
                  :counter="50"
                  label="数据字典值"
                ></v-text-field>
              </v-col>
              <v-col v-else cols="12" md="4">
                <v-table density="compact">
                  <thead>
                  <tr>
                    <th class="text-left">
                      <v-text-field
                        v-model="state.item.code"
                        :error-messages="state.item.codeErrorMessage"
                        density="compact"
                        label="代码"
                      ></v-text-field>
                    </th>
                    <th class="text-left">
                      <v-text-field
                        v-model="state.item.value"
                        :error-messages="state.item.valueErrorMessage"
                        density="compact"
                        label="值"
                      ></v-text-field>
                    </th>
                    <th class="text-center">
                      <v-btn
                        icon
                        size="small"
                        variant="flat"
                        color="secondary"
                        @click="addItem">
                        <v-icon>mdi-plus</v-icon>
                        <v-tooltip activator="parent" location="right">新增</v-tooltip>
                      </v-btn>
                    </th>
                  </tr>
                  <tr>
                    <th class="text-left">代码</th>
                    <th class="text-left">值</th>
                    <th class="text-center">操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="item in model.items" :key="item.code">
                    <td class="text-left">{{ item.code }}</td>
                    <td class="text-left">{{ item.value }}</td>
                    <td class="text-center">
                      <v-tooltip location="right" text="删除">
                        <template v-slot:activator="{ props }">
                          <v-icon size="small" v-bind="props" @click="deleteItem(item)">
                            mdi-delete
                          </v-icon>
                        </template>
                      </v-tooltip>
                    </td>
                  </tr>
                  </tbody>
                </v-table>
              </v-col>
              <v-col cols="12" md="4">
                <v-select
                  v-model="model.type"
                  :items="dictionaryTypeItems"
                  :rules="[rules.required]"
                  label="数据字典类型"
                  clearable
                ></v-select>
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
  item: {
    code: null,
    value: null,
    sort: 0
  }
})
const router = useRouter()
const appStore = useAppStore()
const {hasAuthority} = appStore
const ruleStore = useRuleStore()
const {rules} = storeToRefs(ruleStore)
const dictionaryStore = useDictionaryStore()
const dictionaryTypeItems = dictionaryStore.getValue('DictionaryType')
const model = ref({
  enabled: true,
  type: 1,
  items: []
})
const form = ref()

const isNew = computed(() => {
  return !props.id || props.id <= 0
})


onMounted(() => {
  loadData()
})

function loadData() {
  if (!props.id || props.id <= 0) return

  state.loading = true
  axios.get(`/api/organization/v1/dictionary/${props.id}`).then(response => {
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
  axios.post('/api/organization/v1/dictionary', model.value).then(response => {
    const json = response.data
    appStore.alertMessage(json.message)
    goBack()
  }).finally(() => {
    state.loading = false
  })
}

function addItem() {
  const {code, value, sort = 0} = state.item
  if (code && value) {
    model.value.items.push({code, value, sort})
    state.item = {}
  } else {
    state.item.codeErrorMessage = code ? undefined : '必填！'
    state.item.valueErrorMessage = value ? undefined : '必填！'
  }
}

function deleteItem(item) {
  const index = model.value.items.indexOf(item)
  if (index >= 0) {
    model.value.items.splice(index, 1)
  }
}

function goBack() {
  router.push({
    name: 'OrganizationDictionary'
  })
}
</script>

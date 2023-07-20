<template>
  <router-view/>
  <v-snackbar
    v-model="hasMessage"
    :timeout="2000">
    {{ message }}
    <template v-slot:actions>
      <v-btn
        icon
        color="blue"
        @click="hasMessage = false">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
  <v-dialog
    v-model="showDialog"
    width="auto">
    <v-card>
      <v-card-title class="bg-indigo">
        <div class="text-h6">{{ dialogConfig && dialogConfig.title }}</div>
      </v-card-title>
      <v-card-text>
        <div class="text-body-1 px-6 py-3">{{ dialogConfig && dialogConfig.message }}</div>
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn
          prepend-icon="mdi-hand-okay"
          variant="elevated"
          color="primary"
          @click="confirmDialog()">
          确定
        </v-btn>
        <v-btn
          prepend-icon="mdi-cancel"
          variant="outlined"
          color="secondary"
          @click="cancelDialog()">
          取消
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import {computed} from 'vue'
import {storeToRefs} from 'pinia'
import {useAppStore} from '@/store/app'

const appStore = useAppStore()
const {message, dialogConfig} = storeToRefs(appStore)

const hasMessage = computed({
  get() {
    return message.value != null
  },
  set() {
    message.value = null
  }
})

const showDialog = computed({
  get() {
    return dialogConfig.value != null
  },
  set() {
    dialogConfig.value = null
  }
})

function confirmDialog() {
  const {ok} = dialogConfig.value
  dialogConfig.value = null
  if (ok instanceof Function) {
    ok()
  }
}

function cancelDialog() {
  const {cancel} = dialogConfig.value
  dialogConfig.value = null
  if (cancel instanceof Function) {
    cancel()
  }
}
</script>

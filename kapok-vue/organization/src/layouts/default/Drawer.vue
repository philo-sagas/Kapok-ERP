<template>
  <v-navigation-drawer
    :model-value="props.drawer"
    @update:modelValue="$emit('update:drawer', $event)"
    :expand-on-hover="!mobile"
    :rail="!mobile"
    color="indigo-lighten-5"
    elevation="2">
    <v-list>
      <v-list-item
        prepend-avatar="@/assets/avatar.jpg"
        :title="user.sub"
        :subtitle="user.name">
        <template #append>
          <v-btn
            @click="refreshAuth"
            variant="plain"
            icon="mdi-refresh"
            density="compact"></v-btn>
        </template>
      </v-list-item>
    </v-list>
    <v-divider></v-divider>
    <v-list
      :items="authMenus"
      density="compact"
      nav>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup>
import {onMounted} from 'vue'
import {storeToRefs} from 'pinia'
import {useDisplay} from 'vuetify'
import {useAppStore} from '@/store/app'

const props = defineProps({
  drawer: Boolean
})
defineEmits(['update:drawer'])
const {mobile} = useDisplay()

const appStore = useAppStore()
const {user, authMenus} = storeToRefs(appStore)
const {refreshAuth} = appStore

onMounted(() => {
  refreshAuth()
})
</script>

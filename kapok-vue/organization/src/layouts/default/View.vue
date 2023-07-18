<template>
  <v-main>
    <router-view />
  </v-main>
  <v-snackbar
    v-model="appStore.hasMessage"
    :timeout="2000"
  >
    {{ appStore.message }}
    <template v-slot:actions>
      <v-btn
        icon
        color="blue"
        @click="appStore.hasMessage = false"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script setup>
import {useAppStore} from '@/store/app'

const appStore = useAppStore()

</script>

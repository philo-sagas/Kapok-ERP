<template>
  <v-app-bar
    :elevation="2"
    color="indigo"
  >
    <v-app-bar-nav-icon
      @click.stop="$emit('update:drawer', !props.drawer)"
    ></v-app-bar-nav-icon>
    <v-app-bar-title>
      <v-icon
        color="blue-darken-2"
        icon="mdi-store"/>
      Kapok-ERP
      <v-btn
        class="ml-3"
        variant="tonal"
        density="comfortable"
        prepend-icon="mdi-magnify">
        搜索
      </v-btn>
    </v-app-bar-title>

    <v-spacer></v-spacer>

    <v-btn icon>
      <v-badge
        :content="0"
        color="error">
        <v-icon>mdi-bell-badge-outline</v-icon>
      </v-badge>
    </v-btn>

    <v-btn icon>
      <v-icon>mdi-cog-outline</v-icon>
    </v-btn>

    <v-divider
      class="mx-2"
      inset
      vertical
    ></v-divider>

    <v-btn icon @click="logout">
      <v-icon>mdi-export</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script setup>
import {useRouter} from 'vue-router'

const props = defineProps({
  drawer: Boolean
})
defineEmits(['update:drawer'])

const router = useRouter()

function logout() {
  router.push({
    name: 'Login'
  })
}
</script>

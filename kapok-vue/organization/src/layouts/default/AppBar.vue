<template>
  <v-app-bar
    :elevation="2"
    color="indigo">
    <v-app-bar-nav-icon
      v-if="props.showTools"
      @click.stop="$emit('update:drawer', !props.drawer)">
    </v-app-bar-nav-icon>
    <v-app-bar-nav-icon v-else icon="mdi-home" :to="{name: 'Home'}">
    </v-app-bar-nav-icon>
    <v-app-bar-title>
      <v-icon
        color="blue-darken-2"
        icon="mdi-store"></v-icon>
      Kapok-ERP
      <v-btn
        v-if="props.showTools"
        class="ml-3"
        variant="tonal"
        density="comfortable"
        prepend-icon="mdi-magnify">
        搜索
      </v-btn>
    </v-app-bar-title>

    <v-spacer></v-spacer>

    <v-btn
      v-if="props.showTools"
      icon>
      <v-badge
        :content="0"
        color="error">
        <v-icon>mdi-bell-badge-outline</v-icon>
      </v-badge>
    </v-btn>
    <v-btn
      v-if="props.showTools"
      icon>
      <v-icon>mdi-cog-outline</v-icon>
    </v-btn>
    <v-divider
      v-if="props.showTools"
      inset
      vertical
      class="mx-2">
    </v-divider>
    <v-btn
      v-if="props.showTools"
      @click="logout"
      icon>
      <v-icon>mdi-export</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script setup>
import {logout} from '@/utils/authorization'

const props = defineProps({
  drawer: Boolean,
  showTools: {
    type: Boolean,
    default: true
  }
})
defineEmits(['update:drawer'])

</script>

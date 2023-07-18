<template>
  <v-app>
    <default-bar v-model:drawer="drawer" />
    <default-drawer v-model:drawer="drawer" />
    <default-view />
  </v-app>
</template>

<script setup>
import {ref} from 'vue'
import DefaultBar from './default/AppBar.vue'
import DefaultDrawer from './default/Drawer.vue'
import DefaultView from './default/View.vue'

const drawer = ref(null)
</script>

<template>
  <v-app>
    <default-bar :show-tools="false"/>
    <default-view/>
  </v-app>
</template>

<script setup>
import DefaultBar from './default/AppBar.vue'
import DefaultView from './default/View.vue'
</script>

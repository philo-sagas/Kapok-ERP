<template>
  <v-container>
    <v-card flat>
      <v-card-title>
        <v-banner
          lines="one"
          icon="$error"
          color="error">
          <v-banner-text class="text-h4">
            错误信息
          </v-banner-text>
          <template v-slot:actions>
            <v-btn
              size="large"
              @click="goHome"
              color="primary"
              variant="elevated"
              prepend-icon="mdi-subdirectory-arrow-left">
              返回
            </v-btn>
          </template>
        </v-banner>
      </v-card-title>
      <v-card-item>
        <v-alert
          v-for="(value, key) in $route.query"
          :key="key"
          :title="key"
          :text="value"
          class="mb-4"
          type="error"
          border="start"
          variant="tonal"
          density="compact"
        ></v-alert>
      </v-card-item>
    </v-card>
  </v-container>
</template>

<script setup>
import {useRouter} from 'vue-router'

const router = useRouter()

function goHome() {
  router.push({
    name: 'Home'
  })
}
</script>

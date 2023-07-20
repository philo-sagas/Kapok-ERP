<template>
  <v-container class="mt-6">
    <div class="d-flex justify-center">
      <v-icon
        size="large"
        icon="mdi-store"
        color="blue-darken-2"
        class="align-self-center"></v-icon>
      <div class="text-h6 pa-2">Kapok-ERP</div>
    </div>
    <v-card
      rounded="lg"
      elevation="8"
      max-width="448"
      class="mx-auto pa-12">
      <v-alert
        v-if="false"
        closable
        class="mb-4"
        type="error"
        border="start"
        variant="tonal"
        density="compact"
        title="登录失败"
        text="用户名或密码错误"
      ></v-alert>
      <v-alert
        v-if="false"
        closable
        class="mb-4"
        type="success"
        border="start"
        variant="tonal"
        density="compact"
        title="登出成功"
        text="您已经登出"
      ></v-alert>
      <div class="text-subtitle-1 text-medium-emphasis">用户账号</div>
      <v-text-field
        autofocus
        name="username"
        density="compact"
        variant="outlined"
        placeholder="请输入用户账号"
        prepend-inner-icon="mdi-account">
      </v-text-field>
      <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
        用户密码
        <a class="text-caption text-decoration-none text-blue" style="display: none;" href="javascript:void(0)"
           tabindex="-1">忘记密码？</a>
      </div>
      <v-text-field
        name="password"
        density="compact"
        variant="outlined"
        placeholder="请输入用户密码"
        prepend-inner-icon="mdi-lock-outline"
        :type="visible ? 'text' : 'password'"
        :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visible = !visible">
      </v-text-field>
      <v-btn
        block
        size="large"
        @click="login"
        color="primary"
        variant="elevated">
        登录
      </v-btn>
    </v-card>
  </v-container>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'

const router = useRouter()
const visible = ref(false)

function login() {
  router.push({
    name: 'Home'
  })
}
</script>

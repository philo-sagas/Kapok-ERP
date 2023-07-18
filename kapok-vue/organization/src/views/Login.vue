<template>
  <v-app full-height class="overflow-hidden">
    <v-row class="align-center">
      <v-col class="justify-center">
        <div class="d-flex justify-center mb-2">
          <v-icon
            size="large"
            color="blue-darken-2"
            icon="mdi-store"
            class="align-self-center"/>
          <div class="text-h6 pa-2">
            Kapok-ERP
          </div>
        </div>

        <v-card
          class="mx-auto pa-12 pb-8"
          elevation="8"
          max-width="448"
          rounded="lg"
        >
          <div class="text-subtitle-1 text-medium-emphasis">用户账号</div>

          <v-text-field
            density="compact"
            placeholder="Enter your username"
            prepend-inner-icon="mdi-account"
            variant="outlined"
          ></v-text-field>

          <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
            用户密码
            <a
              class="text-caption text-decoration-none text-blue"
              href="javascript:void(0)"
            >
              忘记密码？</a>
          </div>

          <v-text-field
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            placeholder="Enter your password"
            prepend-inner-icon="mdi-lock-outline"
            variant="outlined"
            @click:append-inner="visible = !visible"
          ></v-text-field>

          <v-btn
            block
            class="mb-8"
            color="blue"
            size="large"
            variant="tonal"
            @click="login"
          >
            登录
          </v-btn>
        </v-card>
      </v-col>
    </v-row>
  </v-app>
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

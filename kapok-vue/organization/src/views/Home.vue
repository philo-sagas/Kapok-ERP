<template>
  <v-container>
    <v-row>
      <v-col
        v-for="menu in moduleMenus"
        :key="menu.value"
        cols="12"
        md="4"
      >
        <v-card
          class="d-flex align-center bg-indigo-lighten-1"
          height="200"
          dark
          :to="menu.props.to"
        >
          <div
            class="text-h3 flex-grow-1 text-center"
          >
            <v-icon
              :icon="menu.props.prependIcon"
            />
            {{ menu.title }}
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import {useAppStore} from '@/store/app'

const appStore = useAppStore()
const moduleMenus = appStore.getAuthModuleMenus('OrganizationService')

</script>

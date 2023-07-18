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
import {ref} from 'vue'

const moduleMenus = ref([
  {
    title: '组织管理',
    value: 'organization',
    props: {
      prependIcon: 'mdi-account-group',
      to: {
        name: 'OrganizationOrganization'
      }
    }
  },
  {
    title: '用户管理',
    value: 'user',
    props: {
      prependIcon: 'mdi-account',
      to: {
        name: 'OrganizationUser'
      }
    }
  },
  {
    title: '角色管理',
    value: 'role',
    props: {
      prependIcon: 'mdi-clipboard-account',
      to: {
        name: 'OrganizationRole'
      }
    }
  },
  {
    title: '权限管理',
    value: 'permission',
    props: {
      prependIcon: 'mdi-shield-account',
      to: {
        name: 'OrganizationPermission'
      }
    }
  },
  {
    title: '数据字典',
    value: 'dictionary',
    props: {
      prependIcon: 'mdi-book-alphabet',
      to: {
        name: 'OrganizationDictionary'
      }
    }
  }
])
</script>

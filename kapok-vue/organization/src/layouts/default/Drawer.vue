<template>
  <v-navigation-drawer
    :model-value="props.drawer"
    @update:modelValue="$emit('update:drawer', $event)"
    :expand-on-hover="!mobile"
    :rail="!mobile"
    elevation="2"
  >
    <v-list>
      <v-list-item
        prepend-avatar="@/assets/avatar.jpg"
        title="admin"
        subtitle="admin@gmailcom"
      ></v-list-item>
    </v-list>

    <v-divider></v-divider>

    <v-list
      :items="menus"
      density="compact"
      nav>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup>
import {ref} from 'vue'
import {useDisplay} from 'vuetify'

const props = defineProps({
  drawer: Boolean
})
defineEmits(['update:drawer'])
const {mobile} = useDisplay()

const menus = ref([
  {
    title: '组织模块',
    value: 'organization-module',
    props: {
      prependIcon: 'mdi-crowd'
    },
    children: [
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
    ]
  },
  {
    title: '库存模块',
    value: 'mdi-store-marker',
    props: {
      prependIcon: 'mdi-store-marker'
    }
  },
  {
    title: '采购模块',
    value: 'purchase',
    props: {
      prependIcon: 'mdi-purse'
    }
  },
  {
    title: '运输模块',
    value: 'transport',
    props: {
      prependIcon: 'mdi-train-car'
    }
  },
  {
    title: '财务模块',
    value: 'finance',
    props: {
      prependIcon: 'mdi-finance'
    }
  }
])
</script>

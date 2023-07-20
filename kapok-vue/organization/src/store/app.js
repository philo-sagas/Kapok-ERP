import {defineStore} from 'pinia'
import {computed, nextTick, ref} from 'vue'
import {obtainIntrospect, obtainUserinfo} from '@/utils/authorization'

export const useAppStore = defineStore('app', () => {
  const user = ref({})
  const authorities = ref([])
  const menus = ref([
    {
      title: '组织模块',
      value: 'OrganizationService',
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
/*
    {
      title: '库存模块',
      value: 'StoreMarkerService',
      props: {
        prependIcon: 'mdi-store-marker'
      }
    },
    {
      title: '采购模块',
      value: 'PurchaseService',
      props: {
        prependIcon: 'mdi-purse'
      }
    },
    {
      title: '运输模块',
      value: 'TransportService',
      props: {
        prependIcon: 'mdi-train-car'
      }
    },
    {
      title: '财务模块',
      value: 'FinanceService',
      props: {
        prependIcon: 'mdi-finance'
      }
    }
*/
  ])
  const message = ref()
  const dialogConfig = ref()


  const authMenus = computed(() => {
    const root = {children: menus.value}
    const queue = [root]
    while (queue.length) {
      const node = queue.pop()
      if (node.children && node.children.length) {
        node.children = node.children.filter(n => authorities.value.includes(n.value))
        queue.push(...node.children)
      }
    }
    let m = root.children
    while (m && m.length == 1 && m[0].children && m[0].children.length) {
      m = m[0].children
    }
    return m
  })


  function hasAuthority(value) {
    return authorities.value.includes(value)
  }

  function getAuthModuleMenus(module) {
    return computed(() => {
      const queue = [...menus.value]
      while (queue.length) {
        const node = queue.pop()
        if (node.value == module) {
          return node.children || []
        }
        if (node.children && node.children.length) {
          node.children = node.children.filter(n => authorities.value.includes(n.value))
          queue.push(...node.children)
        }
      }
      return []
    })
  }

  function refreshAuth() {
    obtainIntrospect().then(data => {
      if (data.active) {
        authorities.value = data.authorities
      }
    })
    obtainUserinfo().then(data => {
      if (data instanceof Object) {
        user.value.sub = data.sub
        user.value.name = data.name
      }
    })
  }

  function alertMessage(msg) {
    message.value = null
    nextTick(() => message.value = msg)
  }

  function confirmMessage(config) {
    dialogConfig.value = null
    nextTick(() => dialogConfig.value = {
      title: '确定提示',
      message: '请确定操作是否继续执行？',
      ...config
    })
  }


  return {
    /* state */
    user,
    authorities,
    message,
    dialogConfig,

    /* getters */
    authMenus,

    /* actions */
    hasAuthority,
    getAuthModuleMenus,
    refreshAuth,
    alertMessage,
    confirmMessage
  }
})

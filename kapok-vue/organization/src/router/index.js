// Composables
import {createRouter, createWebHistory} from 'vue-router'
import {obtainAccessToken} from '@/utils/authorization'

const routes = [
  {
    path: '/',
    name: 'Home',
    beforeEnter: (to, from, next) => {
      if (to.query.code) {
        obtainAccessToken(to.query.code).then(() => next({name: 'OrganizationHome'}))
      } else {
        next({name: 'OrganizationHome'})
      }
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: {name: 'Home'}
  },
  {
    path: '/organization',
    component: () => import('@/layouts/NavLayout.vue'),
    children: [
      {
        path: '',
        name: 'OrganizationHome',
        component: () => import(/* webpackChunkName: "organization" */ '@/views/Home.vue'),
      },
      {
        path: 'user',
        children: [
          {
            path: '',
            name: 'OrganizationUser',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/user/User.vue'),
            meta: {
              breadcrumbs: [
                {
                  title: '用户管理',
                  to: {
                    name: 'OrganizationUser'
                  }
                }
              ]
            }
          },
          {
            path: ':id',
            name: 'OrganizationUserEdit',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/user/UserEdit.vue'),
            props: true,
            meta: {
              breadcrumbs: [
                {
                  title: '用户管理',
                  to: {
                    name: 'OrganizationUser'
                  }
                },
                {
                  title: '编辑',
                  to: {
                    name: 'OrganizationUserEdit'
                  }
                }
              ]
            }
          }
        ]
      },
      {
        path: 'role',
        children: [
          {
            path: '',
            name: 'OrganizationRole',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/role/Role.vue'),
            meta: {
              breadcrumbs: [
                {
                  title: '角色管理',
                  to: {
                    name: 'OrganizationRole'
                  }
                }
              ]
            }
          },
          {
            path: ':id',
            name: 'OrganizationRoleEdit',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/role/RoleEdit.vue'),
            props: true,
            meta: {
              breadcrumbs: [
                {
                  title: '角色管理',
                  to: {
                    name: 'OrganizationRole'
                  }
                },
                {
                  title: '编辑',
                  to: {
                    name: 'OrganizationRoleEdit'
                  }
                }
              ]
            }
          }
        ]
      },
      {
        path: 'permission',
        children: [
          {
            path: '',
            name: 'OrganizationPermission',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/permission/Permission.vue'),
            meta: {
              breadcrumbs: [
                {
                  title: '权限管理',
                  to: {
                    name: 'OrganizationPermission'
                  }
                }
              ]
            }
          },
          {
            path: ':id',
            name: 'OrganizationPermissionEdit',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/permission/PermissionEdit.vue'),
            props: true,
            meta: {
              breadcrumbs: [
                {
                  title: '权限管理',
                  to: {
                    name: 'OrganizationPermission'
                  }
                },
                {
                  title: '编辑',
                  to: {
                    name: 'OrganizationPermissionEdit'
                  }
                }
              ]
            }
          }
        ]
      },
      {
        path: 'organization',
        children: [
          {
            path: '',
            name: 'OrganizationOrganization',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/organization/Organization.vue'),
            meta: {
              breadcrumbs: [
                {
                  title: '组织架构管理',
                  to: {
                    name: 'OrganizationOrganization'
                  }
                }
              ]
            }
          },
          {
            path: ':id',
            name: 'OrganizationOrganizationEdit',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/organization/OrganizationEdit.vue'),
            props: true,
            meta: {
              breadcrumbs: [
                {
                  title: '组织架构管理',
                  to: {
                    name: 'OrganizationOrganization'
                  }
                },
                {
                  title: '编辑',
                  to: {
                    name: 'OrganizationOrganizationEdit'
                  }
                }
              ]
            }
          }
        ]
      },
      {
        path: 'dictionary',
        children: [
          {
            path: '',
            name: 'OrganizationDictionary',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/dictionary/Dictionary.vue'),
            meta: {
              breadcrumbs: [
                {
                  title: '数据字典管理',
                  to: {
                    name: 'OrganizationDictionary'
                  }
                }
              ]
            }
          },
          {
            path: ':id',
            name: 'OrganizationDictionaryEdit',
            component: () => import(/* webpackChunkName: "organization" */ '@/views/dictionary/DictionaryEdit.vue'),
            props: true,
            meta: {
              breadcrumbs: [
                {
                  title: '数据字典管理',
                  to: {
                    name: 'OrganizationDictionary'
                  }
                },
                {
                  title: '编辑',
                  to: {
                    name: 'OrganizationDictionaryEdit'
                  }
                }
              ]
            }
          }
        ]
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

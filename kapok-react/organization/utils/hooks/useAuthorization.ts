'use client'

import {useEffect, useState} from 'react'
import {obtainIntrospect, obtainUserinfo} from '@/utils/authorization'

interface User {
  sub: string,
  name: string,
  nickname: string,
  gender: string,
  picture: string,
  birthdate: string
}

interface Menu {
  code: string,
  name: string,
  icon: string,
  href: string
}

const menus: Menu[] = [
  {
    code: 'organization',
    name: '组织管理',
    icon: 'groups',
    href: '/dashboard/organization'
  },
  {
    code: 'user',
    name: '用户管理',
    icon: 'person',
    href: '/dashboard/user'
  },
  {
    code: 'role',
    name: '角色管理',
    icon: 'assignment_ind',
    href: '/dashboard/role'
  },
  {
    code: 'permission',
    name: '权限管理',
    icon: 'admin_panel_settings',
    href: '/dashboard/permission'
  },
  {
    code: 'dictionary',
    name: '数据字典',
    icon: 'book',
    href: '/dashboard/dictionary'
  }
]


function useAuthorization() {
  const [user, setUser] = useState<User>({})
  const [authorities, setAuthorities] = useState<string[]>([])

  const authMenus = menus.filter(node => authorities.includes(node.code))

  function hasAuthority(value) {
    return authorities.includes(value)
  }

  useEffect(() => {
    obtainIntrospect().then(data => {
      if (data.active) {
        setAuthorities(data.authorities)
      }
    })
    obtainUserinfo().then(data => {
      if (data instanceof Object) {
        setUser(data)
      }
    })
  }, [])

  return {
    /* state */
    user,
    authorities,
    authMenus,

    /* actions */
    hasAuthority
  }
}

export default useAuthorization

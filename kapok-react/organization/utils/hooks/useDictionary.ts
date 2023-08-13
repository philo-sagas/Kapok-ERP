'use client'

import {useEffect, useState} from 'react'

interface Dictionary {
  value: {
    title: string,
    value: string
  }[],
  mapping: object
}

function useDictionary(code: string) {
  const [dict, setDict] = useState<Dictionary>({
    value: [],
    mapping: {}
  })

  useEffect(() => {
    const access_token = window.sessionStorage.getItem('access_token')
    fetch(`/api/organization/v1/dictionary/via/${code}`, {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + access_token
      }
    })
      .then((response) => response.json())
      .then((data) => {
        let value = data.data
        const mapping = {}
        if (value instanceof Array) {
          value.forEach(i => {
            i.code = convertType(i.code)
            mapping[i.code] = i.value
          })
          value = value.map(i => ({title: i.value, value: i.code}))
        } else {
          mapping[code] = value
        }
        setDict({value, mapping})
      })
  }, [])

  return dict
}

function convertType(value) {
  // return /^true$|^false$|^\d+$/.test(value) ? eval(value) : value
  if (/^true$/.test(value)) {
    return true
  } else if (/^false$/.test(value)) {
    return false
  } else if (/^\d+$/.test(value)) {
    return Number(value)
  }
  return value
}

export default useDictionary

import {defineStore} from 'pinia'
import {computed, ref} from 'vue'
import axios from 'axios'

export const useDictionaryStore = defineStore('dictionary', () => {
  const dict = ref({}) // { code: { value: Array | String, mapping: Object } }

  function obtainDictionary(code) {
    if (!dict.value[code]) {
      dict.value[code] = {value: [], mapping: {}}
      axios.get(`/api/organization/v1/dictionary/via/${code}`).then(response => {
        let value = response.data.data
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
        dict.value[code] = {value, mapping}
      })
    }
    return dict.value[code]
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

  function getValue(code) {
    return computed(() => {
      const item = obtainDictionary(code)
      return item.value
    })
  }

  function getMapping(code) {
    return computed(() => {
      const item = obtainDictionary(code)
      return item.mapping
    })
  }

  return {
    getValue,
    getMapping
  }
})

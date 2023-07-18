import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useRuleStore = defineStore('rule', () => {
  const rules = ref({
    required: value => !!value || '该字段为必填项！',
    counter10: value => !value ? true : value.length <= 10 || '最多输入10个字符！',
    counter20: value => !value ? true : value.length <= 20 || '最多输入20个字符！',
    counter30: value => !value ? true : value.length <= 30 || '最多输入30个字符！',
    counter40: value => !value ? true : value.length <= 40 || '最多输入40个字符！',
    counter50: value => !value ? true : value.length <= 50 || '最多输入50个字符！',
    counter500: value => !value ? true : value.length <= 500 || '最多输入500个字符！',
    counter1000: value => !value ? true : value.length <= 1000 || '最多输入1000个字符！',
    mobile: value => {
      if (!value) return true
      const pattern = /^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}$/
      return pattern.test(value) || '无效手机号码！'
    },
    email: value => {
      if (!value) return true
      const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      return pattern.test(value) || '无效电子邮件！'
    }
  })
  return {rules}
})

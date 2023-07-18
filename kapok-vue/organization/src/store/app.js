import {defineStore} from 'pinia'
import {computed, ref} from 'vue'

export const useAppStore = defineStore('app', () => {
  const message = ref(null)
  const hasMessage = computed({
    get() {
      return message.value != null
    },
    set() {
      message.value = null
    }
  })
  function setMessage(msg) {
    message.value = null
    message.value = msg
  }
  return {
    message,
    hasMessage,
    setMessage
  }
})

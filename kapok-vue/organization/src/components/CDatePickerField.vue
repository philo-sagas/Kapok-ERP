<template>
  <v-menu
    v-model="showDatePicker"
    :close-on-content-click="false">
    <template #activator="{ props }">
      <v-text-field
        readonly
        clearable
        v-bind="$attrs"
        :model-value="dateFormatted"
        @click:clear="onUpdateModelValue([])"
      >
        <template #append-inner>
          <v-icon v-bind="props">mdi-calendar</v-icon>
        </template>
      </v-text-field>
    </template>
    <v-date-picker
      hide-actions
      @update:modelValue="onUpdateModelValue">
    </v-date-picker>
  </v-menu>
</template>

<script setup>
import {computed, ref} from 'vue'
import {useDate} from 'vuetify/labs/date'

defineOptions({
  inheritAttrs: false
})
const props = defineProps({
  modelValue: [Date, String]
})
const emit = defineEmits(['update:modelValue'])

const date = useDate()
const showDatePicker = ref(false)
const dateFormatted = computed(() => {
  return props.modelValue && date.format(props.modelValue, 'fullDateTime24h')
})

function onUpdateModelValue(value) {
  showDatePicker.value = false
  emit('update:modelValue', value[0])
}
</script>
